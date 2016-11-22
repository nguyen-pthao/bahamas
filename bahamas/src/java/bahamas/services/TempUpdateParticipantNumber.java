package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bahamas.dao.ConnectionManager;
import bahamas.dao.EventDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.entity.Event;
import bahamas.entity.EventParticipant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tan.si.hao
 */
@WebServlet(urlPatterns = {"/temp.updateparticipantnumber"})
public class TempUpdateParticipantNumber extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JsonObject json = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            EventDAO eventDAO = new EventDAO();
            ArrayList<Event> eventList = eventDAO.retrieveAllEvents();
            HashMap<Integer, Integer> eventIdParticipantHM = new HashMap<Integer, Integer>();

            for (Event event : eventList) {
                ArrayList<EventParticipant> eventParticipantList = EventParticipantDAO.retrieveEventParticipantbyEventID(event.getEventId());
                int totalParticipant = 0;
                for (EventParticipant eventParticipant : eventParticipantList) {
                    if (!eventParticipant.isPullout()) {
                        totalParticipant++;
                    }
                }
                eventIdParticipantHM.put(event.getEventId(), totalParticipant);
            }

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                //get database connection
                conn = ConnectionManager.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.prepareStatement("UPDATE EVENT SET PARTICIPANT_NUMBER=? WHERE EVENT_ID= ? ");
                
                for(int tempEventId : eventIdParticipantHM.keySet()){
                    int tempParticipentNumber = eventIdParticipantHM.get(tempEventId);
                    stmt.setInt(1, tempParticipentNumber);
                    stmt.setInt(2, tempEventId);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                conn.commit();
                json.addProperty("message", "success");
                out.println(gson.toJson(json));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }

        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
