/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.*;
import bahamas.entity.*;
import bahamas.util.Authenticator;
import com.google.gson.*;
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
 * @author HUXLEY
 */
@WebServlet(name = "AddContact", urlPatterns = {"/contact.add"})
public class AddContact extends HttpServlet {

    private static final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");

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
                json.addProperty("message", "failed");
                out.println(gson.toJson(json));
                return;
            }

            String jsonLine = sb.toString();
            if (jsonLine == null || jsonLine.isEmpty()) {
                json.addProperty("message", "failed");
                out.println(gson.toJson(json));

            } else {
                //Parse json object
                JsonElement jelement = new JsonParser().parse(jsonLine);
                JsonObject jobject = jelement.getAsJsonObject();

                String token = jobject.get("token").getAsString();
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));

                } else {
                    //Verified token
                    
                    String name = jobject.get("name").getAsString();
                    String altName = jobject.get("altname").getAsString();
                    //to be removed!!!!
                    String userName = jobject.get("username").getAsString();
                    String passWord = jobject.get("password").getAsString();
                    String contactType = jobject.get("contacttype").getAsString();
                    String otherExplanation = jobject.get("explainifother").getAsString();
                    String profession = jobject.get("profession").getAsString();
                    String jobTitle = jobject.get("jobtitle").getAsString();
                    String nric = jobject.get("nricfin").getAsString();
                    String gender = jobject.get("gender").getAsString();
                    String nationality = jobject.get("nationality").getAsString();
                    String dateOfBirth = jobject.get("dateofbirth").getAsString();
                    String remarks = jobject.get("remarks").getAsString();
                    int countryCode = Integer.parseInt(jobject.get("countrycode").getAsString());
                    int phoneNumber = Integer.parseInt(jobject.get("phonenumber").getAsString());
                    String email = jobject.get("email").getAsString();
                    String country = jobject.get("country").getAsString();
                    int zipCode = Integer.parseInt(jobject.get("zipcode").getAsString());
                    String address = jobject.get("address").getAsString();

                    Date dob = null;
                    try {
                        dob = sdf.parse(dateOfBirth);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        json.addProperty("message", "failed");
                        out.println(gson.toJson(json));
                        return;
                    }
                    //Validation of fields

                    //Create new contact object
                    Contact newContact = new Contact(contactType, username, name, altName, otherExplanation, profession,
                            jobTitle, nric, gender, nationality, dob);

                    int newContactId = ContactDAO.addContact(newContact);

                    if (newContactId <= 0) {
                        json.addProperty("message", "failed");
                        out.println(gson.toJson(json));
                        return;
                    } else {

                        newContact.setContactId(newContactId);
                        //Create new phone/address/email object
                        Phone newPhone = new Phone(newContact, countryCode, phoneNumber, username);
                        Email newEmail = new Email(newContact, email, username);
                        Address newAddress = new Address(newContact, country, zipCode, address, username);

                        boolean addPhone = PhoneDAO.addPhone(newPhone);
                        boolean addEmail = EmailDAO.addEmail(newEmail);
                        boolean addAddress = AddressDAO.addAddress(newAddress);

                        if (addPhone && addEmail && addAddress) {
                            json.addProperty("message", "success");
                            out.println(gson.toJson(json));
                        } else {
                            json.addProperty("message", "failed");
                            out.println(gson.toJson(json));
                        }

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
