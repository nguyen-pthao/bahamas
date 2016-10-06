package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.EventAffiliationDAO;
import bahamas.dao.EventDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.EventRoleAssignmentDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.Contact;
import bahamas.entity.Email;
import bahamas.entity.Event;
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
@WebServlet(urlPatterns = {"/event.retrieveindiv"})
public class RetrieveEventIndiv extends HttpServlet {

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
        
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        
        try (PrintWriter out = response.getWriter()) {
            JsonObject json = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonArray eventRoleJsonArray = new JsonArray();
            //JsonArray roleParticipentArray = new JsonArray();
            JsonArray withdrawnParticipentArray = new JsonArray();
            JsonArray eventAffiliationJsonArray = new JsonArray();
            JsonObject teamJson = new JsonObject();
            boolean canView = false;
            boolean canViewRSC = false;
            HashMap<String, String> emailHM = new HashMap<String, String>();
            String email = "";

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
                String eventId = Validator.containsBlankField(jobject.get("eventId"));
                String username = Authenticator.verifyToken(token);
                SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat date2 = new SimpleDateFormat("dd-MMM-yyyy");
                boolean canSeeUsername = false;
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
                        HashMap<String, String> hmTeamPermission = new HashMap<String, String>();

                        //Get all teams this user has
                        ArrayList<TeamJoin> teamJoinList = TeamJoinDAO.retrieveAllTeamJoinCID(contact.getContactId());
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

                        EventDAO eventDAO = new EventDAO();
                        Event event = eventDAO.retrieveEventById(Integer.parseInt(eventId));
                        //SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

                        if (event != null) {
                            json.addProperty("message", "success");

                            json.addProperty("event_id", eventId);
                            json.addProperty("event_title", event.getEventTitle());
                            json.addProperty("event_start_date", date2.format(event.getEventStartDate()));
                            json.addProperty("event_end_date", date2.format(event.getEventEndDate()));
                            json.addProperty("event_time_start", datetime.format(event.getEventStartTime()));
                            json.addProperty("event_time_end", datetime.format(event.getEventEndTime()));
                            json.addProperty("view_time_start", time.format(event.getEventStartTime()));
                            json.addProperty("view_time_end", time.format(event.getEventEndTime()));
                            json.addProperty("send_reminder", Boolean.toString(event.isSendReminder()));
                            json.addProperty("event_description", event.getEventDescription());
                            json.addProperty("minimum_participation", Integer.toString(event.getMinimumParticipation()));
                            json.addProperty("event_class", event.getEventClassName());
                            json.addProperty("event_location", event.getEventLocationName());
                            json.addProperty("address", event.getAddress());
                            json.addProperty("zipcode", event.getZipcode());
                            json.addProperty("event_status", event.getEventStatus());
                            json.addProperty("event_lat", event.getEventLat());
                            json.addProperty("event_lng", event.getEventLng());
                            json.addProperty("date_created", date.format(event.getDateCreated()));
                            json.addProperty("remarks", event.getRemarks());
                            ArrayList<EventRoleAssignment> eventRoleAssignmentList = EventRoleAssignmentDAO.retrieveEventRoleById(event.getEventId());
                            EventAffiliation eventAffiliation = EventAffiliationDAO.retrieveAllEventAffiliation(Integer.parseInt(eventId));
                            ArrayList<EventParticipant> EventParticipantList = EventParticipantDAO.retrieveEventParticipantbyEventID(Integer.parseInt(eventId));
                            ArrayList<EventRoleAssignment> EventRoleAssignmentList = EventRoleAssignmentDAO.retrieveEventRoleById(Integer.parseInt(eventId));

                            boolean canJoinDisable = false;

                            if (eventRoleAssignmentList != null && eventRoleAssignmentList.size() != 0) {

                                for (EventRoleAssignment eventRoleAssignment : eventRoleAssignmentList) {
                                    EventParticipant eventParticipant = EventParticipantDAO.retrieveParticipantbyRoleIDContactID(eventRoleAssignment.getRoleId(), contact.getContactId());
                                    JsonObject roleJson = new JsonObject();
                                    roleJson.addProperty("event_role_id", eventRoleAssignment.getRoleId());
                                    roleJson.addProperty("event_role", eventRoleAssignment.getRoleName());
                                    roleJson.addProperty("event_desc", eventRoleAssignment.getRoleDescription());
                                    if (eventParticipant == null) {
                                        roleJson.addProperty("joined", false);
                                    } else if (eventParticipant.isPullout()) {
                                        roleJson.addProperty("joined", false);
                                    } else {
                                        roleJson.addProperty("joined", true);
                                        canJoinDisable = true;
                                    }
                                    JsonArray roleParticipentArray = new JsonArray();
                                    if (EventRoleAssignmentList != null && EventParticipantList != null) {

                                        int roleId = eventRoleAssignment.getRoleId();
                                        //JsonArray participantsWithSameRole = new JsonArray();
                                        for (EventParticipant eventParticipantTemp : EventParticipantList) {
                                            JsonObject role = new JsonObject();
                                            if (roleId == eventParticipantTemp.getRoleID()) {
                                                if (!eventParticipantTemp.isPullout()) {
                                                    int participantID = eventParticipantTemp.getContactID();
                                                    Contact contactTemp = cDAO.retrieveContactById(participantID);
                                                    ArrayList<Email> emailList = EmailDAO.retrieveAllEmail(contactTemp);
                                                    role.addProperty("role", eventRoleAssignment.getRoleName());
                                                    role.addProperty("role_id", eventRoleAssignment.getRoleId());
                                                    //role.addProperty("participant_name", (contactTemp.getName() + "(" + contactTemp.getUsername() + ")"));
                                                    role.addProperty("contact_id", contactTemp.getContactId());
                                                    //boolean chk2 = Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId));
                                                    if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId)) || contactTemp.getContactId() == contact.getContactId()) {
                                                        //boolean chk1 = RoleCheckDAO.checkRole(contact.getContactId(), "teammanager");
                                                        String tempUsername = "No username";
                                                        if (contactTemp.getUsername() != null) {
                                                            tempUsername = contactTemp.getUsername();
                                                        }
                                                        if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || contactTemp.getContactId() == contact.getContactId()) {
                                                            role.addProperty("participant_name", (contactTemp.getName() + "(" + tempUsername + ")"));
                                                            role.addProperty("canRemark", true);
                                                        } else {
                                                            role.addProperty("participant_name", (contactTemp.getName()));
                                                            role.addProperty("canRemark", false);
                                                        }
                                                        if(contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || event.getContactId() == contact.getContactId()){
                                                            role.addProperty("canRemove", true);
                                                        } else {
                                                            role.addProperty("canRemove", false);
                                                        }
                                                        role.addProperty("remarks", eventParticipantTemp.getRemarks());
                                                        if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId))) {
                                                            role.addProperty("canAppreciate", true);
                                                            //role.addProperty("canAddParticipants", true);
                                                            role.addProperty("eventParticipantservice_comment", eventParticipantTemp.getService_comment());
                                                        } else {
                                                            role.addProperty("canAppreciate", false);
                                                            //role.addProperty("canAddParticipants", false);
                                                            role.add("eventParticipantservice_comment", new JsonPrimitive(""));
                                                        }
                                                        role.addProperty("award_hours", Double.toString(eventParticipantTemp.getHoursServed()));
                                                    } else {
                                                        role.addProperty("participant_name", (contactTemp.getName()));
                                                        role.addProperty("canRemove", false);
                                                        role.addProperty("remarks", eventParticipantTemp.getRemarks());
                                                        role.addProperty("canAppreciate", false);
                                                        //role.addProperty("canAddParticipants", false);
                                                        role.add("eventParticipantservice_comment", new JsonPrimitive(""));
                                                        role.addProperty("award_hours", "");
                                                    }
                                                    /*
                                                    if (contactTemp.getContactId() == contact.getContactId()) {
                                                        role.addProperty("canRemark", true);
                                                    } else {
                                                        role.addProperty("canRemark", false);
                                                    }
                                                    */
                                                    roleParticipentArray.add(role);
                                                    //roleJson.add("event_participant", roleParticipentArray);

                                                    if (!emailList.isEmpty()) {

                                                        try {
                                                            Date todayDate = new Date();
                                                            Date todayDateWithoutTime = sdf.parse(sdf.format(todayDate));
                                                            ArrayList<String> emailDisplayList = new ArrayList<String>();
                                                            for (int i = 0; i < emailList.size(); i++) {
                                                                Email emailObj = emailList.get(i);
                                                                Date ObsDateWithoutTime = null;
                                                                if (emailObj.getDateObsolete() != null) {
                                                                    ObsDateWithoutTime = sdf.parse(sdf.format(emailObj.getDateObsolete()));
                                                                }
                                                                if (emailObj.getDateObsolete() != null && !ObsDateWithoutTime.equals(todayDateWithoutTime) && !ObsDateWithoutTime.before(todayDateWithoutTime)) {
                                                                    emailDisplayList.add(emailObj.getEmail());
                                                                } else if (emailObj.getDateObsolete() == null) {
                                                                    emailDisplayList.add(emailObj.getEmail());
                                                                }
                                                            }
                                                            if (!emailDisplayList.isEmpty()) {
                                                                for (int i = 0; i < emailDisplayList.size() - 1; i++) {
                                                                    
                                                                    emailHM.put(contactTemp.getName() + "<" + emailDisplayList.get(i) + ">;", contactTemp.getName() + "<" + emailDisplayList.get(i) + ">;");
                                                                }
                                                                
                                                                emailHM.put(contactTemp.getName() + "<" + emailDisplayList.get(emailDisplayList.size() - 1) + ">;", contactTemp.getName() + "<" + emailDisplayList.get(emailDisplayList.size() - 1) + ">;");
                                                            }

                                                        } catch (ParseException ex) {
                                                            Logger.getLogger(RetrieveContact.class.getName()).log(Level.SEVERE, null, ex);
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }

                                    roleJson.add("event_participant", roleParticipentArray);

//new                                    
                                    eventRoleJsonArray.add(roleJson);
                                }
                            }
                            if (eventAffiliation != null) {
                                for (String team : eventAffiliation.getTeamArray()) {
                                    //JsonElement jsonElement = parser.parse(team);
                                    eventAffiliationJsonArray.add(new JsonPrimitive(team));
                                }
                                teamJson.add("teams_affiliated", eventAffiliationJsonArray);
                                teamJson.addProperty("explain_if_other", eventAffiliation.getExplainIfOthers());
                                //teamJson.addProperty("remarks", eventAffiliation.getRemarks());
                            }
                            json.add("event_role", eventRoleJsonArray);
                            json.add("event_team_affiliation", teamJson);
                            //json.add("event_participant", roleParticipentArray);

