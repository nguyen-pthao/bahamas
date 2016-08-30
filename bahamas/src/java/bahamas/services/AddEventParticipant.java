/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AppNotificationDAO;
import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EventDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.AppNotification;
import bahamas.entity.Contact;
import bahamas.entity.Event;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tan.si.hao
 */
@WebServlet(name = "AddEventParticipant", urlPatterns = {"/event.join"})
public class AddEventParticipant extends HttpServlet {

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
                String nameOfFailedAddRole = "";

                String token = Validator.containsBlankField(jobject.get("token"));
                String eventId = Validator.containsBlankField(jobject.get("event_id"));
                String roleId = Validator.containsBlankField(jobject.get("event_role_id"));
                JsonArray targetContactidArray = null;
                if (jobject.has("contact_id_list")) {
                    targetContactidArray = jobject.get("contact_id_list").getAsJsonArray();
                }
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                } else {
                    //Verified token
                    ContactDAO cDAO = new ContactDAO();
                    Contact contact = cDAO.retrieveContactByUsername(username);
                    boolean canAddTarget = false;
                    if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId))) {
                        canAddTarget = true;
                    }

                    if (contact == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {
                        if (eventId == null || roleId == null) {
                            json.addProperty("message", "error");
                            if (eventId == null) {
                                jsonErrorMsgArray.add(new JsonPrimitive("Missing event ID"));
                            }
                            if (roleId == null) {
                                jsonErrorMsgArray.add(new JsonPrimitive("Missing role ID"));
                            }
                            json.add("errorMsg", jsonErrorMsgArray);
                            out.println(gson.toJson(json));
                            return;
                        }
                        EventParticipant eventParticipant = null;
                        EventDAO eventDAO = new EventDAO();
                        Event event = eventDAO.retrieveEventById(Integer.parseInt(eventId));
                        
                        if (targetContactidArray != null && canAddTarget) {
                            for (int i = 0; i < targetContactidArray.size(); i++) {
                                int targetContactid = targetContactidArray.get(i).getAsInt();
                                Contact contact1 = cDAO.retrieveContactById(targetContactid);
                                if (contact1 != null) {
                                    eventParticipant = new EventParticipant(contact1.getContactId(), null, Integer.parseInt(roleId), Integer.parseInt(eventId), username, false, null, null, 0, null, null);
                                }
                                if (contact1 != null && EventParticipantDAO.addEventParticipant(eventParticipant)) {
                                    if (event.isSendReminder()) {
                                        AppNotification appNotification = new AppNotification(event.getContactId(), Integer.parseInt(eventId), ".viewIndivEvent", contact1.getName() + " joined event " + event.getEventTitle() + ". Click to view event.", false);
                                        AppNotificationDAO.addAppNotification(appNotification);
                                    }
                                    AuditLogDAO.insertAuditLog(username, "JOIN EVENT", "Join event under contact: Contact ID: " + contact1.getContactId() + " | Event ID: " + eventId + " | Event role ID: " + roleId);
                                    //json.addProperty("message", "success");
                                    //out.println(gson.toJson(json));
                                } else {
                                    //json.addProperty("message", "fail to insert Contact ID: " + contact1.getContactId() + " to Event ID: " + eventId + " | Event role ID: " + roleId);
                                    nameOfFailedAddRole += targetContactid + ", ";
                                }
                                if (nameOfFailedAddRole.isEmpty()) {
                                    json.addProperty("message", "success");
                                } else {
                                    json.addProperty("message", "Fail to add contact id: " + nameOfFailedAddRole.substring(0, nameOfFailedAddRole.length() - 2));
                                }
                            }
                        } else {
                            eventParticipant = new EventParticipant(contact.getContactId(), null, Integer.parseInt(roleId), Integer.parseInt(eventId), username, false, null, null, 0, null, null);
                            if (EventParticipantDAO.addEventParticipant(eventParticipant)) {
                                if (event.isSendReminder()) {
                                    AppNotification appNotification = new AppNotification(event.getContactId(), Integer.parseInt(eventId), ".viewIndivEvent", "<b>" + contact.getName() + "</b> joined event <b>" + event.getEventTitle() + "</b>. Click to view event.", false);
                                    AppNotificationDAO.addAppNotification(appNotification);
                                }
                                AuditLogDAO.insertAuditLog(username, "JOIN EVENT", "Join event under contact: Contact ID: " + contact.getContactId() + " | Event ID: " + eventId + " | Event role ID: " + roleId);
                                json.addProperty("message", "success");
                                //out.println(gson.toJson(json));
                                //return;
                            } else {
                                json.addProperty("message", "fail to insert Contact ID: " + contact.getContactId() + " | Event ID: " + eventId + " | Event role ID: " + roleId);
                                //out.println(gson.toJson(json));
                                //return;
                            }
                        }
                        out.println(gson.toJson(json));
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
