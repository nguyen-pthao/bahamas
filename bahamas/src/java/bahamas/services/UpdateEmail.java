/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.PhoneDAO;
import bahamas.entity.*;
import bahamas.util.Authenticator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
 * @author huxley.goh
 */
@WebServlet(name = "UpdateEmail", urlPatterns = {"/email.update"})
public class UpdateEmail extends HttpServlet {

     private static final SimpleDateFormat date = new java.text.SimpleDateFormat("dd-MMM-yyyy");
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

                String token = jobject.get("token").getAsString();
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));

                } else {
                    //Verified token
                    int contactId = Integer.parseInt(jobject.get("id").getAsString());
                    ContactDAO cDAO = new ContactDAO();

                    Contact c = cDAO.retrieveContactById(contactId);

                    //Verification for add additional phone details (OWNSELF)
                    if (c == null || !c.getUsername().equals(username)) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {
                        String email = jobject.get("email").getAsString();
                        String emailRemarks = jobject.get("emailremarks").getAsString();

                         Date dateObsolete = null;
                        try {
                            dateObsolete = date.parse(jobject.get("dateobsolete").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();                                             
                        }

                        Email newEmail = new Email(c, email, username, emailRemarks, dateObsolete);

                        if (EmailDAO.addEmail(newEmail)) {
                            json.addProperty("message", "success");
                            out.println(gson.toJson(json));
                        } else {
                            json.addProperty("message", "fail");
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
