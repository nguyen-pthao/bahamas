/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EventDAO;
import bahamas.dao.RoleCheckDAO;
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
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *
 * @author tan.si.hao
 */
@WebServlet(name = "AddEvent", urlPatterns = {"/event.create"})
public class AddEvent extends HttpServlet {

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
            JsonArray jsonErrorMsgArray = new JsonArray();
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
                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String token = Validator.containsBlankField(jobject.get("token"));
                String eventTitle = Validator.containsBlankField(jobject.get("event_title"));
                Date eventStartDate = Validator.isDateValid(jobject.get("event_start_date"), "event_start_date");
                Date eventEndDate = Validator.isDateValid(jobject.get("event_end_date"), "event_end_date");
                Date eventTimeStart = Validator.isDateValid(jobject.get("event_time_start"), "event_time_start");
                Date eventTimeEnd = Validator.isDateValid(jobject.get("event_time_end"),"event_time_end");
                String eventStatus = Validator.containsBlankField(jobject.get("event_status"));
                boolean sendReminder = jobject.get("send_reminder").getAsBoolean();
                String eventDescription = Validator.containsBlankField(jobject.get("event_description"));
                String minimumParticipation = jobject.get("minimum_participation").getAsString();
                String eventClass = Validator.containsBlankField(jobject.get("event_class"));
                String eventLocation = jobject.get("event_location").getAsString();
                String explainIfOthers = jobject.get("explain_if_others").getAsString();
                String eventLat = jobject.get("event_lat").getAsString();
                String eventLng = jobject.get("event_lng").getAsString();
                String username = Authenticator.verifyToken(token);
                boolean ignore = jobject.get("ignore").getAsBoolean();
                
                
                if(eventClass == null || eventStartDate == null || eventLocation == null || eventTimeEnd == null || eventTimeStart == null || eventTitle == null || eventEndDate == null){
                    json.addProperty("message", "Missing fields");
                    if(eventClass == null){
                        //json.addProperty("event_class", "Missing Event Class");
                        jsonErrorMsgArray.add(new JsonPrimitive("Missing Event Class"));        
                    }
                    if(eventStartDate == null){
                        //json.addProperty("event_start_date", "Missing Event Start Date");
                        jsonErrorMsgArray.add(new JsonPrimitive("Missing Event Start Date"));
                    }
                    if(eventEndDate == null){
                        //json.addProperty("event_end_date", "Missing Event End Date");
                        jsonErrorMsgArray.add(new JsonPrimitive("Missing Event End Date"));
                    }
                    if(eventLocation == null){
                        //json.addProperty("event_location", "Missing Event Location");
                        jsonErrorMsgArray.add(new JsonPrimitive("Missing Event Location"));
                    }
                    if(eventTimeEnd == null){
                        //json.addProperty("event_time_end", "Missing Event Time End");
                        jsonErrorMsgArray.add(new JsonPrimitive("Missing Event Time End"));
                    }
                    if(eventTimeStart == null){
                        //json.addProperty("event_time_start", "Missing Event Time Start");
                        jsonErrorMsgArray.add(new JsonPrimitive("Missing Event Time Start"));
                    }
                    if(eventTitle == null){
                        //json.addProperty("event_title", "Missing Event Title");
                        jsonErrorMsgArray.add(new JsonPrimitive("Missing Event Title"));
                    }
                    json.add("errorMsg", jsonErrorMsgArray);
                    out.println(gson.toJson(json));
                    return;
                }
                
                if(eventTimeEnd.before(eventTimeStart) || eventTimeEnd.equals(eventTimeStart)){
                    json.addProperty("message", "dateError");
                    jsonErrorMsgArray.add(new JsonPrimitive("Start time cannot be after end time"));  
                    json.add("errorMsg", jsonErrorMsgArray);
                    out.println(gson.toJson(json));
                    return;
                }
                
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
                    } else {    
                        //Only Admin, tm and eventleader are able to create an event
                        if(contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || RoleCheckDAO.checkRole(contact.getContactId(), "eventleader") ){
                            
                            //check if exist
                            Event event = new Event(eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, explainIfOthers, eventDescription, Integer.parseInt(minimumParticipation), sendReminder, eventClass, eventLocation, eventLat, eventLng, eventStatus);                         
                            String errorMsg = EventDAO.eventExist(event);
                            
                            if(errorMsg != null && !ignore){
                                json = new JsonObject();
                                jsonErrorMsgArray = new JsonArray();
                                jsonErrorMsgArray.add(new JsonPrimitive(errorMsg)); 
                                json.addProperty("message", "conflict");
                                json.add("errorMsg", jsonErrorMsgArray);
                                out.println(gson.toJson(json));
                                return;
                            }
                            
                            int eventID = EventDAO.addEvent(event,username);
                            if(eventID > 0){
                                AuditLogDAO.insertAuditLog(username, "ADD EVENT", "Add event under contact: Contact ID: " + contact.getContactId() + " | Event ID: " + eventID);
                                json.addProperty("message", "success");
                                json.addProperty("event_id", Integer.toString(eventID));
                                out.println(gson.toJson(json));
                            }else{
                                json.addProperty("message", "Fail to insert");
                                out.println(gson.toJson(json));
                            }
                                
                        }else{
                            json.addProperty("message", "fail");
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
