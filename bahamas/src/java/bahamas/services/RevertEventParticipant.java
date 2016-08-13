/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EventAffiliationDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.EventRoleAssignmentDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.Contact;
import bahamas.entity.EventAffiliation;
import bahamas.entity.EventParticipant;
import bahamas.entity.EventRoleAssignment;
import bahamas.entity.TeamJoin;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
@WebServlet(name = "RevertEventParticipant", urlPatterns = {"/event.revertrole"})
public class RevertEventParticipant extends HttpServlet {

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
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

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
                String roleId = Validator.containsBlankField(jobject.get("role_id"));
                String participantId = Validator.containsBlankField(jobject.get("contact_id"));
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
                        if(eventId == null || roleId == null || participantId == null){
                            json.addProperty("message", "error");
                            if(eventId == null){
                                jsonErrorMsgArray.add(new JsonPrimitive("Missing event ID"));
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
                        boolean hasRole = false;
                        String roleName = "";
                        ArrayList<EventParticipant> eventParticipantList = EventParticipantDAO.retrieveEventParticipantbyEventID(Integer.parseInt(eventId));
                        Iterator iter = eventParticipantList.iterator();
                        while(iter.hasNext()){
                            EventParticipant eventParticipant = (EventParticipant)iter.next();
                            if(!eventParticipant.isPullout() && eventParticipant.getRoleID() == Integer.parseInt(roleId) && eventParticipant.getContactID() == Integer.parseInt(participantId)){
                                int tempRoleId = eventParticipant.getRoleID();
                                EventRoleAssignment eventRoleAssignment = EventRoleAssignmentDAO.retrieveEventRoleByRoleId(tempRoleId);
                                roleName = eventRoleAssignment.getRoleName();
                                hasRole = true;
                            }
                        }
                        
                        if(hasRole){
                            jsonErrorMsgArray.add(new JsonPrimitive("Participant is already participating as " + roleName));
                            json.add("errorMsg", jsonErrorMsgArray);
                            out.println(gson.toJson(json));
                            return;
                        }
                        
                        HashMap<String, String> hmTeamPermission = new HashMap<String, String>();

                        //Get all teams this user has
                        ArrayList<TeamJoin> teamJoinList = TeamJoinDAO.retrieveAllTeamJoinCID(Integer.parseInt(participantId));
                        try {
                            for (TeamJoin teamJoin : teamJoinList) {
                                //hmTeamPermission.put(teamJoin.getTeamName(), teamJoin.getPermission());
                                Date currentDateTime = new Date();
                                Date currentDate = date.parse(date.format(currentDateTime));
                                Date obsDate = null;

                                if (teamJoin.getDateObsolete() != null) {
                                    obsDate = date.parse(date.format(teamJoin.getDateObsolete()));
                                }

                                if (obsDate == null || obsDate.equals(currentDate) || obsDate.after(currentDate)) {
                                    hmTeamPermission.put(teamJoin.getTeamName(), teamJoin.getPermission());
                                }
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(RetrieveUpcomingParticipants.class.getName()).log(Level.SEVERE, null, ex);
                            json.addProperty("message", "fail");
                            out.println(gson.toJson(json));
                            return;
                        }
                        
                        if(!contact.isIsAdmin() && !RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") && !Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId))){
                            json.addProperty("message", "No permission to revert role");
                            out.println(gson.toJson(json));
                            return;
                        }
                        
                        EventAffiliation eventAffiliation = EventAffiliationDAO.retrieveAllEventAffiliation(Integer.parseInt(eventId));
                        ArrayList<String> teamNameList = eventAffiliation.getTeamArray();
                        Iterator iterTeamNameList = teamNameList.iterator();
                        boolean teamInEvent = false;
                        while(iterTeamNameList.hasNext()){
                            String teamName = (String)iterTeamNameList.next();
                            if(hmTeamPermission.containsKey(teamName)){
                                teamInEvent = true;
                                break;
                            }
                        }
                        
                        if(!teamInEvent){
                            
                            jsonErrorMsgArray.add(new JsonPrimitive("Unable to revert role as user's team is no longer in this event."));
                            json.add("errorMsg", jsonErrorMsgArray);
                            out.println(gson.toJson(json));
                            return;
                        }
                        
                        EventParticipant eventParticipant = new EventParticipant(Integer.parseInt(participantId), null, Integer.parseInt(roleId), Integer.parseInt(eventId), username, false, null, null, 0, null, null);
                        if(EventParticipantDAO.updateEventRole(eventParticipant)){
                            AuditLogDAO.insertAuditLog(username, "Revert role", "Revert role in event under contact: Contact ID: " + contact.getContactId() + " | Event ID: " + eventId + " | Event role ID: " + roleId);
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
