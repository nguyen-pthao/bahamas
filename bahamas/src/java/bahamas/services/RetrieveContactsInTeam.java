/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.EventAffiliationDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.EventRoleAssignmentDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.Contact;
import bahamas.entity.Email;
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
import is203.JWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
 * @author SI HAO =D
 */
@WebServlet(name = "RetrieveContactsInTeam", urlPatterns = {"/event.retrievecontactsinteam"})
public class RetrieveContactsInTeam extends HttpServlet {

    private static final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");

    /*
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JWTException {
        response.setContentType("application/JSON;charset=UTF-8");
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

                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();
                String token = Validator.containsBlankField(jobject.get("token"));
                String roleId = Validator.containsBlankField(jobject.get("role_id"));
                String eventId = Validator.containsBlankField(jobject.get("event_id"));

                if (token == null || token.isEmpty() || roleId == null || eventId == null) {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                    return;
                }

                String username = Authenticator.verifyToken(token);
                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                    return;
                }

                ContactDAO contactDAO = new ContactDAO();
                Contact contact = contactDAO.retrieveContactByUsername(username);

                //ContactDAO contactDAO = new ContactDAO();
                ArrayList<Contact> contactList = null;

                //if (!contactList.isEmpty() && !contact.isIsNovice()) {
                if (!contact.isIsNovice()) {

                    json.addProperty("message", "success");

                    if (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || Validator.validEventLeaderPosition(contact.getContactId(), Integer.parseInt(eventId))) {
                        boolean containsTeamOther = false;

                        EventAffiliation eventAffiliation = EventAffiliationDAO.retrieveAllEventAffiliation(Integer.parseInt(eventId));
                        ArrayList<String> teamListInEvent = null;
                        if (eventAffiliation != null) {
                            teamListInEvent = eventAffiliation.getTeamArray();
                        }

                        for (String tempTeam : teamListInEvent) {
                            if (tempTeam.equalsIgnoreCase("other")) {
                                containsTeamOther = true;
                                break;
                            }
                        }
                        HashMap<Integer, Contact> contactidContactHM = new HashMap<Integer, Contact>();
                        ArrayList<Contact> contactListWithEmailPhone = contactDAO.retrieveAllContactWithEmailPhone();
                        for(Contact contactTemp:contactListWithEmailPhone){
                            contactidContactHM.put(contactTemp.getContactId(), contactTemp);
                        }
                        if (containsTeamOther) {
                            contactList = contactDAO.retrieveAllContact();
                        } else {
                            contactList = contactDAO.retrieveAllContactInTeams(teamListInEvent);
                        }

                        JsonArray contactArray = retrieveAll(contactList, eventId, teamListInEvent, contactidContactHM);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    }

                } else {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                    return;
                }
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));
            }

        }
    }

    private static JsonArray retrieveAll(ArrayList<Contact> contactList, String eventId, ArrayList<String> teamListInEvent, HashMap<Integer, Contact> contactidContactHM) {

        JsonArray contactArray = new JsonArray();
        JsonObject jsonContactObj;
        ArrayList<EventRoleAssignment> eventRoleAssignmentList = EventRoleAssignmentDAO.retrieveEventRoleById(Integer.parseInt(eventId));


        for (Contact c : contactList) {


            boolean hasJoinedRole = false;
 
            if (eventRoleAssignmentList != null) {
                Iterator iter3 = eventRoleAssignmentList.iterator();
                while (iter3.hasNext()) {
                    EventRoleAssignment eventRoleAssignment = (EventRoleAssignment) iter3.next();
                    EventParticipant eventParticipant = EventParticipantDAO.retrieveParticipantbyRoleIDContactID(eventRoleAssignment.getRoleId(), c.getContactId());
                    if (eventParticipant != null && !eventParticipant.isPullout()) {
                        hasJoinedRole = true;
                        break;
                    }
                }
            }
            //} else {
            //    return contactArray;
            //}
            //if ((containsTeamOther || teamMatch) && !hasJoinedRole && teamListInEvent != null) {
            if (!hasJoinedRole && teamListInEvent != null) {

                String emailStr = "";
                String name = c.getName();
                
                if (name == null) {
                    name = "";
                }
 
                
                Contact tempC = contactidContactHM.get(c.getContactId());
                if (tempC.getEmailStrList() != null) {
                    emailStr = tempC.getEmailStrList();
                }

                jsonContactObj = new JsonObject();
                jsonContactObj.addProperty("name", name);
                jsonContactObj.addProperty("email", emailStr);
                jsonContactObj.addProperty("cid", c.getContactId());
                contactArray.add(jsonContactObj);
            }
        }
        return contactArray;
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
        try {
            processRequest(request, response);
        } catch (JWTException ex) {
            Logger.getLogger(RetrieveContactsInTeam.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (JWTException ex) {
            Logger.getLogger(RetrieveContactsInTeam.class.getName()).log(Level.SEVERE, null, ex);
        }
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
