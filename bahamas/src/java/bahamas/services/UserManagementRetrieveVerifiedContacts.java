/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.UserManagementDAO;
import bahamas.entity.Contact;
import bahamas.entity.UserManagement;
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
@WebServlet(name = "UserManagementRetrieveVerifiedContacts", urlPatterns = {"/usermanagement.retrieveverifiedcontacts"})
public class UserManagementRetrieveVerifiedContacts extends HttpServlet {

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
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

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
                String authenticationCode = Validator.containsBlankField(jobject.get("authenticationcode"));
                Date userCreatedStartDate = Validator.isDateValid(jobject.get("user_create_startdate"), "user_create_date");
                Date userCreatedEndDate = Validator.isDateValid(jobject.get("user_create_enddate"), "user_create_enddate");
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

                if ((token == null || token.isEmpty())) {
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

                try {
                    if (userCreatedStartDate != null) {
                        userCreatedStartDate = date.parse(date.format(userCreatedStartDate));
                    }
                    if (userCreatedEndDate != null) {
                        userCreatedEndDate = date.parse(date.format(userCreatedEndDate));
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(UserManagementRetrieveUsers.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (contact != null && (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager") || RoleCheckDAO.checkRole(contact.getContactId(), "eventleader"))) {
                    ArrayList<UserManagement> userManagementList = UserManagementDAO.retrieveAllVerifiedContactsWithTeamPreference(userCreatedStartDate,userCreatedEndDate);
                    json.addProperty("message", "success");
                    JsonArray contactArray = new JsonArray();
                    JsonObject jsonContactObj;

                    for (UserManagement userManagement : userManagementList) {
                        Contact c = userManagement.getContact();
                        jsonContactObj = new JsonObject();
                        jsonContactObj.addProperty("cid", c.getContactId());
                        jsonContactObj.addProperty("user_date_created", sdf.format(c.getDateCreated()));
                        jsonContactObj.addProperty("code", "");
                        jsonContactObj.addProperty("name", c.getName());
                        jsonContactObj.addProperty("email", userManagement.getEmail());
                        String teamArray[] = userManagement.getTeamList();
                        
                        int count = 0;
                        for(int i = 0; i < teamArray.length; i++){
                            jsonContactObj.addProperty("team" + (i+1), teamArray[i]);
                            count++;
                        }
                        if(count == 0){
                            jsonContactObj.addProperty("team1", "");
                            jsonContactObj.addProperty("team2", "");
                            jsonContactObj.addProperty("team3", "");
                        } else if (count == 1){
                            jsonContactObj.addProperty("team2", "");
                            jsonContactObj.addProperty("team3", "");
                        } else if (count == 2){
                            jsonContactObj.addProperty("team3", "");
                        }

                        if (contact.isIsAdmin()) {
                            jsonContactObj.addProperty("cancheck", true);
                        } else {
                            jsonContactObj.addProperty("cancheck", false);
                        }
                        contactArray.add(jsonContactObj);
                    }
                    json.add("user", contactArray);
                    out.println(gson.toJson(json));
                } else {
                    json.addProperty("message", "fail to retrieve from system");
                    out.println(gson.toJson(json));
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
