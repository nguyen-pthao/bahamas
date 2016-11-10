/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.RegistrationDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Contact;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HUXLEY
 */
@WebServlet(name = "AddRemoteRegistration", urlPatterns = {"/remoteregistration.add"})
public class AddRemoteRegistration extends HttpServlet {

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

                int formId = Validator.isIntValid(jobject.get("form_id"));
                int contactId = Validator.isIntValid(jobject.get("contact_id"));
                String name = Validator.containsBlankField(jobject.get("name"));
                String profession = Validator.containsBlankField(jobject.get("profession"));
                String jobTitle = Validator.containsBlankField(jobject.get("job_title"));
                String nric = Validator.containsBlankField(jobject.get("nric_fin"));
                String gender = Validator.containsBlankField(jobject.get("gender"));
                String nationality = Validator.containsBlankField(jobject.get("nationality"));
                Date dob = Validator.isBirthdayValid(jobject.get("date_of_birth"), "date of birth");
                String remarks = Validator.containsBlankField(jobject.get("remarks"));
                String language = Validator.containsBlankField(jobject.get("language"));
                String speakWrite = Validator.containsBlankField(jobject.get("speak_write"));
                String email = Validator.containsBlankField(jobject.get("email"));

                if (email != null) {
                    if (EmailDAO.emailExist(email)) {
                        Validator.getErrorList().add("Email already exists");
                    } else if (!Validator.validEmail(email)) {
                        Validator.getErrorList().add("Invalid email format");
                    }
                }

                int countryCode = Validator.isIntValid(jobject.get("country_code"));
                String phoneNumber = Validator.containsBlankField(jobject.get("phone_number"));
                String team = Validator.containsBlankField(jobject.get("team"));
                String skillsAsset = Validator.containsBlankField(jobject.get("skill_asset"));

                boolean first = Validator.isBooleanValid(jobject.get("first"));
                boolean second = Validator.isBooleanValid(jobject.get("second"));
                boolean requestUser = Validator.isBooleanValid(jobject.get("request_user"));

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

                if (formId == 0) {
                    json.addProperty("message", "invalid authentication code");
                    out.println(gson.toJson(json));
                    return;
                }

                if (RegistrationDAO.addRegistration(formId, contactId, name, nric,
                        nationality, dob, gender, profession, jobTitle, remarks,
                        countryCode, phoneNumber, email, language, speakWrite, skillsAsset,
                        team, first, second, requestUser)) {

                    json.addProperty("message", "success");
                    out.println(gson.toJson(json));

                } else {
                    json.addProperty("message", "failure insert into system");
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
