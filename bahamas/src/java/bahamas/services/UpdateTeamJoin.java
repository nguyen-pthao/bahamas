/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.dao.list.PermissionLevelListDAO;
import bahamas.entity.Contact;
import bahamas.entity.Email;
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
import is203.JWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
 * @author huxley.goh
 */
@WebServlet(name = "UpdateTeamJoin", urlPatterns = {"/teamjoin.update"})
public class UpdateTeamJoin extends HttpServlet {

    private static final SimpleDateFormat date = new java.text.SimpleDateFormat("dd-MMM-yyyy");

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
            throws ServletException, IOException, JWTException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

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
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));

                } else {
                    //Verified token
                    int contactId = Validator.isIntValid(jobject.get("contact_id"));
                    ContactDAO cDAO = new ContactDAO();

                    Contact c = cDAO.retrieveContactById(contactId);

                    if (c == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {

                        Contact user = cDAO.retrieveContactByUsername(username);
                        String userType = Validator.containsBlankField(jobject.get("user_type"));
                        if (!user.isIsAdmin() && (!userType.equals("teammanager")
                                && !RoleCheckDAO.checkRole(user.getContactId(), userType)) && (!userType.equals("eventleader")
                                && !RoleCheckDAO.checkRole(user.getContactId(), userType)) && !c.getUsername().equals(username)) {
                            json.addProperty("message", "fail");
                            out.println(gson.toJson(json));
                            return;
                        }

                        String team = Validator.containsBlankField(jobject.get("team"));
                        String explainIfOther = Validator.containsBlankField(jobject.get("explain_if_other"));
                        String subTeam = Validator.containsBlankField(jobject.get("subteam"));
                        String permission = Validator.containsBlankField(jobject.get("permission_level"));

                        PermissionLevelListDAO permissionList = new PermissionLevelListDAO();
                        if (permission != null) {
                            if (!permissionList.retrievePermissionLevelList().contains(permission)) {
                                Validator.getErrorList().add("Permission not referencing to Permission List");
                            }

                            if (userType.equals("eventleader") && permission.equals("Team manager")) {
                                Validator.getErrorList().add("Event leader permission cannot assign a team manager permission");
                            }
                        }

                        String remarks = Validator.containsBlankField(jobject.get("remarks"));
                        Date dateObsolete = Validator.isDateValid(jobject.get("date_obsolete"), "date obsolete");

                        TeamJoin tj = new TeamJoin(c, team, username, explainIfOther,
                                subTeam, dateObsolete, remarks, permission);

                        if (!Validator.getErrorList().isEmpty()) {
                            JsonArray errorArray = new JsonArray();
                            for (String s : Validator.getErrorList()) {
                                JsonPrimitive o = new JsonPrimitive(s);
                                errorArray.add(o);
                            }
                            Validator.getErrorList().clear();
                            json.add("message", errorArray);
                            out.println(gson.toJson(json));
                            return;
                        }

                        if (TeamJoinDAO.updateTeamJoin(tj)) {
                            //change contact to a non novice account
                            if (permission != null) {
                                ContactDAO.changeNovicePermission(c, false);
                                AuditLogDAO.insertAuditLog(username, "UPDATE TEAM JOIN", "Update team join under contact: Contact ID: " + contactId);
                                json.addProperty("message", "success");
                                out.println(gson.toJson(json));
                                return;
                            }

                        } else {
                            json.addProperty("message", "failure to update into system");
                            out.println(gson.toJson(json));
                        }

                        json.addProperty("message", "failure to update into system");
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
        try {
            processRequest(request, response);
        } catch (JWTException ex) {
            Logger.getLogger(UpdateTeamJoin.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UpdateTeamJoin.class.getName()).log(Level.SEVERE, null, ex);
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
