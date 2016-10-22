package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bahamas.dao.ContactDAO;
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
@WebServlet(urlPatterns = {"/usermanagement.retrieveusers"})
public class UserManagementRetrieveUsers extends HttpServlet {

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

                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();
                String token = Validator.containsBlankField(jobject.get("token"));
                Date userCreatedStartDate = Validator.isDateValid(jobject.get("user_create_startdate"), "user_create_date");
                Date userLoginStartDate = Validator.isDateValid(jobject.get("user_login_startdate"), "user_login_date");
                Date userCreatedEndDate = Validator.isDateValid(jobject.get("user_create_enddate"), "user_create_enddate");
                Date userLoginEndDate = Validator.isDateValid(jobject.get("user_login_enddate"), "user_login_enddate");
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

                if (contact != null && (contact.isIsAdmin() || RoleCheckDAO.checkRole(contact.getContactId(), "teammanager"))) {

                    try {
                        if (userCreatedStartDate != null) {
                            userCreatedStartDate = date.parse(date.format(userCreatedStartDate));
                        }
                        if (userLoginStartDate != null) {
                            userLoginStartDate = date.parse(date.format(userLoginStartDate));
                        }
                        if (userCreatedEndDate != null) {
                            userCreatedEndDate = date.parse(date.format(userCreatedEndDate));
                        }
                        if (userLoginEndDate != null) {
                            userLoginEndDate = date.parse(date.format(userLoginEndDate));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(UserManagementRetrieveUsers.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ArrayList<Contact> contactList = contactDAO.retrieveAllContactWithEmailPhone(userCreatedStartDate, userCreatedEndDate, userLoginStartDate, userLoginEndDate);

                    json.addProperty("message", "success");

                    JsonArray userArray = new JsonArray();
                    JsonObject jsonContactObj;

                    for (Contact c : contactList) {

                        jsonContactObj = new JsonObject();
                        jsonContactObj.addProperty("cid", c.getContactId());
                        jsonContactObj.addProperty("name", c.getName());
                        if (c.getEmailStrList() != null && !c.getEmailStrList().isEmpty()) {
                            jsonContactObj.addProperty("email", c.getEmailStrList());
                        } else {
                            jsonContactObj.addProperty("email", "");
                        }
                        jsonContactObj.addProperty("username", c.getUsername());
                        jsonContactObj.addProperty("user_date_created", sdf.format(c.getDateCreated()));
                        if(c.getLastLogin() != null){
                            jsonContactObj.addProperty("last_login", sdf.format(c.getLastLogin()));
                        } else {
                            jsonContactObj.addProperty("last_login", "-");
                        }
                        jsonContactObj.addProperty("deactivated", c.isDeactivated());
                        if (contact.isIsAdmin()) {
                            jsonContactObj.addProperty("cancheck", true);
                        } else {
                            jsonContactObj.addProperty("cancheck", false);
                        }
                        userArray.add(jsonContactObj);
                    }
                    json.add("user", userArray);
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
