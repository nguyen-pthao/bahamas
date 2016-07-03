/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.*;
import bahamas.entity.*;
import bahamas.util.Authenticator;
import bahamas.util.PasswordHash;
import bahamas.util.Validator;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateContact", urlPatterns = {"/contact.update"})
public class UpdateContact extends HttpServlet {

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

                String token = jobject.get("token").getAsString();
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));

                } else {
                    ContactDAO cDAO = new ContactDAO();
                    Contact user = cDAO.retrieveContactByUsername(username);

                    int contactId = Validator.isIntValid(jobject.get("contact_id").getAsString());
                    Contact c = cDAO.retrieveContactById(contactId);

                    if (c == null || user == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    String userType = Validator.containsBlankField(jobject.get("user_type").getAsString());
                    if (!user.isIsAdmin() && (!userType.equals("teammanager")
                            && !RoleCheckDAO.checkRole(user.getContactId(), userType)) && (!userType.equals("eventleader")
                            && !RoleCheckDAO.checkRole(user.getContactId(), userType)) && !c.getUsername().equals(username)) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    c.setName(Validator.containsBlankField(jobject.get("name").getAsString()));
                    c.setAltName(Validator.containsBlankField(jobject.get("alt_name").getAsString()));
                    c.setContactType(Validator.containsBlankField(jobject.get("contact_type").getAsString()));
                    c.setExplainIfOther(Validator.containsBlankField(jobject.get("explain_if_other").getAsString()));
                    c.setProfession(Validator.containsBlankField(jobject.get("profession").getAsString()));
                    c.setJobTitle(Validator.containsBlankField(jobject.get("job_title").getAsString()));
                    c.setNric(Validator.containsBlankField(jobject.get("nric_fin").getAsString()));
                    c.setGender(Validator.containsBlankField(jobject.get("gender").getAsString()));
                    c.setNationality(Validator.containsBlankField(jobject.get("nationality").getAsString()));
                    c.setDateOfBirth(Validator.isDateValid(jobject.get("date_of_birth").getAsString()));
                    c.setRemarks(Validator.containsBlankField(jobject.get("remarks").getAsString()));

                    if (ContactDAO.updateContact(c)) {
                        AuditLogDAO.insertAuditLog(username, "UPDATE CONTACT", "Update contact under contact: Contact ID: " + contactId);
                        json.addProperty("message", "success");
                        out.println(gson.toJson(json));
                    } else {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                    }

                }
            }

            //}
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
