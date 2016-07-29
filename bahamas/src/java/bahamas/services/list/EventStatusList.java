/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services.list;

import bahamas.dao.list.EventStatusListDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Darryl Mok
 */
@WebServlet(name = "EventStatusList", urlPatterns = {"/eventstatuslist"})
public class EventStatusList extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/JSON;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JsonObject json = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();


            /*          
             String token = request.getParameter("token");
             if (!Authenticator.verifyToken(token)) {
             json.addProperty("message", "invalid token");
             out.println(gson.toJson(json));

             } else {
             //Verified
             */
            //Validation
            //Create new contact object
            EventStatusListDAO eventStatusListDAO = new EventStatusListDAO();
            ArrayList<String> eventStatusList = eventStatusListDAO.retrieveEventStatusList();

            if (!eventStatusList.isEmpty()) {
                json.addProperty("message", "success");

                JsonArray eventStatusArray = new JsonArray();
                JsonObject jsonStatusClassObj;

                for (String s : eventStatusList) {

                    jsonStatusClassObj = new JsonObject();
                    jsonStatusClassObj.addProperty("eventStatus", s);

                    eventStatusArray.add(jsonStatusClassObj);

                }
                json.add("eventStatusList", eventStatusArray);
                out.println(gson.toJson(json));
            } else {
                json.addProperty("message", "failed");
                out.println(gson.toJson(json));
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
