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
import bahamas.util.EmailGenerator;
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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final String SAVE_DIR = "images";

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
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));

                } else {
                    ContactDAO cDAO = new ContactDAO();
                    Contact user = cDAO.retrieveContactByUsername(username);

                    int contactId = Validator.isIntValid(jobject.get("contact_id"));
                    Contact c = cDAO.retrieveContactById(contactId);

                    if (c == null || user == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    if (!user.isIsAdmin() && (c.getContactId() != user.getContactId())) {
                        json.addProperty("message", "not admin");
                        out.println(gson.toJson(json));
                        return;
                    }

                    if (jobject.get("current_password") != null) {
                        String currentPassword = Validator.containsBlankField(jobject.get("current_password"));
                        if (!PasswordHash.verify(currentPassword, c.getPassword(), c.getSalt())) {
                            json.addProperty("message", "incorrect current password");
                            out.println(gson.toJson(json));
                            return;
                        }
                    }

                    String password = null;
                    if (jobject.get("password") != null) {
                        password = Validator.containsBlankField(jobject.get("password"));
                        if (jobject.get("confirm_password") != null) {
                            String confirmPassword = Validator.containsBlankField(jobject.get("confirm_password"));
                            if (password != null) {
                                if (!password.equals(confirmPassword)) {
                                    json.addProperty("message", "password mismatch");
                                    out.println(gson.toJson(json));
                                    return;
                                }
                            }
                        }
                        if (password != null) {
                            String[] passwordGenerate = PasswordHash.getHashAndSalt(password);
                            c.setPassword(passwordGenerate[0]);
                            c.setSalt(passwordGenerate[1]);
                        }
                    }

                    if (user.isIsAdmin()) {
                        if (jobject.get("username") != null) {
                            String uName = Validator.containsBlankField(jobject.get("username"));
                            if (uName != null && cDAO.retrieveContactByUsername(uName) == null) {
                                c.setUsername(Validator.containsBlankField(jobject.get("username")));
                            } else {
                                json.addProperty("message", "username already exist");
                                out.println(gson.toJson(json));
                                return;
                            }

                        }

                        if (jobject.get("is_admin") != null) {
                            c.setIsAdmin(Validator.isBooleanValid(jobject.get("is_admin")));
                            if (Validator.isBooleanValid(jobject.get("is_admin")) == true) {
                                cDAO.changeNovicePermission(c, false);
                            }
                        }

                        if (jobject.get("deactivated") != null) {
                            c.setDeactivated(Validator.isBooleanValid(jobject.get("deactivated")));
                        }

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

                    if (ContactDAO.updateUser(c)) {
                        if (jobject.get("email") != null && password != null) {
                            String email = Validator.containsBlankField(jobject.get("email"));
                            String[] temp = {c.getName(), c.getUsername(), password};

                            if (c.getUsername() != null && !c.getUsername().isEmpty()) {
                                String appPath = request.getServletContext().getRealPath("");
                                String savePath = appPath + File.separator + SAVE_DIR;

                                Files.copy(new File(savePath + File.separator + "default" + ".jpg").toPath(),
                                        new File(savePath + File.separator + c.getUsername() + ".jpg").toPath(), REPLACE_EXISTING);
                            }

                            new Thread(() -> {
                                // Send EmailGenerator in a separate thread
                                EmailGenerator.sendEmail(email, temp);
                            }).start();
                        }

                        AuditLogDAO.insertAuditLog(username, "UPDATE CONTACT", "Update Contact under contact: Contact ID: " + contactId);
                        json.addProperty("message", "success");
                        out.println(gson.toJson(json));

                    } else {
                        json.addProperty("message", "failure to update into system");
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
        try {
            processRequest(request, response);
        } catch (JWTException ex) {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
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
