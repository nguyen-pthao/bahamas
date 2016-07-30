package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bahamas.dao.ContactDAO;
import bahamas.dao.EventDAO;
import bahamas.entity.Contact;
import bahamas.entity.Event;
import bahamas.util.Authenticator;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tan.si.hao
 */
@WebServlet(urlPatterns = {"/event.retrieveupcoming"})
public class RetrieveAllUpcomingEvents extends HttpServlet {

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

            //Retrieve the json string as a reader 
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = request.getReader();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));
                return;
            }

            String jsonLine = sb.toString();
            if (jsonLine == null || jsonLine.isEmpty()) {
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));

            } else {
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String token = Validator.containsBlankField(jobject.get("token"));
                String username = Authenticator.verifyToken(token);
                
                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                } else {
                    //Verified token
                    ContactDAO cDAO = new ContactDAO();
                    Contact contact = cDAO.retrieveContactByUsername(username);

                    if (contact == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {    
                        //Only Admin and tm are able to create an event
                            
                            EventDAO eventDAO = new EventDAO();
                            ArrayList<Event> eventList = eventDAO.retrieveAllEvents();
                            SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
                            JsonArray eventArray = new JsonArray();
                            JsonObject jsonContactObj;
                            if(eventList != null){
                                json.addProperty("message", "success");
                                for(int i = 0; i < eventList.size(); i++){
                                    Event event = eventList.get(i);
                                    Date currentDateTime = new Date();
                                    try {
                                        Date currentDate = date.parse(date.format(currentDateTime));
                                        Date currentTime = time.parse(time.format(currentDateTime));
                                        Date eventEndDate = date.parse(date.format(event.getEventEndDate()));
                                        Date eventEndTime = time.parse(time.format(event.getEventEndTime()));
                                        if(eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate)&& eventEndTime.after(currentTime))){
                                            
                                            //view for novcie
                                            if(contact.isIsNovice()){
                                                if(event.getEventClassName().equals("Training")){
                                                    jsonContactObj = new JsonObject();
                                                    jsonContactObj.addProperty("event_id", event.getEventId());
                                                    jsonContactObj.addProperty("event_title", event.getEventTitle());
                                                    jsonContactObj.addProperty("event_start_date", date.format(event.getEventStartDate()));
                                                    jsonContactObj.addProperty("event_end_date", date.format(event.getEventEndDate()));
                                                    jsonContactObj.addProperty("event_time_start", time.format(event.getEventStartTime()));
                                                    jsonContactObj.addProperty("event_time_end", time.format(event.getEventEndTime()));
                                                    jsonContactObj.addProperty("event_class", event.getEventClassName());
                                                    eventArray.add(jsonContactObj);
                                                }
                                            }else{
                                                //view for all other users
                                                jsonContactObj = new JsonObject();
                                                jsonContactObj.addProperty("event_id", event.getEventId());
                                                jsonContactObj.addProperty("event_title", event.getEventTitle());
                                                jsonContactObj.addProperty("event_start_date", date.format(event.getEventStartDate()));
                                                jsonContactObj.addProperty("event_end_date", date.format(event.getEventEndDate()));
                                                jsonContactObj.addProperty("event_time_start", time.format(event.getEventStartTime()));
                                                jsonContactObj.addProperty("event_time_end", time.format(event.getEventEndTime()));
                                                jsonContactObj.addProperty("event_class", event.getEventClassName());
                                                eventArray.add(jsonContactObj);
                                            }


                                        }
                                    } catch (ParseException ex) {
                                        Logger.getLogger(RetrieveAllUpcomingEvents.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                                json.add("event", eventArray);
                                out.println(gson.toJson(json));
                            }else{
                                json.addProperty("message", "Fail retrieve all events");
                                out.println(gson.toJson(json));
                            }
                    }
                }
                
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
