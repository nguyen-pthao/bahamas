/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.ContactDAO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Darryl Mok
 */
@WebServlet(name = "ExportContact", urlPatterns = {"/export.contact"})
public class ExportContact extends HttpServlet {

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
                json.addProperty("message", "json empty");
                out.println(gson.toJson(json));
                
            } else {
                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();
                String token = Validator.containsBlankField(jobject.get("token"));
                //String cidString = jobject.get("cid").getAsString();
                //Optional for admin and novice
                //String teamName = jobject.get("teamname").getAsString();
                //String permission = jobject.get("permission").getAsString();

                if ((token == null || token.isEmpty())) {
                    json.addProperty("message", "no token");
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
                ArrayList<Contact> aList = contactDAO.retrieveAllContact();

                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat sdft = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                JsonArray contactArray = new JsonArray();

                //start
                if (aList != null && !aList.isEmpty()) {

                    for (int i = 0; i < aList.size(); i++) {
                        JsonObject jsonContactObj = new JsonObject();
                        Contact contact = aList.get(i);

                        if (contact.getContactId() > 0) {
                            jsonContactObj.addProperty("cid", contact.getContactId());
                        } else {
                            jsonContactObj.addProperty("cid", "");
                        }
                        if (contact.getName() != null ) {
                            jsonContactObj.addProperty("name", contact.getName());
                        } else {
                            jsonContactObj.addProperty("name", "");
                        }
                        if (contact.getAltName() != null ) {
                            jsonContactObj.addProperty("alt_name", contact.getAltName());
                        } else {
                            jsonContactObj.addProperty("alt_name", "");
                        }
                        if (contact.getUsername() != null ) {
                            jsonContactObj.addProperty("username", contact.getUsername());
                        } else {
                            jsonContactObj.addProperty("username", "");
                        }
                        
                        if (contact.getDateOfBirth() != null) {
                            jsonContactObj.addProperty("date_of_birth", sdf.format(contact.getDateOfBirth()));
                        } else {
                            jsonContactObj.addProperty("date_of_birth", "");
                        }
                        if (contact.getNric() != null ) {
                            jsonContactObj.addProperty("nric", contact.getNric());
                        } else {
                            jsonContactObj.addProperty("nric", "");
                        }
                        if (contact.getGender() != null ) {
                            jsonContactObj.addProperty("gender", contact.getGender());
                        } else {
                            jsonContactObj.addProperty("gender", "");
                        }
                        if (contact.getNationality() != null ) {
                            jsonContactObj.addProperty("nationality", contact.getNationality());
                        } else {
                            jsonContactObj.addProperty("nationality", "");
                        }
                        
                        if (contact.getContactType() != null) {
                            jsonContactObj.addProperty("contact_type", contact.getContactType());
                        } else {
                            jsonContactObj.addProperty("contact_type", "");
                        }
                        if (contact.getExplainIfOther() != null) {
                            jsonContactObj.addProperty("explain_if_other", contact.getExplainIfOther());
                        } else {
                            jsonContactObj.addProperty("explain_if_other", "");
                        }
                        if (contact.getProfession() != null) {
                            jsonContactObj.addProperty("profession", contact.getProfession());
                        } else {
                            jsonContactObj.addProperty("profession", "");
                        }
                        if (contact.getJobTitle() != null) {
                            jsonContactObj.addProperty("job_title", contact.getJobTitle());
                        } else {
                            jsonContactObj.addProperty("job_title", "");
                        }

                        if (contact.getRemarks() != null) {
                            jsonContactObj.addProperty("remarks", contact.getRemarks());
                        } else {
                            jsonContactObj.addProperty("remarks", "");
                        }
                        
                        if (contact.isIsAdmin()) {
                            jsonContactObj.addProperty("isadmin", contact.isIsAdmin());
                        } else {
                            jsonContactObj.addProperty("isadmin", false);
                        }
                        
                        if (contact.isIsNovice()) {
                            jsonContactObj.addProperty("isnovice", contact.isIsNovice());
                        } else {
                            jsonContactObj.addProperty("isnovice", false);
                        }
                        if (contact.isNotification()) {
                            jsonContactObj.addProperty("isnotification", contact.isNotification());
                        } else {
                            jsonContactObj.addProperty("isnotification", false);
                        }
                        
                        jsonContactObj.addProperty("created_by", contact.getCreatedBy());
                        jsonContactObj.addProperty("date_created", sdft.format(contact.getDateCreated()));
                        contactArray.add(jsonContactObj);

                    }
                    json.addProperty("message", "success");
                    json.add("contactlist", contactArray);
                } else {

                    json.addProperty("message", "list empty");
                    out.println(gson.toJson(json));
                    return;
                }

            }

            out.println(gson.toJson(json));
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
