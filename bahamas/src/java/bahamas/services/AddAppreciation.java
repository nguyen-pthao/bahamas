/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AppreciationDAO;
import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Appreciation;
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
import is203.JWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author HUXLEY
 */
@WebServlet(name = "AddAppreciation", urlPatterns = {"/appreciation.add"})
public class AddAppreciation extends HttpServlet {

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
                    return;
                } else {
                    //Verified token
                    int contactId = Validator.isIntValid(jobject.get("contact_id"));
                    ContactDAO cDAO = new ContactDAO();

                    Contact user = cDAO.retrieveContactByUsername(username);
                    String userType = Validator.containsBlankField(jobject.get("user_type"));
                    if (!user.isIsAdmin() && (!userType.equals("teammanager")
                            && !RoleCheckDAO.checkRole(user.getContactId(), userType))) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    Contact c = cDAO.retrieveContactById(contactId);

                    if (c == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {

                        String appraisalComment = Validator.containsBlankField(jobject.get("appraisal_comment"));
                        String appraisalBy = Validator.containsBlankField(jobject.get("appraisal_by"));
                        Date appraisalDate = Validator.isDateValid(jobject.get("appraisal_date"), "appraisal date");
                        String appreciationGesture = Validator.containsBlankField(jobject.get("appreciation_gesture"));
                        String appreciationBy = Validator.containsBlankField(jobject.get("appreciation_by"));
                        Date appreciationDate = Validator.isDateValid(jobject.get("appreciation_date"), "appreciation date");
                        String appreciationRemarks = Validator.containsBlankField(jobject.get("remarks"));

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

                        Appreciation appreciation = new Appreciation(c, appraisalComment, appraisalBy, appraisalDate, appreciationGesture,
                                appreciationBy, appreciationDate, appreciationRemarks, username);

                        if (AppreciationDAO.addAppreciation(appreciation)) {
                            AuditLogDAO.insertAuditLog(username, "ADD APPRECIATION", "Add appreciation under contact: Contact ID: " + c.getContactId());
                            json.addProperty("message", "success");
                            out.println(gson.toJson(json));
                        } else {
                            json.addProperty("message", "failure insert into system");
                            out.println(gson.toJson(json));
                        }

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
            Logger.getLogger(AddAppreciation.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddAppreciation.class.getName()).log(Level.SEVERE, null, ex);
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
