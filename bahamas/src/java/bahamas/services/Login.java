/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.entity.AuditLog;
import bahamas.entity.Contact;
import bahamas.util.Authenticator;
import bahamas.util.PasswordHash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            /* TODO output your page here. You may use following sample code. */

            JsonObject json = new JsonObject();
            JsonArray errorArray = new JsonArray();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            JsonPrimitive invalid = new JsonPrimitive("invalid username/password");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            errorArray.add(invalid);

            if (username == null || password == null) {
                json.addProperty("status", "error");
                //json.addProperty("messages", "invalid username/password");
                json.add("messages", errorArray);
                out.println(gson.toJson(json));
                return;
            }

            ContactDAO contactDAO = new ContactDAO();
            Contact contact = contactDAO.retrieveContactByUsername(username);

            if (contact != null) {
                //String serverPassword = PasswordHash.hashPassword(contact.getPassword());
                String serverPassword = contact.getPassword();
                if (serverPassword.equals(password) && !contact.isDeactivated()) {
                    
                    AuditLogDAO.insertAuditLog(username, "LOGIN", "Login into system");

                    String token = Authenticator.signedToken(username);
                    json.addProperty("status", "success");
                    json.addProperty("token", token);
                    JsonObject jsonContactObj = new JsonObject();
                    if (contact.isIsAdmin()) {
                        json.addProperty("userType", "admin");
                    } else {
                        json.addProperty("userType", "I will update you guys later");
                    }

                    jsonContactObj.addProperty("contactType", contact.getContactType());
                    jsonContactObj.addProperty("dateCreated", formatter.format(contact.getDateCreated()));
                    jsonContactObj.addProperty("createdBy", contact.getCreatedBy());
                    jsonContactObj.addProperty("name", contact.getName());
                    jsonContactObj.addProperty("altName", contact.getAltName());
                    jsonContactObj.addProperty("explainIfOther", contact.getExplainIfOther());
                    jsonContactObj.addProperty("profession", contact.getProfession());
                    jsonContactObj.addProperty("jobTitle", contact.getJobTitle());
                    jsonContactObj.addProperty("nric", contact.getNric());
                    jsonContactObj.addProperty("gender", contact.getGender());
                    jsonContactObj.addProperty("nationality", contact.getNationality());
                    jsonContactObj.addProperty("dateOfBirth", formatter.format(contact.getDateOfBirth()));
                    jsonContactObj.addProperty("profilePic", contact.getProfilePic());
                    jsonContactObj.addProperty("remarks", contact.getRemarks());
                    json.add("contact", jsonContactObj);

                    out.println(gson.toJson(json));
                    return;
                }
            }
            json.addProperty("status", "error");
            //json.addProperty("messages", "invalid username/password");
            json.add("messages", errorArray);
            out.print(gson.toJson(json));
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
