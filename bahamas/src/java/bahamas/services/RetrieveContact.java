/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AddressDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.PhoneDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Address;
import bahamas.entity.Contact;
import bahamas.entity.Email;
import bahamas.entity.Phone;
import bahamas.util.Authenticator;
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
                //String cidString = jobject.get("cid").getAsString();
                //Optional for admin and novice
                //String teamName = jobject.get("teamname").getAsString();
                //String permission = jobject.get("permission").getAsString();

                if ((token == null || token.isEmpty())) {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                    return;
                }

                String username = Authenticator.verifyToken(token);
                ContactDAO contactDAO = new ContactDAO();
                Contact contact = contactDAO.retrieveContactByUsername(username);

                //ContactDAO contactDAO = new ContactDAO();
                ArrayList<Contact> contactList = contactDAO.retrieveAllContact();

                if (!contactList.isEmpty() && !contact.isIsNovice()) {

                    json.addProperty("message", "success");
                    
                    if (contact.isIsAdmin()) {
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "teammanager")) {
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "eventleader")) {
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "associate")) {
                        JsonArray contactArray = retrieveAll(contactList, false);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    }
                    
                    
                    /*                       
                    if (contact.isIsAdmin()) { //Admin
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), permission) && permission.equals("teammanager")) { //Team manager
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else {
                        int cid = Integer.parseInt(cidString);
                        //check permission, 
                        if (RoleCheckDAO.checkRole(cid, teamName, permission)) {

                            if (permission.equals("eventleader")) { //Event leader
                                // To be confirm
                                JsonArray contactArray = retrieveAll(contactList, true);
                                json.add("contact", contactArray);
                                out.println(gson.toJson(json));
                                return;
                            } else if (permission.equals("associate")) { //Associate
                                // To be confirm
                                JsonArray contactArray = retrieveAll(contactList, false);
                                json.add("contact", contactArray);
                                out.println(gson.toJson(json));
                                return;
                            }
                        }

                    }
                    */

                    //}
                } else {

                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                    return;
                }
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));
            }

        }
    }

    private static JsonArray retrieveAll(ArrayList<Contact> contactList, boolean unlock) {

        JsonArray contactArray = new JsonArray();
        JsonObject jsonContactObj;

        for (Contact c : contactList) {
            
            ArrayList<Email> emailList = EmailDAO.retrieveAllEmail(c);
            ArrayList<Phone> phoneList = PhoneDAO.retrieveAllPhone(c);
            ArrayList<Address> addressList = AddressDAO.retrieveAllAddress(c);
            
            String emailStr = "";
            String phoneStr = "";
            String addressStr = "";
            String name = c.getName();
            String altName = c.getAltName();
            String contactType = c.getContactType();
            String explainIfOther = c.getExplainIfOther();
            String profession = c.getProfession();
            String jobTitle = c.getJobTitle();
            String nric = c.getNric();
            String gender = c.getGender();
            String nationality = c.getNationality();
            String remarks = c.getRemarks();
            
            if(name == null){
                name = "";
            }
            if(altName == null){
                altName = "";
            }
            if(contactType == null){
                contactType = "";
            }
            if(explainIfOther == null){
                explainIfOther = "";
            }
            if(profession == null){
                profession = "";
            }
            if(jobTitle == null){
                jobTitle = "";
            }
            if(nric == null){
                nric = "";
            }
            if(gender == null){
                gender = "";
            }
            if(nationality == null){
                nationality = "";
            }
            if(remarks == null){
                remarks = "";
            }
            
            
            if(!emailList.isEmpty()){
                
                for(int i = 0; i < emailList.size()-1; i++){
                    Email email = emailList.get(i);
                    emailStr += email.getEmail() + " | ";
                }
                Email email = emailList.get(emailList.size()-1);
                emailStr += email.getEmail();
                
            }
            if(!phoneList.isEmpty()){
                
                for(int i = 0; i < phoneList.size()-1; i++){
                    Phone phone = phoneList.get(i);
                    phoneStr += "+" +phone.getCountryCode() + " " + phone.getPhoneNumber() + " | ";
                }
                Phone phone = phoneList.get(phoneList.size()-1);
                phoneStr += "+" +phone.getCountryCode() + " " + phone.getPhoneNumber();
                
            }
            if(!addressList.isEmpty()){
                
                for(int i = 0; i < addressList.size()-1; i++){
                    Address address = addressList.get(i);
                    addressStr += address.getAddress() + ", " + address.getCountry() + ", " + address.getZipcode() + " | ";
                }
                Address address = addressList.get(addressList.size()-1);
                addressStr += address.getAddress() + ", " + address.getCountry() + ", " + address.getZipcode();
                
            }
            
            

            jsonContactObj = new JsonObject();
            jsonContactObj.addProperty("name", name);
            jsonContactObj.addProperty("alt_name", altName);
            if(unlock){
                jsonContactObj.addProperty("phone", phoneStr);
            }
            jsonContactObj.addProperty("email", emailStr);
            jsonContactObj.addProperty("contact_type", contactType);
            /*
            jsonContactObj.addProperty("explain_if_other", explainIfOther);
            if(unlockAll){
                jsonContactObj.addProperty("profession", profession);
                jsonContactObj.addProperty("job_title", jobTitle);
                jsonContactObj.addProperty("nric", nric);
                jsonContactObj.addProperty("gender", gender);
                jsonContactObj.addProperty("nationality", nationality);
            }
            if(c.getDateOfBirth() != null){
                jsonContactObj.addProperty("date_of_birth", sdf.format(c.getDateOfBirth()));
            }else{
                jsonContactObj.addProperty("date_of_birth", "");
            }
            jsonContactObj.addProperty("address", addressStr);
            jsonContactObj.addProperty("remarks", remarks);
            */
            contactArray.add(jsonContactObj);
            jsonContactObj.addProperty("cid", c.getContactId());
        }
        return contactArray;
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
