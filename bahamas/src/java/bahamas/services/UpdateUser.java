package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Contact;
import bahamas.util.Authenticator;
import bahamas.util.PasswordHash;
import bahamas.util.Email;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HUXLEY
 */
@WebServlet(urlPatterns = {"/user.update"})
public class UpdateUser extends HttpServlet {

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

                    if (!user.isIsAdmin() && (c.getContactId() != user.getContactId())) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    if (jobject.get("current_password") != null) {
                        String currentPassword = Validator.containsBlankField(jobject.get("current_password").getAsString());
                        if (!PasswordHash.verify(currentPassword, c.getPassword(), c.getSalt())) {
                            json.addProperty("message", "current password failed");
                            out.println(gson.toJson(json));
                            return;
                        }
                    }

                    String password = null;
                    if (jobject.get("password") != null) {
                        password = Validator.containsBlankField(jobject.get("password").getAsString());
                        if (jobject.get("confirm_password") != null) {
                            String confirmPassword = Validator.containsBlankField(jobject.get("confirm_password").getAsString());
                            if (!password.equals(confirmPassword)) {
                                json.addProperty("message", "password mismatch");
                                out.println(gson.toJson(json));
                                return;
                            }
                        }
                        if (password != null) {
                            String[] passwordGenerate = PasswordHash.getHashAndSalt(password);
                            c.setPassword(passwordGenerate[0]);
                            c.setSalt(passwordGenerate[1]);
                        }
                    }

                    if (user.isIsAdmin()) {
                        String uName = Validator.containsBlankField(jobject.get("username").getAsString());
                        if (uName != null && cDAO.retrieveContactByUsername(uName) == null) {
                            c.setUsername(Validator.containsBlankField(jobject.get("username").getAsString()));
                        } else {
                            json.addProperty("message", "fail");
                            out.println(gson.toJson(json));
                            return;
                        }

                        c.setIsAdmin(Validator.isBooleanValid(jobject.get("is_admin").getAsString()));
                        if (Validator.isBooleanValid(jobject.get("is_admin").getAsString()) == true) {
                            cDAO.changeNovicePermission(c, false);
                        }
                        c.setDeactivated(Validator.isBooleanValid(jobject.get("deactivated").getAsString()));

                    }

                    if (ContactDAO.updateUser(c)) {
                        String email = Validator.containsBlankField(jobject.get("email").getAsString());
                        if (email != null && password != null) {
                            String[] temp = {c.getName(), c.getUsername(), password};
                            new Thread(() -> {
                                // Send Email in a separate thread
                                Email.sendEmail(email, temp);
                            }).start();
                        }

                        AuditLogDAO.insertAuditLog(username, "UPDATE CONTACT", "Update Contact under contact: Contact ID: " + contactId);
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
