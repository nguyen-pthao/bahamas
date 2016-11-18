/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.entity.Contact;
import bahamas.entity.EventParticipant;
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
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tan.si.hao
 */
@WebServlet(name = "AddEventParticipantRemarkComment", urlPatterns = {"/event.addeventremarks"})
public class AddEventParticipantRemarkComment extends HttpServlet {

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
                
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String token = Validator.containsBlankField(jobject.get("token"));
                String roleId = Validator.containsBlankField(jobject.get("role_id"));
                String remarks = Validator.containsBlankField(jobject.get("remarks"));
                String participantId = "";
                String awardHours = "";
                if(jobject.has("participant_id")){
                    participantId = Validator.containsBlankField(jobject.get("participant_id"));
                }
                if(jobject.has("award_hours")){
                    awardHours = Validator.containsBlankField(jobject.get("award_hours"));
                }
                
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
                    }else{
                        if(remarks == null || roleId == null || participantId == null){
                            json.addProperty("message", "error");
                            if(remarks == null){
                                jsonErrorMsgArray.add(new JsonPrimitive("Missing remarks"));
                            }
                            if(roleId == null){
                                jsonErrorMsgArray.add(new JsonPrimitive("Missing role ID"));
                            }
                            if(participantId == null){
                                jsonErrorMsgArray.add(new JsonPrimitive("Missing participant ID"));
                            }
                            json.add("errorMsg", jsonErrorMsgArray);
                            out.println(gson.toJson(json));
                            return;
                        }
                        EventParticipant eventParticipant = null;
                        if(!participantId.isEmpty() || !awardHours.isEmpty()){
                            //adding service comment
                            eventParticipant = EventParticipantDAO.retrieveParticipantbyRoleIDContactID(Integer.parseInt(roleId), Integer.parseInt(participantId));
                            if(!participantId.isEmpty()){
                                eventParticipant.setService_comment(remarks);
                            }
                            if(awardHours != null && !awardHours.isEmpty()){
                                eventParticipant.setHoursServed(Double.parseDouble(awardHours));
                            }
                        }else{
                            //user adding his/her remark for the event
                            eventParticipant = EventParticipantDAO.retrieveParticipantbyRoleIDContactID(Integer.parseInt(roleId), contact.getContactId());
                            eventParticipant.setRemarks(remarks);
                        }
                        
                        //EventParticipant eventParticipant = new EventParticipant(contact.getContactId(), null, Integer.parseInt(roleId), Integer.parseInt(eventId), username, false, null, null, 0, null, null);
                        if(EventParticipantDAO.updateEventRole(eventParticipant)){
                            if(!participantId.isEmpty()){
                                AuditLogDAO.insertAuditLog(username, "Add service comment", "Add service comment under contact: Contact ID: " + contact.getContactId());
                            }else{
                                AuditLogDAO.insertAuditLog(username, "Add remarks", "Add remarks under contact: Contact ID: " + contact.getContactId());
                            }
                            json.addProperty("message", "success");
                            out.println(gson.toJson(json));
                        }else{
                            json.addProperty("message", "fail to insert DB");
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
