package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bahamas.dao.ContactDAO;
import bahamas.dao.EventAffiliationDAO;
import bahamas.dao.EventDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.Contact;
import bahamas.entity.Event;
import bahamas.entity.EventAffiliation;
import bahamas.entity.EventParticipant;
import bahamas.entity.TeamJoin;
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
                String teamNameFilter = "";
                String username = Authenticator.verifyToken(token);
                if (jobject.has("teamFilter")) {
                    teamNameFilter = jobject.get("teamFilter").getAsString();
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
                        return;
                    } else {
                        HashMap<String, String> hmTeamPermission = new HashMap<String, String>();
                        //Get all teams this user has
                        ArrayList<TeamJoin> teamJoinList = TeamJoinDAO.retrieveAllTeamJoinCID(contact.getContactId());
                        for (TeamJoin teamJoin : teamJoinList) {
                            hmTeamPermission.put(teamJoin.getTeamName(), teamJoin.getPermission());
                        }

                        EventDAO eventDAO = new EventDAO();
                        ArrayList<Event> eventList = eventDAO.retrieveAllEvents();
                        SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
                        JsonArray eventArray = new JsonArray();
                        JsonObject jsonContactObj;
                        if (eventList != null) {
                            json.addProperty("message", "success");
                            for (int i = 0; i < eventList.size(); i++) {
                                Event event = eventList.get(i);
                                Date currentDateTime = new Date();
                                try {
                                    Date currentDate = date.parse(date.format(currentDateTime));
                                    Date currentTime = time.parse(time.format(currentDateTime));
                                    Date eventEndDate = date.parse(date.format(event.getEventEndDate()));
                                    Date eventEndTime = time.parse(time.format(event.getEventEndTime()));
                                    if (eventEndDate.after(currentDate) || (eventEndDate.equals(currentDate) && eventEndTime.after(currentTime))) {

                                        //view for novcie
                                        if (contact.isIsNovice()) {
                                            if (event.getEventClassName().trim().toLowerCase().equals("basic training")) {

                                                EventAffiliation eventAffiliation = EventAffiliationDAO.retrieveAllEventAffiliation(event.getEventId());
                                                //get the team here
                                                boolean isPendingTeam = false;

                                                jsonContactObj = new JsonObject();
                                                if (eventAffiliation != null) {
                                                    String teamTemp = "";
                                                    ArrayList<String> teamnameList = eventAffiliation.getTeamArray();
                                                    for (int m = 0; m < teamnameList.size() - 1; m++) {
                                                        teamTemp += teamnameList.get(m) + " | ";
                                                        if (hmTeamPermission.containsKey(teamnameList.get(m))) {
                                                            isPendingTeam = true;
                                                        }
                                                    }
                                                    teamTemp += teamnameList.get(teamnameList.size() - 1);
                                                    if (hmTeamPermission.containsKey(teamnameList.get(teamnameList.size() - 1))) {
                                                        isPendingTeam = true;
                                                    }
                                                    jsonContactObj.addProperty("team", teamTemp);
                                                } else {
                                                    jsonContactObj.addProperty("team", "");
                                                }
                                                jsonContactObj.addProperty("event_id", event.getEventId());
                                                jsonContactObj.addProperty("event_title", event.getEventTitle());
                                                jsonContactObj.addProperty("event_start_date", date.format(event.getEventStartDate()));
                                                jsonContactObj.addProperty("event_end_date", date.format(event.getEventEndDate()));
                                                jsonContactObj.addProperty("event_time_start", time.format(event.getEventStartTime()));
                                                jsonContactObj.addProperty("event_class", event.getEventClassName());
                                                jsonContactObj.addProperty("event_location", event.getEventLocationName());
                                                jsonContactObj.addProperty("event_status", event.getEventStatus());
                                                jsonContactObj.addProperty("canEdit", false);
                                                jsonContactObj.addProperty("canDelete", false);
                                                jsonContactObj.addProperty("canJoin", true);
                                                if (isPendingTeam) {
                                                    eventArray.add(jsonContactObj);
                                                }
                                            }
                                        } else {
                                            //view for all other users
                                            jsonContactObj = new JsonObject();
                                            jsonContactObj.addProperty("event_id", event.getEventId());
                                            jsonContactObj.addProperty("event_title", event.getEventTitle());
                                            jsonContactObj.addProperty("event_start_date", date.format(event.getEventStartDate()));
                                            jsonContactObj.addProperty("event_end_date", date.format(event.getEventEndDate()));
                                            jsonContactObj.addProperty("event_time_start", time.format(event.getEventStartTime()));
                                            EventAffiliation eventAffiliation = EventAffiliationDAO.retrieveAllEventAffiliation(event.getEventId());
                                            HashMap<String, String> eventTeamsHM = new HashMap<String, String>();
                                            if (eventAffiliation != null) {
                                                String teamTemp = "";
                                                ArrayList<String> teamnameList = eventAffiliation.getTeamArray();
                                                for (int m = 0; m < teamnameList.size() - 1; m++) {
                                                    teamTemp += teamnameList.get(m) + " | ";
                                                    eventTeamsHM.put(teamnameList.get(m), teamnameList.get(m));
                                                }
                                                teamTemp += teamnameList.get(teamnameList.size() - 1);
                                                eventTeamsHM.put(teamnameList.get(teamnameList.size() - 1), teamnameList.get(teamnameList.size() - 1));
                                                jsonContactObj.addProperty("team", teamTemp);
                                            } else {
                                                jsonContactObj.addProperty("team", "");
                                            }
                                            jsonContactObj.addProperty("event_class", event.getEventClassName());
                                            jsonContactObj.addProperty("event_location", event.getEventLocationName());
                                            jsonContactObj.addProperty("event_status", event.getEventStatus());
                                            if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || event.getCreatedBy().equals(contact.getUsername())) {
                                                jsonContactObj.addProperty("canEdit", true);
                                                jsonContactObj.addProperty("canDelete", true);
                                                jsonContactObj.addProperty("canJoin", true);
                                            } else {
                                                if (eventAffiliation != null) {
                                                    ArrayList<String> teamsInEvent = eventAffiliation.getTeamArray();
                                                    if (teamsInEvent != null && !teamsInEvent.isEmpty()) {
                                                        boolean marked = false;
                                                        for (String eventTeam : teamsInEvent) {
                                                            Boolean matchTeam = hmTeamPermission.containsKey(eventTeam);
                                                            if (matchTeam && !marked) {
                                                                String permision = hmTeamPermission.get(eventTeam);
                                                                if (permision.equals("Event leader")) {
                                                                    jsonContactObj.addProperty("canEdit", true);
                                                                    jsonContactObj.addProperty("canDelete", true);
                                                                    jsonContactObj.addProperty("canJoin", true);
                                                                    marked = true;
                                                                } else if (permision.equals("Associate")) {
                                                                    jsonContactObj.addProperty("canEdit", false);
                                                                    jsonContactObj.addProperty("canDelete", false);
                                                                    jsonContactObj.addProperty("canJoin", true);
                                                                    marked = true;
                                                                }
                                                            }
                                                        }
                                                        if (!marked) {
                                                            jsonContactObj.addProperty("canEdit", false);
                                                            jsonContactObj.addProperty("canDelete", false);
                                                            jsonContactObj.addProperty("canJoin", false);
                                                        }
                                                    } else {
                                                        jsonContactObj.addProperty("canEdit", false);
                                                        jsonContactObj.addProperty("canDelete", false);
                                                        jsonContactObj.addProperty("canJoin", false);
                                                    }
                                                } else {
                                                    jsonContactObj.addProperty("canEdit", false);
                                                    jsonContactObj.addProperty("canDelete", false);
                                                    jsonContactObj.addProperty("canJoin", false);
                                                }
                                            }
                                            ArrayList<EventParticipant> eventParticipantList = EventParticipantDAO.retrieveEventParticipantbyEventID(event.getEventId());
                                            int partNumber = 0;
                                            for(EventParticipant eventParticipant : eventParticipantList){
                                                if(!eventParticipant.isPullout()){
                                                    partNumber++;
                                                }
                                            }
                                            if (eventParticipantList == null) {
                                                jsonContactObj.addProperty("totalParticipant", partNumber);
                                            } else {
                                                jsonContactObj.addProperty("totalParticipant", partNumber);
                                            }
                                            jsonContactObj.addProperty("createdBy", event.getCreatedBy());
                                            if (teamNameFilter.isEmpty()) {
                                                eventArray.add(jsonContactObj);
                                            } else if (!teamNameFilter.isEmpty() && teamNameFilter.equals("my_team")) {
                                                Iterator iter = eventTeamsHM.keySet().iterator();
                                                while (iter.hasNext()) {
                                                    String eventTeam = (String) iter.next();
                                                    Boolean matchTeam = hmTeamPermission.containsKey(eventTeam);

                                                    if (matchTeam) {
                                                        eventArray.add(jsonContactObj);
                                                        //hmTeamPermission.clear();
                                                    }
                                                }
                                            } else if (!teamNameFilter.isEmpty() && eventTeamsHM.containsKey(teamNameFilter)) {
                                                eventArray.add(jsonContactObj);
                                                hmTeamPermission.clear();
                                            }
                                        }
                                    }
                                } catch (ParseException ex) {
                                    Logger.getLogger(RetrieveAllUpcomingEvents.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            json.add("event", eventArray);
                            out.println(gson.toJson(json));
                        } else {
                            json.addProperty("message", "Fail retrieve all upcoming events");
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
