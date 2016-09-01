/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.EventParticipantDAO;
import bahamas.dao.EventRoleAssignmentDAO;
import bahamas.entity.EventParticipant;
import bahamas.entity.EventRoleAssignment;
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
import java.text.SimpleDateFormat;
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
@WebServlet(name = "ExportParticipant", urlPatterns = {"/export.participant"})
public class ExportParticipant extends HttpServlet {

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
                json.addProperty("message", "json empty");
                out.println(gson.toJson(json));
                
            } else {
                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();
                String token = Validator.containsBlankField(jobject.get("token"));
                //String cidString = jobject.get("cid").getAsString();
                //Optional for admin and novice
                //String teamName = jobject.get("teamname").getAsString();
                //String permission = jobject.get("permission").getAsString();

                if ((token == null || token.isEmpty())) {
                    json.addProperty("message", "no token");
                    out.println(gson.toJson(json));
                    return;
                }

                String username = Authenticator.verifyToken(token);
                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));
                    return;
                }
                
                EventParticipantDAO eventParticipantDAO = new EventParticipantDAO();
                ArrayList<EventParticipant> aList = eventParticipantDAO.retrieveEventParticipants();

                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat sdft = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                JsonArray eventParticipantArray = new JsonArray();

                //start
                if (aList != null && !aList.isEmpty()) {

                    for (int i = 0; i < aList.size(); i++) {
                        JsonObject jsonEventParticipantObj = new JsonObject();
                        EventParticipant eventParticipant = aList.get(i);

                        if (eventParticipant.getEventID()> 0) {
                            jsonEventParticipantObj.addProperty("eid", eventParticipant.getEventID());
                        } else {
                            jsonEventParticipantObj.addProperty("eid", "");
                        }
                        if (eventParticipant.getContactID()> 0) {
                            jsonEventParticipantObj.addProperty("cid", eventParticipant.getContactID());
                        } else {
                            jsonEventParticipantObj.addProperty("cid", "");
                        }
                        if (eventParticipant.getAwarderID()!= null ) {
                            jsonEventParticipantObj.addProperty("awarder", eventParticipant.getAwarderID());
                        } else {
                            jsonEventParticipantObj.addProperty("awarder", "");
                        }
                        EventRoleAssignmentDAO eventRoleAssignmentDAO = new EventRoleAssignmentDAO();
                        
                        if (eventParticipant.getRoleID()> 0) {
                            jsonEventParticipantObj.addProperty("role_name", eventRoleAssignmentDAO.retrieveEventRoleByRoleId(eventParticipant.getRoleID()).getRoleName());
                        } else {
                            jsonEventParticipantObj.addProperty("role_name", "");
                        }
                        if (eventParticipant.getRoleID()> 0) {
                            jsonEventParticipantObj.addProperty("role_description", eventRoleAssignmentDAO.retrieveEventRoleByRoleId(eventParticipant.getRoleID()).getRoleDescription());
                        } else {
                            jsonEventParticipantObj.addProperty("role_description", "");
                        }
                        
                        if (eventParticipant.isPullout()) {
                            jsonEventParticipantObj.addProperty("pullout", eventParticipant.isPullout());
                        } else {
                            jsonEventParticipantObj.addProperty("pullout", false);
                        }
                        if (eventParticipant.getReason() != null) {
                            jsonEventParticipantObj.addProperty("reason", eventParticipant.getReason());
                        } else {
                            jsonEventParticipantObj.addProperty("reason", "");
                        }
                        if (eventParticipant.getDatepullout()!= null) {
                            jsonEventParticipantObj.addProperty("date_pullout", sdf.format(eventParticipant.getDatepullout()));
                        } else {
                            jsonEventParticipantObj.addProperty("date_pullout", "");
                        }
                        if (eventParticipant.getHoursServed() > 0) {
                            jsonEventParticipantObj.addProperty("hours_served", eventParticipant.getHoursServed());
                        } else {
                            jsonEventParticipantObj.addProperty("hours_served", "");
                        }
                        if (eventParticipant.getService_comment()!= null) {
                            jsonEventParticipantObj.addProperty("service_comment", eventParticipant.getService_comment());
                        } else {
                            jsonEventParticipantObj.addProperty("service_comment", "");
                        }
                        if (eventParticipant.getRemarks() != null) {
                            jsonEventParticipantObj.addProperty("remarks", eventParticipant.getRemarks());
                        } else {
                            jsonEventParticipantObj.addProperty("remarks", "");
                        }
                        
                        jsonEventParticipantObj.addProperty("created_by", eventParticipant.getCreatedBy());
                        jsonEventParticipantObj.addProperty("date_created", sdft.format(eventParticipant.getDateCreated()));
                        eventParticipantArray.add(jsonEventParticipantObj);

                    }
                    json.addProperty("message", "success");
                    json.add("list", eventParticipantArray);
                    AuditLogDAO.insertAuditLog(username, "EXPORT", "Exported eventParticipant table");
                } else {

                    json.addProperty("message", "eventParticipant table is empty");
                    out.println(gson.toJson(json));
                    return;
                }

            }

            out.println(gson.toJson(json));
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
