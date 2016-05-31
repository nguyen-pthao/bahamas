/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
import bahamas.entity.Contact;
import bahamas.util.Authenticator;
import com.google.gson.*;
import java.io.IOException;
import java.io.PrintWriter;
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

            //Parse json object
            String jsonLine = request.getParameter("data");
            if (jsonLine == null || jsonLine.isEmpty()) {
                json.addProperty("message", "failed");
                out.println(gson.toJson(json));
            } else {
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String token = jobject.get("token").toString();
                String username = Authenticator.verifyToken(token);
                
                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));

                } else {
                    //Verified token

                    String name = jobject.get("name").toString();
                    String altName = jobject.get("altName").toString();
                    String contactType = jobject.get("contactType").toString();
                    String otherExplanation = jobject.get("otherExplanation").toString();
                    String profession = jobject.get("profession").toString();
                    String jobTitle = jobject.get("jobTitle").toString();
                    String nric = jobject.get("nric").toString();
                    String gender = jobject.get("gender").toString();
                    String nationality = jobject.get("nationality").toString();
                    String remarks = jobject.get("remarks").toString();

                    //Validation of fields
                    
                    //Create new contact object
                    Contact newContact = new Contact();
                    newContact.setName(name);
                    newContact.setAltName(altName);
                    newContact.setContactType(contactType);
                    newContact.setExplainIfOther(otherExplanation);
                    newContact.setProfession(profession);
                    newContact.setJobTitle(jobTitle);
                    newContact.setNric(nric);
                    newContact.setGender(gender);
                    newContact.setNationality(nationality);
                    newContact.setRemarks(remarks);
                    newContact.setDateCreated(new Date());
                    newContact.setCreatedBy(username);
                   
                    if (ContactDAO.addContact(newContact)) {
                        json.addProperty("message", "success");
                        out.println(gson.toJson(json));
                    } else {
                        json.addProperty("message", "failed");
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
