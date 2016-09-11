/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AppNotificationDAO;
import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EventAffiliationDAO;
import bahamas.dao.EventDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.EventRoleAssignmentDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.AppNotification;
import bahamas.entity.Contact;
import bahamas.entity.Event;
import bahamas.entity.EventAffiliation;
import bahamas.entity.EventParticipant;
import bahamas.entity.EventRoleAssignment;
import bahamas.entity.TeamJoin;
import bahamas.util.Authenticator;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
@WebServlet(name = "DeleteEventRoles", urlPatterns = {"/event.deleterole"})
public class DeleteEventRoles extends HttpServlet {

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
                String eventId = Validator.containsBlankField(jobject.get("event_id"));
                String eventRoleId = Validator.containsBlankField(jobject.get("event_role_id"));
                boolean ignore = jobject.get("ignore").getAsBoolean();
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                } else {
                    //Verified token
                    ContactDAO cDAO = new ContactDAO();
                    Contact contact = cDAO.retrieveContactByUsername(username);

                    if (contact == null || eventId == null || eventRoleId == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {
                        EventDAO eventDAO = new EventDAO();
                        Event event = eventDAO.retrieveEventById(Integer.parseInt(eventId));
                        int participantCount = 0;

                        if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || event.getCreatedBy().equals(contact.getUsername())) {
                            if (EventParticipantDAO.roleExist(Integer.parseInt(eventRoleId)) && !ignore) {
                                json.addProperty("message", "Has participants");
                                json.addProperty("errorMsg", "There are participants signed up for this role. Are you sure you want to delete role?");
                                out.println(gson.toJson(json));
                                return;
                            }
                            EventRoleAssignment eventRoleAssignment = EventRoleAssignmentDAO.retrieveEventRoleByRoleId(Integer.parseInt(eventRoleId));
                            ArrayList<EventParticipant> eventParticipantList = EventParticipantDAO.retrieveEventParticipantbyEventID(Integer.parseInt(eventId));
                            for (EventParticipant eventParticipant : eventParticipantList) {
                                if (!eventParticipant.isPullout() && eventParticipant.getRoleID() == Integer.parseInt(eventRoleId)) {
                                    ContactDAO contactDAO = new ContactDAO();
                                    if (contactDAO.retrieveContactById(eventParticipant.getContactID()).getUsername() != null) {
                                        AppNotification appNotification = new AppNotification(eventParticipant.getContactID(), event.getEventId(), ".viewIndivEvent", "You have been removed from event \"" + event.getEventTitle() + "\". Click to view event.");
                                        AppNotificationDAO.addAppNotification(appNotification);
                                    }
                                    participantCount++;
                                }
                            }
                            if (EventParticipantDAO.deleteParticipantsByRoleId(Integer.parseInt(eventRoleId)) && EventRoleAssignmentDAO.deleteRoles(Integer.parseInt(eventRoleId))) {
                                AuditLogDAO.insertAuditLog(username, "DELETE ROLE", "Delete role under contact: Contact ID: " + contact.getContactId() + " | Event ID: " + eventId + " | Event role: " + eventRoleAssignment.getRoleName());

                                HashMap<String, String> teamHM = new HashMap<String, String>();
                                EventAffiliation eventAffiliation = EventAffiliationDAO.retrieveAllEventAffiliation(Integer.parseInt(eventId));
                                for (String tempTeam : eventAffiliation.getTeamArray()) {
                                    teamHM.put(tempTeam, tempTeam);
                                }
                                HashMap<Integer, String> cidNamePairHM = new HashMap<Integer, String>();
                                ContactDAO contactDAO = new ContactDAO();
                                ArrayList<Contact> contactList = contactDAO.retrieveAllContact();
                                if (contactList != null && !contactList.isEmpty()) {
                                    Iterator iter = contactList.iterator();
                                    while (iter.hasNext()) {
                                        Contact tempContact = (Contact) iter.next();
                                        ArrayList<TeamJoin> teamJoinList = TeamJoinDAO.retrieveAllTeamJoinCID(tempContact.getContactId());
                                        if (teamJoinList != null && !teamJoinList.isEmpty()) {
                                            Iterator iter2 = teamJoinList.iterator();
                                            while (iter2.hasNext()) {
                                                TeamJoin teamJoinTemp = (TeamJoin) iter2.next();
                                                if (teamHM.containsKey(teamJoinTemp.getTeamName())) {
                                                    cidNamePairHM.put(tempContact.getContactId(), tempContact.getName());
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int tempContactId : cidNamePairHM.keySet()) {
                                    if (contactDAO.retrieveContactById(tempContactId).getUsername() != null && event.getContactId() != contact.getContactId()) {
                                        AppNotification appNotification = new AppNotification(tempContactId, event.getEventId(), ".viewIndivEvent", "Event \"" + event.getEventTitle() + "\" has been updated. Click to view event.");
                                        AppNotificationDAO.addAppNotification(appNotification);
                                    }
                                }

                                json.addProperty("message", "success");
                                out.println(gson.toJson(json));
                            } else {
                                json.addProperty("message", "fail");
                                out.println(gson.toJson(json));
                            }

                            int participantNumber = event.getParticipantNumber();
                            event.setParticipantNumber(participantNumber - participantCount);
                            if (!EventDAO.updateEventDetails(event)) {
                                json.addProperty("message", "Fail to add update event table");
                                out.println(gson.toJson(json));
                            }

                        } else {
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
