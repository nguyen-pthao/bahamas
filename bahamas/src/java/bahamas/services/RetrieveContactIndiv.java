package bahamas.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bahamas.dao.AddressDAO;
import bahamas.dao.AppreciationDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.DonationDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.LanguageDAO;
import bahamas.dao.MembershipDAO;
import bahamas.dao.OfficeHeldDAO;
import bahamas.dao.PhoneDAO;
import bahamas.dao.ProxyDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.SkillDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.Address;
import bahamas.entity.Appreciation;
import bahamas.entity.Contact;
import bahamas.entity.Donation;
import bahamas.entity.Email;
import bahamas.entity.EventParticipation;
import bahamas.entity.LanguageAssignment;
import bahamas.entity.Membership;
import bahamas.entity.OfficeHeld;
import bahamas.entity.Phone;
import bahamas.entity.Proxy;
import bahamas.entity.SkillAssignment;
import bahamas.entity.TeamJoin;
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
 * @author tan.si.hao
 */
@WebServlet(urlPatterns = {"/contact.retrieve.indiv"})
public class RetrieveContactIndiv extends HttpServlet {

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
                String cidString = jobject.get("cid").getAsString();
                String otherCidString = jobject.get("other_cid").getAsString();
                String permission = jobject.get("permission").getAsString();
                String teamName = jobject.get("team_name").getAsString();

                if ((token == null || token.isEmpty()) || (cidString == null || cidString.isEmpty()) || (otherCidString == null || otherCidString.isEmpty()) || (permission == null || permission.isEmpty()) ) {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                    return;
                }

                String username = Authenticator.verifyToken(token);
                ContactDAO contactDAO = new ContactDAO();
                Contact contact = contactDAO.retrieveContactByUsername(username);
                int cid = Integer.parseInt(cidString);
                int otherCid = Integer.parseInt(cidString);
                Contact viewContact = contactDAO.retrieveContactById(otherCid);
                
