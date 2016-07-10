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

/**
 *
 * @author HUXLEY
 */
@WebServlet(name = "AddContact", urlPatterns = {"/contact.add"})
public class AddContact extends HttpServlet {

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

                String token = Validator.containsBlankField(jobject.get("token"));
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));

                } else {

                    ContactDAO cDAO = new ContactDAO();
                    Contact user = cDAO.retrieveContactByUsername(username);
                    String userType = Validator.containsBlankField(jobject.get("user_type"));
                    if (!user.isIsAdmin() && !userType.equals("teammanager") && !RoleCheckDAO.checkRole(user.getContactId(), userType)) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    String name = Validator.containsBlankField(jobject.get("name"));
                    String altName = Validator.containsBlankField(jobject.get("alt_name"));
                    String contactType = Validator.containsBlankField(jobject.get("contact_type"));
                    String otherExplanation = Validator.containsBlankField(jobject.get("explain_if_other"));
                    String profession = Validator.containsBlankField(jobject.get("profession"));
                    String jobTitle = Validator.containsBlankField(jobject.get("job_title"));
                    String nric = Validator.containsBlankField(jobject.get("nric_fin"));
                    String gender = Validator.containsBlankField(jobject.get("gender"));
                    String nationality = Validator.containsBlankField(jobject.get("nationality"));
                    Date dob = Validator.isDateValid(jobject.get("date_of_birth"), "date of birth");
                    String remarks = Validator.containsBlankField(jobject.get("remarks"));

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

                    //Create new contact object
                    Contact newContact = new Contact(contactType, username, name, altName, otherExplanation, profession,
                            jobTitle, nric, gender, nationality, dob, remarks);

                    int newContactId = ContactDAO.addContact(newContact);

                    if (newContactId <= 0) {
                        json.addProperty("message", "failure insert into system");
                        out.println(gson.toJson(json));
                        return;
                    } else {
                        AuditLogDAO.insertAuditLog(username, "CONTACT", "Created contact: Contact ID: " + newContactId + " | Name: " + name);
                        json.addProperty("message", "success");
                        json.addProperty("contact_id", String.valueOf(newContactId));
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
