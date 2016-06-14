/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.AuditLog;
import bahamas.entity.Contact;
import bahamas.entity.TeamJoin;
import bahamas.util.Authenticator;
import bahamas.util.PasswordHash;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HUXLEY
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));

            } else {

                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String username = jobject.get("username").getAsString();
                String password = jobject.get("password").getAsString();
                ArrayList<TeamJoin> teamJoinList;

                if (username == null || password == null) {                  
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                    return;
                } else {
                    //Verified username and password not null

                    ContactDAO contactDAO = new ContactDAO();
                    Contact contact = contactDAO.retrieveContactByUsername(username);

                    if (contact != null) {

                        teamJoinList = TeamJoinDAO.retrieveAllTeamJoin(username);
                        contact.setTeamJoinList(teamJoinList);
                      
                        if (PasswordHash.verify(password, contact.getPassword(), contact.getSalt()) 
                                && !contact.isDeactivated()) {

                            AuditLogDAO.insertAuditLog(username, "LOGIN", "Login into system");

                            String token = Authenticator.signedToken(username);
                            json.addProperty("message", "success");
                            json.addProperty("token", token);
                            JsonObject jsonContactObj = new JsonObject();
                            JsonArray jsonTeamObjList = new JsonArray();
                            //JsonObject jsonTeamObj = new JsonObject();

                            if (contact.isIsNovice()) {
                                json.addProperty("usertype", "novice");
                            } else if (contact.isIsAdmin()) {
                                json.addProperty("usertype", "admin");
                            } else if (RoleCheckDAO.checkRole(contact.getContactId(), "Team manager")) {
                                json.addProperty("usertype", "teammanager");
                            } else if (RoleCheckDAO.checkRole(contact.getContactId(), "Event leader")) {
                                json.addProperty("usertype", "eventleader");
                            } else if (RoleCheckDAO.checkRole(contact.getContactId(), "Associate")) {
                                json.addProperty("usertype", "associate");
                            }
                            jsonContactObj.addProperty("cid", contact.getContactId());
                            jsonContactObj.addProperty("contacttype", contact.getContactType());
                            jsonContactObj.addProperty("datecreated", datetime.format(contact.getDateCreated()));
                            jsonContactObj.addProperty("createdby", contact.getCreatedBy());
                            jsonContactObj.addProperty("name", contact.getName());
                            jsonContactObj.addProperty("altname", contact.getAltName());
                            jsonContactObj.addProperty("explainifother", contact.getExplainIfOther());
                            jsonContactObj.addProperty("profession", contact.getProfession());
                            jsonContactObj.addProperty("jobtitle", contact.getJobTitle());
                            jsonContactObj.addProperty("nric", contact.getNric());
                            jsonContactObj.addProperty("gender", contact.getGender());
                            jsonContactObj.addProperty("nationality", contact.getNationality());
                            jsonContactObj.addProperty("dateofbirth", date.format(contact.getDateOfBirth()));
                            jsonContactObj.addProperty("profilepic", contact.getProfilePic());
                            jsonContactObj.addProperty("remarks", contact.getRemarks());
                            //json.add("contact", jsonContactObj);

                            if (teamJoinList != null && !teamJoinList.isEmpty()) {
                                Iterator iter = teamJoinList.iterator();
                                while (iter.hasNext()) {
                                    JsonObject jsonTeamObj = new JsonObject();
                                    TeamJoin teamjoin = (TeamJoin) iter.next();
                                    jsonTeamObj.addProperty("teamname", teamjoin.getTeamName());
                                    jsonTeamObj.addProperty("permission", teamjoin.getPermission());
                                    jsonTeamObj.addProperty("subteam", teamjoin.getSubTeam());
                                    jsonTeamObj.addProperty("explainifothers", teamjoin.getExplainIfOthers());
                                    jsonTeamObj.addProperty("createdby", teamjoin.getCreatedBy());
                                    jsonTeamObj.addProperty("datecreated", datetime.format(teamjoin.getDateCreated()));
                                    jsonTeamObj.addProperty("remarks", teamjoin.getRemarks());
                                    if (teamjoin.getDateObsolete() != null) {
                                        jsonTeamObj.addProperty("dateobsolete", date.format(teamjoin.getDateObsolete()));
                                    }
                                    jsonTeamObjList.add(jsonTeamObj);
                                    jsonContactObj.add("teams", jsonTeamObjList);
                                }
                                //json.add("teams", jsonTeamObjList);

                            }

                            json.add("contact", jsonContactObj);
                            out.println(gson.toJson(json));
                            return;
                        }
                    }

                }
                json.addProperty("message", "fail");              
                out.print(gson.toJson(json));
            }

            // Uncomment ABOVE for login taking JSON object
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