                if (viewContact != null) {

                    json.addProperty("message", "success");
                                           
                    if (contact.isIsAdmin()) { //Admin
                        JsonArray contactArray = retrieveByAdminTmEl(viewContact);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), permission) && permission.equals("teammanager")) { //Team manager
                        JsonArray contactArray = retrieveByAdminTmEl(viewContact);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else {
                        //int cid = contact.getContactId();
                        //check permission, 
                        if (RoleCheckDAO.checkRole(contact.getContactId(), teamName, permission)) {

                            if (permission.equals("eventleader")) { //Event leader
                                // To be confirm
                                JsonArray contactArray = retrieveByAdminTmEl(contact);
                                json.add("contact", contactArray);
                                out.println(gson.toJson(json));
                                return;
                            } else if (permission.equals("associate")) { //Associate
                                // To be confirm
                                //JsonArray contactArray = retrieveByAssociate(contact);
                                //json.add("contact", contactArray);
                                //out.println(gson.toJson(json));
                                return;
                            }
                        }

                    }
                    

                  
                } else {

                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                }
                json.addProperty("message", "fail");
                out.println(gson.toJson(json));
            }

        }
    }
    
    
    private static JsonArray retrieveByAdminTmEl(Contact contact) {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        JsonArray contactArray = new JsonArray();
        JsonObject jsonContactObj;
        JsonArray jsonObjList = new JsonArray();

        ArrayList<Email> emailList = EmailDAO.retrieveAllEmail(contact);
        ArrayList<Phone> phoneList = PhoneDAO.retrieveAllPhone(contact);
        ArrayList<Address> addressList = AddressDAO.retrieveAllAddress(contact);
        ArrayList<OfficeHeld> officeHeldList = OfficeHeldDAO.retrieveOfficeHeldByCID(contact.getContactId());
        
        ArrayList<Proxy> proxyList = ProxyDAO.retrieveByProxyCID(contact.getContactId());
        ArrayList<Membership> membershipList = MembershipDAO.retrieveMembershipByCID(contact.getContactId());
        ArrayList<LanguageAssignment> languageAssignmentList = LanguageDAO.retrieveLanguageByCID(contact.getContactId());
        ArrayList<SkillAssignment> SkillAssignmentList = SkillDAO.retrieveSkillByCID(contact.getContactId());
        ArrayList<Appreciation> AppreciationList = AppreciationDAO.retrieveAppreciation(contact.getContactId());
        ArrayList<Donation> DonationList = DonationDAO.retrieveDonationByCID(contact.getContactId());
        ArrayList<TeamJoin> TeamJoinList = TeamJoinDAO.retrieveAllTeamJoin(contact.getUsername());
        
        
        
        String emailStr = "";
        String phoneStr = "";
        String addressStr = "";
        String officeHeldStr = "";
        String proxy = "";
        String membership = "";
        String language = "";
        String skill = "";
        String Appreciation = "";
        String donation = "";
        String TeamJoin = "";
        
        String name = contact.getName();
        String altName = contact.getAltName();
        String contactType = contact.getContactType();
        String explainIfOther = contact.getExplainIfOther();
        String profession = contact.getProfession();
        String jobTitle = contact.getJobTitle();
        String nric = contact.getNric();
        String gender = contact.getGender();
        String nationality = contact.getNationality();
        String remarks = contact.getRemarks();

        if (name == null) {
            name = "";
        }
        if (altName == null) {
            altName = "";
        }
        if (contactType == null) {
            contactType = "";
        }
        if (explainIfOther == null) {
            explainIfOther = "";
        }
        if (profession == null) {
            profession = "";
        }
        if (jobTitle == null) {
            jobTitle = "";
        }
        if (nric == null) {
            nric = "";
        }
        if (gender == null) {
            gender = "";
        }
        if (nationality == null) {
            nationality = "";
        }
        if (remarks == null) {
            remarks = "";
        }
        /*
        if (!emailList.isEmpty()) {

            for (int i = 0; i < emailList.size() - 1; i++) {
                Email email = emailList.get(i);
                emailStr += email.getEmail() + " | ";
            }
            Email email = emailList.get(emailList.size() - 1);
            emailStr += email.getEmail();

        }
        if (!phoneList.isEmpty()) {

            for (int i = 0; i < phoneList.size() - 1; i++) {
                Phone phone = phoneList.get(i);
                phoneStr += "+" + phone.getCountryCode() + " " + phone.getPhoneNumber() + " | ";
            }
            Phone phone = phoneList.get(phoneList.size() - 1);
            phoneStr += "+" + phone.getCountryCode() + " " + phone.getPhoneNumber();

        }
        if (!addressList.isEmpty()) {

            for (int i = 0; i < addressList.size() - 1; i++) {
                Address address = addressList.get(i);
                addressStr += address.getAddress() + ", " + address.getCountry() + ", " + address.getZipcode() + " | ";
            }
            Address address = addressList.get(addressList.size() - 1);
            addressStr += address.getAddress() + ", " + address.getCountry() + ", " + address.getZipcode();

        }
        if (!officeHeldList.isEmpty()) {

            for (int i = 0; i < officeHeldList.size() - 1; i++) {
                OfficeHeld officeHeld = officeHeldList.get(i);
                officeHeldStr += officeHeld.getOfficeHeldPosition() + ", " + officeHeld.getCreatedBy() + ", " + officeHeld.getRemarks() + " | ";
            }
            OfficeHeld officeHeld = officeHeldList.get(officeHeldList.size() - 1);
            officeHeldStr += officeHeld.getOfficeHeldPosition() + ", " + officeHeld.getCreatedBy() + ", " + officeHeld.getRemarks();

        }
        
        */

        jsonContactObj = new JsonObject();
        jsonContactObj.addProperty("other_cid", contact.getContactId());
        jsonContactObj.addProperty("name", name);
        jsonContactObj.addProperty("alt_name", altName);
        jsonContactObj.addProperty("contact_type", contactType);
        jsonContactObj.addProperty("explain_if_other", explainIfOther);
        jsonContactObj.addProperty("profession", profession);
        jsonContactObj.addProperty("job_title", jobTitle);
        jsonContactObj.addProperty("nric", nric);
        jsonContactObj.addProperty("gender", gender);
        jsonContactObj.addProperty("nationality", nationality);
        if (contact.getDateOfBirth() != null) {
            jsonContactObj.addProperty("date_of_birth", sdf.format(contact.getDateOfBirth()));
        } else {
            jsonContactObj.addProperty("date_of_birth", "");
        }
        
        if (!phoneList.isEmpty()) {
            
            for (int i = 0; i < phoneList.size(); i++) {
                JsonObject jsonPhoneObj = new JsonObject();
                Phone phone = phoneList.get(i);
                //phoneStr += "+" + phone.getCountryCode() + " " + phone.getPhoneNumber() + " | ";
                jsonPhoneObj.addProperty("country_code", phone.getCountryCode());
                jsonPhoneObj.addProperty("phone", phone.getPhoneNumber());
                //jsonPhoneObj.addProperty("remarks", phone.getRemarks());
                
                if (phone.getRemarks() != null) {
                    jsonPhoneObj.addProperty("remarks", phone.getRemarks());
                } else {
                    jsonPhoneObj.addProperty("remarks", "");
                }
                
                if (phone.getDateObsolete() != null) {
                    jsonPhoneObj.addProperty("date_obsolete", sdf.format(phone.getDateCreated()));
                } else {
                    jsonPhoneObj.addProperty("date_obsolete", "");
                }
                jsonObjList.add(jsonPhoneObj);
                jsonContactObj.add("phone", jsonObjList);
            }

        } else {
            jsonContactObj.addProperty("phone", "");
        }
        
        
        
        
        
        
        jsonContactObj.addProperty("email", emailStr);
        jsonContactObj.addProperty("address", addressStr);
        jsonContactObj.addProperty("remarks", remarks);
        contactArray.add(jsonContactObj);
        jsonContactObj.addProperty("cid", contact.getContactId());

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
