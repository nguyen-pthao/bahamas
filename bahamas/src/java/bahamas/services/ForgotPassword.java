/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AddressDAO;
import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.entity.Contact;
import bahamas.entity.Email;
import bahamas.util.Authenticator;
import bahamas.util.EmailGenerator;
import bahamas.util.PasswordHash;
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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author huxley.goh
 */
@WebServlet(name = "ForgotPassword", urlPatterns = {"/password.forgot"})
public class ForgotPassword extends HttpServlet {

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

                //String username = Validator.containsBlankField(jobject.get("username"));
                String email = Validator.containsBlankField(jobject.get("email"));
                /*
                if (username == null) {
                    Validator.getErrorList().add("invalid username");
                }
                */
                if (email == null || !Validator.validEmail(email)) {
                    Validator.getErrorList().add("invalid email");
                }

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

                Email e = EmailDAO.retrieveEmail(email);
                ContactDAO cDAO = new ContactDAO();
                int contactId = EmailDAO.retrieveContactId(email);
                Contact c = cDAO.retrieveContactById(contactId);

                if (e != null && e.getVerified() && c != null) {
                    if (e.getContact().getContactId() == c.getContactId()) {
                        String newPassword = PasswordHash.generateRandomString(8);
                        String[] temp = {c.getName(), c.getUsername(), newPassword};

                        String[] passwordGenerate = PasswordHash.getHashAndSalt(newPassword);
                        c.setPassword(passwordGenerate[0]);
                        c.setSalt(passwordGenerate[1]);

                        if (ContactDAO.updateUser(c)) {
                            new Thread(() -> {
                                // Send EmailGenerator in a separate thread
                                EmailGenerator.sendEmail(e.getEmail(), temp);
                            }).start();

                            AuditLogDAO.insertAuditLog(c.getUsername(), "UPDATE CONTACT", "Update Contact forgot password under contact: Contact ID: " + c.getContactId());

                            json.addProperty("message", "success");
                            out.println(gson.toJson(json));
                            return;
                        } else {
                            json.addProperty("message", "failure to update into system");
                            out.println(gson.toJson(json));
                            return;
                        }

                    }
                } else {
                    json.addProperty("message", "invalid forgot password request");
                    out.println(gson.toJson(json));
                    return;
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