                            if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId))) {
                                if (EventRoleAssignmentList != null && EventParticipantList != null) {
                                    for (EventRoleAssignment eventRoleAssignment : EventRoleAssignmentList) {
                                        int roleId = eventRoleAssignment.getRoleId();
                                        //JsonArray participantsWithSameRole = new JsonArray();
                                        for (EventParticipant eventParticipant : EventParticipantList) {
                                            JsonObject role = new JsonObject();
                                            if (roleId == eventParticipant.getRoleID()) {
                                                if (eventParticipant.isPullout()) {
                                                    int participantID = eventParticipant.getContactID();
                                                    Contact contactTemp = cDAO.retrieveContactById(participantID);
                                                    role.addProperty("role", eventRoleAssignment.getRoleName());
                                                    role.addProperty("role_id", eventRoleAssignment.getRoleId());
                                                    String tempUsername = "No username";
                                                    if (contactTemp.getUsername() != null) {
                                                        tempUsername = contactTemp.getUsername();
                                                    }
                                                    role.addProperty("participant_name", (contactTemp.getName() + "(" + tempUsername + ")"));
                                                    role.addProperty("contact_id", contactTemp.getContactId());
                                                    role.addProperty("reason", eventParticipant.getReason());
                                                    withdrawnParticipentArray.add(role);
                                                }
                                            }
                                        }
                                    }
                                }
                                json.add("withdrawn_participants", withdrawnParticipentArray);
                                canView = true;
                                canViewRSC = true;
                            } else {
                                json.addProperty("withdrawn_participants", "");
                            }

                            //if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || event.getCreatedBy().equals(contact.getUsername())) {
                            if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId))) {
                                //check of the user has joined this event
                                //EventParticipantDAO.retrieveParticipantbyEventIDRoleID(,eventId);
                                json.addProperty("canEdit", true);
                                json.addProperty("canDelete", true);
                                if (canJoinDisable) {
                                    json.addProperty("canJoin", false);
                                } else {
                                    json.addProperty("canJoin", true);
                                }
                                json.addProperty("canAddParticipants", true);

                            } else {
                                if (eventAffiliation != null) {
                                    ArrayList<String> teamsInEvent = eventAffiliation.getTeamArray();
                                    if (teamsInEvent != null && !teamsInEvent.isEmpty()) {
                                        boolean marked = false;
                                        for (String eventTeam : teamsInEvent) {
                                            Boolean matchTeam = hmTeamPermission.containsKey(eventTeam);
                                            if (matchTeam && !marked) {
                                                String permision = hmTeamPermission.get(eventTeam);
                                                if (permision == null && event.getEventClassName().trim().toLowerCase().equals("basic training")) {
                                                    json.addProperty("canEdit", false);
                                                    json.addProperty("canDelete", false);
                                                    json.addProperty("canJoin", true);
                                                    marked = true;
                                                } else if (permision != null && permision.equals("Event leader")) {
                                                    json.addProperty("canEdit", true);
                                                    json.addProperty("canDelete", true);
                                                    if (canJoinDisable) {
                                                        json.addProperty("canJoin", false);
                                                    } else {
                                                        json.addProperty("canJoin", true);
                                                    }
                                                    marked = true;
                                                } else if (permision != null && permision.equals("Associate")) {
                                                    json.addProperty("canEdit", false);
                                                    json.addProperty("canDelete", false);
                                                    if (canJoinDisable) {
                                                        json.addProperty("canJoin", false);
                                                    } else {
                                                        json.addProperty("canJoin", true);
                                                    }
                                                    marked = true;
                                                }
                                            }
                                        }
                                        if (!marked) {
                                            json.addProperty("canEdit", false);
                                            json.addProperty("canDelete", false);
                                            json.addProperty("canJoin", false);
                                        }
                                    } else {
                                        json.addProperty("canEdit", false);
                                        json.addProperty("canDelete", false);
                                        json.addProperty("canJoin", false);
                                    }
                                } else {
                                    json.addProperty("canEdit", false);
                                    json.addProperty("canDelete", false);
                                    json.addProperty("canJoin", false);
                                }
                                json.addProperty("canAddParticipants", false);
                            }
                            json.addProperty("canView", canView);
                            json.addProperty("canViewRSC", canViewRSC);
                            json.addProperty("createdBy", event.getCreatedBy());
                            if (event.getReminderEmail() == null || event.getReminderEmail().isEmpty()) {
                                json.addProperty("reminder_email", "");
                            } else {
                                json.addProperty("reminder_email", event.getReminderEmail());
                            }
                            if (event.getContactId() == contact.getContactId()) {
                                json.addProperty("isCreator", true);
                            } else {
                                json.addProperty("isCreator", false);
                            }
                            Iterator iter = emailHM.keySet().iterator();
                            while (iter.hasNext()) {
                                email += " " + (String) iter.next();
                            }
                            json.addProperty("emailList", email);
                            out.println(gson.toJson(json));
                        } else {
                            json.addProperty("message", "Fail retrieve event");
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
