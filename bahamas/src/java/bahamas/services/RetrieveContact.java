/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
import bahamas.entity.Contact;
import bahamas.util.Authenticator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@WebServlet(name = "RetrieveContact", urlPatterns = {"/contact.retrieve"})
public class RetrieveContact extends HttpServlet {

    private static final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
   
    
     /*
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


            /*          
            String token = request.getParameter("token");
            if (!Authenticator.verifyToken(token)) {
                json.addProperty("message", "invalid token");
                out.println(gson.toJson(json));

            } else {
                //Verified
             */
            //Validation
            //Create new contact object
            
            /*
            username  = token
            boolean t = checkRole(cid,teamid,p)
            RoleCheckDAO
            */
            ContactDAO contactDAO = new ContactDAO();
            ArrayList<Contact> contactList = contactDAO.retrieveAllContact();

            if (!contactList.isEmpty()) {
                json.addProperty("message", "success");

                JsonArray contactArray = new JsonArray();
                JsonObject jsonContactObj;

                for (Contact c : contactList) {

                    jsonContactObj = new JsonObject();
                    jsonContactObj.addProperty("username", c.getUsername());
                    //jsonContactObj.addProperty("password", c.getPassword());

                    jsonContactObj.addProperty("contactType", c.getContactType());
                    jsonContactObj.addProperty("dateCreated", sdf.format(c.getDateCreated()));
                    jsonContactObj.addProperty("createdBy", c.getCreatedBy());
                    jsonContactObj.addProperty("name", c.getName());
                    jsonContactObj.addProperty("altName", c.getAltName());
                    jsonContactObj.addProperty("explainIfOther", c.getExplainIfOther());
                    jsonContactObj.addProperty("profession", c.getProfession());
                    jsonContactObj.addProperty("jobTitle", c.getJobTitle());
                    jsonContactObj.addProperty("nric", c.getNric());
                    jsonContactObj.addProperty("gender", c.getGender());
                    jsonContactObj.addProperty("nationality", c.getNationality());
                    jsonContactObj.addProperty("dateOfBirth", sdf.format(c.getDateOfBirth()));
                    jsonContactObj.addProperty("profilePic", c.getProfilePic());
                    jsonContactObj.addProperty("remarks", c.getRemarks());
                    contactArray.add(jsonContactObj);

                }
                json.add("contact", contactArray);
                out.println(gson.toJson(json));
               
            } else {
                json.addProperty("message", "failed");
                out.println(gson.toJson(json));
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
