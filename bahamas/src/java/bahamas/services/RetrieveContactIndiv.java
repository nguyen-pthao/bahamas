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
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import is203.JWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            throws ServletException, IOException, JWTException {
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
                String token = Validator.containsBlankField(jobject.get("token"));
                //String cidString = jobject.get("cid").getAsString();
                String otherCidString = Validator.containsBlankField(jobject.get("other_cid"));
                //String permission = jobject.get("permission").getAsString();
                //String teamName = jobject.get("team_name").getAsString();

                if ((token == null || token.isEmpty()) || (otherCidString == null || otherCidString.isEmpty() ) ) {
                    json.addProperty("message", "fail");
                    out.println(gson.toJson(json));
                    return;
                }

                String username = Authenticator.verifyToken(token);
                ContactDAO contactDAO = new ContactDAO();
                Contact contact = contactDAO.retrieveContactByUsername(username);
                //int cid = Integer.parseInt(cidString);
                int otherCid = Integer.parseInt(otherCidString);
                Contact viewContact = contactDAO.retrieveContactById(otherCid);
                
                if (viewContact != null && !contact.isIsNovice()) {

                    json.addProperty("message", "success");
                    
                    /*
                    try to add there
                    if (contact.isIsNovice()) {
                        json.addProperty("user_type", "novice");
                    } 
                    */
                    if (contact.isIsAdmin()) {
                        JsonArray contactArray = retrieveContactDetails(viewContact, contact.isIsAdmin(),false, false, Integer.toString(contact.getContactId()));
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "teammanager")) {
                        JsonArray contactArray = retrieveContactDetails(viewContact, false, true , false, Integer.toString(contact.getContactId()));
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "eventleader")) {
                        JsonArray contactArray = retrieveContactDetails(viewContact, false, false, true, Integer.toString(contact.getContactId()));
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "associate")){
                        JsonArray contactArray = retrieveContactDetails(viewContact, false, false, false, Integer.toString(contact.getContactId()));
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    }
                    
                    /*                     
                    if (contact.isIsAdmin()) { //Admin
                        JsonArray contactArray = retrieveByAdminTmEl(viewContact, contact.isIsAdmin());
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), permission) && permission.equals("teammanager")) { //Team manager
                        JsonArray contactArray = retrieveByAdminTmEl(viewContact, false);
                        json.add("contact", contactArray);
                        out.println(gson.toJson(json));
                        return;
                    } else {
                        //int cid = contact.getContactId();
                        //check permission, 
                        if (RoleCheckDAO.checkRole(contact.getContactId(), teamName, permission)) {

                            if (permission.equals("eventleader")) { //Event leader
                                // To be confirm
                                JsonArray contactArray = retrieveByAdminTmEl(contact, false);
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
                    
                    */
                  
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
    
    private static JsonArray retrieveContactDetails(Contact contact, boolean isAdmin, boolean isTeamMgt, boolean isEventLead, String cidString) {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdft = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        JsonArray contactArray = new JsonArray();
        JsonObject jsonContactObj;
        JsonArray jsonObjPhone = new JsonArray();
        JsonArray jsonObjEmail = new JsonArray();
        JsonArray jsonObjAddress = new JsonArray();
        JsonArray jsonObjOfficeHeld = new JsonArray();
        JsonArray jsonObjProxy = new JsonArray();
        JsonArray jsonObjMembership = new JsonArray();
        JsonArray jsonObjLanguage = new JsonArray();
        JsonArray jsonObjSkill = new JsonArray();
        JsonArray jsonObjAppreciation = new JsonArray();
        JsonArray jsonObjDonation = new JsonArray();
        JsonArray jsonObjTeamJoin = new JsonArray();
        

        ArrayList<Email> emailList = EmailDAO.retrieveAllEmail(contact);
        ArrayList<Phone> phoneList = PhoneDAO.retrieveAllPhone(contact);
        ArrayList<Address> addressList = AddressDAO.retrieveAllAddress(contact);
        ArrayList<OfficeHeld> officeHeldList = OfficeHeldDAO.retrieveOfficeHeldByCID(contact.getContactId());
        
        ArrayList<Proxy> proxyList = ProxyDAO.retrieveByProxyCID(contact.getContactId());
        ArrayList<Membership> membershipList = MembershipDAO.retrieveMembershipByCID(contact.getContactId());
        ArrayList<LanguageAssignment> languageAssignmentList = LanguageDAO.retrieveLanguageByCID(contact.getContactId());
        ArrayList<SkillAssignment> skillAssignmentList = SkillDAO.retrieveSkillByCID(contact.getContactId());
        ArrayList<Appreciation> appreciationList = AppreciationDAO.retrieveAppreciation(contact.getContactId());
        ArrayList<Donation> donationList = DonationDAO.retrieveDonationByCID(contact.getContactId());
        //ArrayList<TeamJoin> teamJoinList = TeamJoinDAO.retrieveAllTeamJoin(contact.getUsername());
        ArrayList<TeamJoin> teamJoinList = TeamJoinDAO.retrieveAllTeamJoinCID(contact.getContactId());
        String emailStr = "";
        String phoneStr = "";
        String addressStr = "";
        String officeHeldStr = "";
        
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
 

        jsonContactObj = new JsonObject();
        jsonContactObj.addProperty("other_cid", contact.getContactId());
        jsonContactObj.addProperty("is_admin", Boolean.toString(contact.isIsAdmin()));
        if(isAdmin){
            if (contact.getUsername() != null) {
                jsonContactObj.addProperty("username", contact.getUsername());
            } else {
                jsonContactObj.addProperty("username", "");
            }
            jsonContactObj.addProperty("deactivated",Boolean.toString(contact.isDeactivated()));
        }
        jsonContactObj.addProperty("name", name);
        if (isAdmin || isTeamMgt){
            if (contact.getNric() != null) {
                jsonContactObj.addProperty("nric_fin", contact.getNric());
            } else {
                jsonContactObj.addProperty("nric_fin", "");
            }
        }
        jsonContactObj.addProperty("alt_name", altName);
        jsonContactObj.addProperty("contact_type", contactType);
        jsonContactObj.addProperty("explain_if_other", explainIfOther);
        
        if (isAdmin || isTeamMgt){
            
            jsonContactObj.addProperty("profession", profession);
            jsonContactObj.addProperty("job_title", jobTitle);
            jsonContactObj.addProperty("gender", gender);
            jsonContactObj.addProperty("nationality", nationality);
            if (contact.getDateOfBirth() != null) {
                jsonContactObj.addProperty("date_of_birth", sdf.format(contact.getDateOfBirth()));
            } else {
                jsonContactObj.addProperty("date_of_birth", "");
            }
            jsonContactObj.addProperty("date_created", sdft.format(contact.getDateCreated()));    
            jsonContactObj.addProperty("created_by", contact.getCreatedBy());   
            
        }
        jsonContactObj.addProperty("remarks", remarks);
        if (isAdmin || isTeamMgt || isEventLead){
            if (phoneList != null && !phoneList.isEmpty()) {

                for (int i = 0; i < phoneList.size(); i++) {
                    JsonObject jsonPhoneObj = new JsonObject();
                    Phone phone = phoneList.get(i);
                    //phoneStr += "+" + phone.getCountryCode() + " " + phone.getPhoneNumber() + " | ";
                    jsonPhoneObj.addProperty("country_code", phone.getCountryCode());
                    jsonPhoneObj.addProperty("phone_number", phone.getPhoneNumber());
                    //jsonPhoneObj.addProperty("remarks", phone.getRemarks());

                    if (phone.getRemarks() != null) {
                        jsonPhoneObj.addProperty("remarks", phone.getRemarks());
                    } else {
                        jsonPhoneObj.addProperty("remarks", "");
                    }

                    if (phone.getDateObsolete() != null) {
                        jsonPhoneObj.addProperty("date_obsolete", sdf.format(phone.getDateObsolete()));
                    } else {
                        jsonPhoneObj.addProperty("date_obsolete", "");
                    }
                    jsonPhoneObj.addProperty("created_by", phone.getCreatedBy());
                    jsonPhoneObj.addProperty("date_created", sdft.format(phone.getDateCreated()));    
                    jsonObjPhone.add(jsonPhoneObj);
                    jsonContactObj.add("phone", jsonObjPhone);
                }

            } else {
                jsonContactObj.addProperty("phone", "");
            }
        }
        
        
        if (emailList != null && !emailList.isEmpty()) {
            
            for (int i = 0; i < emailList.size(); i++) {
                JsonObject jsonEmailObj = new JsonObject();
                Email email = emailList.get(i);
                
                jsonEmailObj.addProperty("email", email.getEmail());
                
                if (email.getRemarks() != null) {
                    jsonEmailObj.addProperty("remarks", email.getRemarks());
                } else {
                    jsonEmailObj.addProperty("remarks", "");
                }

                if (email.getDateObsolete() != null) {
                    jsonEmailObj.addProperty("date_obsolete", sdf.format(email.getDateObsolete()));
                } else {
                    jsonEmailObj.addProperty("date_obsolete", "");
                }
             
                if (isAdmin || isTeamMgt || isEventLead){
                    jsonEmailObj.addProperty("created_by", email.getCreatedBy());
                    jsonEmailObj.addProperty("date_created", sdft.format(email.getDateCreated()));    
                    jsonEmailObj.addProperty("verified", email.getVerified());
                }
                
                jsonObjEmail.add(jsonEmailObj);
                jsonContactObj.add("email", jsonObjEmail);
            }

        } else {
            jsonContactObj.addProperty("email", "");
        }
       
        if(isAdmin || isTeamMgt || isEventLead){
            if (addressList != null && !addressList.isEmpty()) {

                for (int i = 0; i < addressList.size(); i++) {
                    JsonObject jsonAddressObj = new JsonObject();
                    Address address = addressList.get(i);

                    if (address.getCountry() != null) {
                        jsonAddressObj.addProperty("country", address.getCountry());
                    } else {
                        jsonAddressObj.addProperty("country", "");
                    }

                    jsonAddressObj.addProperty("address", address.getAddress());

                    if (address.getCountry() != null) {
                        jsonAddressObj.addProperty("zipcode", address.getZipcode());
                    } else {
                        jsonAddressObj.addProperty("zipcode", "");
                    }

                    if (address.getRemarks() != null) {
                        jsonAddressObj.addProperty("remarks", address.getRemarks());
                    } else {
                        jsonAddressObj.addProperty("remarks", "");
                    }
                    if (address.getDateObsolete() != null) {
                        jsonAddressObj.addProperty("date_obsolete", sdf.format(address.getDateObsolete()));
                    } else {
                        jsonAddressObj.addProperty("date_obsolete", "");
                    }
                    jsonAddressObj.addProperty("created_by", address.getCreatedBy());
                    jsonAddressObj.addProperty("date_created", sdft.format(address.getDateCreated()));    
                    jsonObjAddress.add(jsonAddressObj);
                    jsonContactObj.add("address", jsonObjAddress);
                }

            } else {
                jsonContactObj.addProperty("address", "");
            }
        }
        
        //offic
        if(isAdmin || isTeamMgt || isEventLead){
            if (officeHeldList != null && !officeHeldList.isEmpty()) {

                for (int i = 0; i < officeHeldList.size(); i++) {
                    JsonObject jsonOfficeHObj = new JsonObject();
                    OfficeHeld officeHeld = officeHeldList.get(i);

                    jsonOfficeHObj.addProperty("office_held", officeHeld.getOfficeHeldPosition());
                    jsonOfficeHObj.addProperty("start_office", sdf.format(officeHeld.getStartOffice()));
                    jsonOfficeHObj.addProperty("end_office", sdf.format(officeHeld.getEndOffice()));
                    if (officeHeld.getRemarks() != null) {
                        jsonOfficeHObj.addProperty("remarks", officeHeld.getRemarks());
                    } else {
                        jsonOfficeHObj.addProperty("remarks", "");
                    }
                    jsonOfficeHObj.addProperty("created_by", officeHeld.getCreatedBy());
                    jsonOfficeHObj.addProperty("date_created", sdft.format(officeHeld.getDateCreated()));
                    jsonObjOfficeHeld.add(jsonOfficeHObj);
                    jsonContactObj.add("office_held", jsonObjOfficeHeld);
                }

            } else {
                jsonContactObj.addProperty("office_held", "");
            }
        }
        //proxyList
        if(isAdmin || isTeamMgt || isEventLead){
            if (proxyList != null && !proxyList.isEmpty()) {

                for (int i = 0; i < proxyList.size(); i++) {
                    JsonObject jsonProxyObj = new JsonObject();
                    Proxy proxy = proxyList.get(i);

                    ContactDAO cDAO = new ContactDAO();
                    Contact c1 = cDAO.retrieveContactById(proxy.getProxyID());
                    Contact c2 = cDAO.retrieveContactById(proxy.getPrincipalID());

                    jsonProxyObj.addProperty("proxy_id", proxy.getProxyID());
                    jsonProxyObj.addProperty("proxy_name", c1.getName());
                    jsonProxyObj.addProperty("principal_id", proxy.getPrincipalID());
                    jsonProxyObj.addProperty("principal_name", c2.getName());
                    if (proxy.getProxyStanding() != null) {
                        jsonProxyObj.addProperty("proxy_standing", proxy.getProxyStanding());
                    } else {
                        jsonProxyObj.addProperty("proxy_standing", "");
                    }


                    if (proxy.getRemarks() != null) {
                        jsonProxyObj.addProperty("remarks", proxy.getRemarks());
                    } else {
                        jsonProxyObj.addProperty("remarks", "");
                    }
                    if (proxy.getDateObsolete() != null) {
                        jsonProxyObj.addProperty("date_obsolete", sdf.format(proxy.getDateObsolete()));
                    } else {
                        jsonProxyObj.addProperty("date_obsolete", "");
                    }
                    jsonProxyObj.addProperty("created_by", proxy.getCreatedBy());
                    jsonProxyObj.addProperty("date_created", sdft.format(proxy.getDateCreated()));
                    jsonObjProxy.add(jsonProxyObj);
                    jsonContactObj.add("proxy", jsonObjProxy);
                }

            } else {
                jsonContactObj.addProperty("proxy", "");
            }
        }
        //membershipList
        if(isAdmin || isTeamMgt || isEventLead){
            if (membershipList != null && !membershipList.isEmpty()) {

                for (int i = 0; i < membershipList.size(); i++) {
                    JsonObject jsonMembershipObj = new JsonObject();
                    Membership membership = membershipList.get(i);
                    jsonMembershipObj.addProperty("membership_id", sdf.format(membership.getMembershipId()));
                    if (membership.getStartMembership() != null) {
                        jsonMembershipObj.addProperty("start_date", sdf.format(membership.getStartMembership()));
                    } else {
                        jsonMembershipObj.addProperty("start_date", "");
                    }
                    if (membership.getEndMembership() != null) {
                        jsonMembershipObj.addProperty("end_date", sdf.format(membership.getEndMembership()));
                    } else {
                        jsonMembershipObj.addProperty("end_date", "");
                    }
                    if (membership.getReceiptDate()!= null) {
                        jsonMembershipObj.addProperty("receipt_date", sdf.format(membership.getReceiptDate()));
                    } else {
                        jsonMembershipObj.addProperty("receipt_date", "");
                    }
                    if (membership.getSubscriptionAmount() > 0) {
                        jsonMembershipObj.addProperty("subscription_amount", membership.getSubscriptionAmount());
                    } else {
                        jsonMembershipObj.addProperty("subscription_amount", "");
                    }
                    if (membership.getExtTransactionRef() != null) {
                        jsonMembershipObj.addProperty("ext_transaction_ref", membership.getExtTransactionRef());
                    } else {
                        jsonMembershipObj.addProperty("ext_transaction_ref", "");
                    }
                    if (membership.getReceiptNumber() != null) {
                        jsonMembershipObj.addProperty("receipt_number", membership.getReceiptNumber());
                    } else {
                        jsonMembershipObj.addProperty("receipt_number", "");
                    }
                    if (membership.getReceiptNumber() != null) {
                        jsonMembershipObj.addProperty("receipt_number", membership.getReceiptNumber());
                    } else {
                        jsonMembershipObj.addProperty("receipt_number", "");
                    }
                    if (membership.getRemarks() != null) {
                        jsonMembershipObj.addProperty("remarks", membership.getRemarks());
                    } else {
                        jsonMembershipObj.addProperty("remarks", "");
                    }
                    if (membership.getReceiptModeName() != null) {
                        jsonMembershipObj.addProperty("receipt_mode_name", membership.getReceiptModeName());
                    } else {
                        jsonMembershipObj.addProperty("receipt_mode_name", "");
                    }
                    if (membership.getExplainIfOtherReceipt()!= null) {
                        jsonMembershipObj.addProperty("explain_if_other_receipt", membership.getExplainIfOtherReceipt());
                    } else {
                        jsonMembershipObj.addProperty("explain_if_other_receipt", "");
                    }
                    if (membership.getMembershipClassName()!= null) {
                        jsonMembershipObj.addProperty("membership_class_name", membership.getMembershipClassName());
                    } else {
                        jsonMembershipObj.addProperty("membership_class_name", "");
                    }
                    if (membership.getExplainIfOtherClass()!= null) {
                        jsonMembershipObj.addProperty("explain_if_other_class", membership.getExplainIfOtherClass());
                    } else {
                        jsonMembershipObj.addProperty("explain_if_other_class", "");
                    }
                    if (membership.getPaymentModeName()!= null) {
                        jsonMembershipObj.addProperty("payment_mode_name", membership.getPaymentModeName());
                    } else {
                        jsonMembershipObj.addProperty("payment_mode_name", "");
                    }
                    if (membership.getExplainIfOtherPayment()!= null) {
                        jsonMembershipObj.addProperty("explain_if_other_payment", membership.getExplainIfOtherPayment());
                    } else {
                        jsonMembershipObj.addProperty("explain_if_other_payment", "");
                    }
                    jsonMembershipObj.addProperty("created_by", membership.getCreatedBy());
                    jsonMembershipObj.addProperty("date_created", sdft.format(membership.getDateCreated()));
                    jsonObjMembership.add(jsonMembershipObj);
                    jsonContactObj.add("membership", jsonObjMembership);
                }
            } else {
                jsonContactObj.addProperty("membership", "");
            }
        }
        //languageAssignmentList
        if (languageAssignmentList != null && !languageAssignmentList.isEmpty()) {
            
            for (int i = 0; i < languageAssignmentList.size(); i++) {
                JsonObject jsonLanguageListObj = new JsonObject();
                LanguageAssignment languageAssignment = languageAssignmentList.get(i);
                
                jsonLanguageListObj.addProperty("language_name", languageAssignment.getLanguage());
                if (languageAssignment.getProficiency()!= null) {
                    jsonLanguageListObj.addProperty("proficiency", languageAssignment.getProficiency());
                } else {
                    jsonLanguageListObj.addProperty("proficiency", "");
                }
                if (languageAssignment.getExplainIfOther()!= null) {
                    jsonLanguageListObj.addProperty("explain_if_other", languageAssignment.getExplainIfOther());
                } else {
                    jsonLanguageListObj.addProperty("explain_if_other", "");
                }
                if (languageAssignment.getRemarks() != null) {
                    jsonLanguageListObj.addProperty("remarks", languageAssignment.getRemarks());
                } else {
                    jsonLanguageListObj.addProperty("remarks", "");
                }
                if (languageAssignment.getDateObsolete() != null) {
                    jsonLanguageListObj.addProperty("date_obsolete", sdf.format(languageAssignment.getDateObsolete()));
                } else {
                    jsonLanguageListObj.addProperty("date_obsolete", "");
                }
                if(isAdmin || isTeamMgt || isEventLead){
                    jsonLanguageListObj.addProperty("created_by", languageAssignment.getCreatedBy());
                    jsonLanguageListObj.addProperty("date_created", sdft.format(languageAssignment.getDateCreated()));
                }
                jsonObjLanguage.add(jsonLanguageListObj);
                jsonContactObj.add("language_assignment", jsonObjLanguage);
            }

        } else {
            jsonContactObj.addProperty("language_assignment", "");
        }
        
        //skillAssignmentList
        if (skillAssignmentList != null && !skillAssignmentList.isEmpty()) {
            
            for (int i = 0; i < skillAssignmentList.size(); i++) {
                JsonObject jsonSkillListObj = new JsonObject();
                SkillAssignment skillAssignment = skillAssignmentList.get(i);

                jsonSkillListObj.addProperty("skill_name", skillAssignment.getSkillName());
                
                if (skillAssignment.getExplainIfOther()!= null) {
                    jsonSkillListObj.addProperty("explain_if_other", skillAssignment.getExplainIfOther());
                } else {
                    jsonSkillListObj.addProperty("explain_if_other", "");
                }
                if (skillAssignment.getRemarks() != null) {
                    jsonSkillListObj.addProperty("remarks", skillAssignment.getRemarks());
                } else {
                    jsonSkillListObj.addProperty("remarks", "");
                }
                if (skillAssignment.getDateObsolete() != null) {
                    jsonSkillListObj.addProperty("date_obsolete", sdf.format(skillAssignment.getDateObsolete()));
                } else {
                    jsonSkillListObj.addProperty("date_obsolete", "");
                }
                if(isAdmin || isTeamMgt || isEventLead){
                    jsonSkillListObj.addProperty("created_by", skillAssignment.getCreatedBy());
                    jsonSkillListObj.addProperty("date_created", sdft.format(skillAssignment.getDateCreated()));
                }
                jsonObjSkill.add(jsonSkillListObj);
                jsonContactObj.add("skill_assignment", jsonObjSkill);
            }

        } else {
            jsonContactObj.addProperty("skill_assignment", "");
        }

        //appreciationList jsonObjappreciation
        if(isAdmin || isTeamMgt){
            if (appreciationList != null && !appreciationList.isEmpty()) {

                for (int i = 0; i < appreciationList.size(); i++) {
                    JsonObject jsonAppreciationObj = new JsonObject();
                    Appreciation appreciation = appreciationList.get(i);

                    jsonAppreciationObj.addProperty("appreciation_id", Integer.toString(appreciation.getAppreciationId()));



                    if (appreciation.getAppraisalComments()!= null) {
                        jsonAppreciationObj.addProperty("appraisal_comments", appreciation.getAppraisalComments());
                    } else {
                        jsonAppreciationObj.addProperty("appraisal_comments", "");
                    }
                    if (appreciation.getAppraisalBy()!= null) {
                        jsonAppreciationObj.addProperty("appraisal_by", appreciation.getAppraisalBy());
                    } else {
                        jsonAppreciationObj.addProperty("appraisal_by", "");
                    }
                    if (appreciation.getAppraisalDate() != null) {
                        jsonAppreciationObj.addProperty("appraisal_date", sdf.format(appreciation.getAppraisalDate()));
                    } else {
                        jsonAppreciationObj.addProperty("appraisal_date", "");
                    }
                    if (appreciation.getAppreciationGesture()!= null) {
                        jsonAppreciationObj.addProperty("appreciation_gesture", appreciation.getAppreciationGesture());
                    } else {
                        jsonAppreciationObj.addProperty("appreciation_gesture", "");
                    }
                     if (appreciation.getAppreciationBy()!= null) {
                        jsonAppreciationObj.addProperty("appreciation_by", appreciation.getAppreciationBy());
                    } else {
                        jsonAppreciationObj.addProperty("appreciation_by", "");
                    }
                    if (appreciation.getAppreciationDate()!= null) {
                        jsonAppreciationObj.addProperty("appreciation_date", sdf.format(appreciation.getAppreciationDate()));
                    } else {
                        jsonAppreciationObj.addProperty("appreciation_date", "");
                    }
                    if (appreciation.getRemarks() != null) {
                        jsonAppreciationObj.addProperty("remarks", appreciation.getRemarks());
                    } else {
                        jsonAppreciationObj.addProperty("remarks", "");
                    }

                    jsonAppreciationObj.addProperty("created_by", appreciation.getCreatedBy());
                    jsonAppreciationObj.addProperty("date_created", sdft.format(appreciation.getDateCreated()));
                    jsonObjAppreciation.add(jsonAppreciationObj);
                    jsonContactObj.add("appreciation", jsonObjAppreciation);
                }

            } else {
                jsonContactObj.addProperty("appreciation", "");
            }
        }
        
        //donationList
        if(isAdmin || isTeamMgt){
            if (donationList != null && !donationList.isEmpty()) {

                for (int i = 0; i < donationList.size(); i++) {
                    JsonObject jsonDonationObj = new JsonObject();
                    Donation donation = donationList.get(i);

                    jsonDonationObj.addProperty("donation_id", Integer.toString(donation.getDonationId()));
                    if (donation.getDateReceived()!= null) {
                        jsonDonationObj.addProperty("date_received", sdf.format(donation.getDateReceived()));
                    } else {
                        jsonDonationObj.addProperty("date_received", "");
                    }
                    jsonDonationObj.addProperty("donation_amount", donation.getDonationAmount());
                    jsonDonationObj.addProperty("payment_mode", donation.getPaymentMode());


                    if (donation.getExplainIfOtherPayment()!= null) {
                        jsonDonationObj.addProperty("explain_if_other_payment", donation.getExplainIfOtherPayment());
                    } else {
                        jsonDonationObj.addProperty("explain_if_other_payment", "");
                    }
                    if (donation.getExtTransactionRef()!= null) {
                        jsonDonationObj.addProperty("ext_transaction_ref", donation.getExtTransactionRef());
                    } else {
                        jsonDonationObj.addProperty("ext_transaction_ref", "");
                    }
                    if (donation.getReceiptNumber()!= null) {
                        jsonDonationObj.addProperty("receipt_number", donation.getReceiptNumber());
                    } else {
                        jsonDonationObj.addProperty("receipt_number", "");
                    }
                    if (donation.getReceiptDate()!= null) {
                        jsonDonationObj.addProperty("receipt_date", sdf.format(donation.getReceiptDate()));
                    } else {
                        jsonDonationObj.addProperty("receipt_date", "");
                    }
                    jsonDonationObj.addProperty("receipt_mode_name", donation.getReceiptMode());
                    if (donation.getExplainIfOtherReceipt()!= null) {
                        jsonDonationObj.addProperty("explain_if_other_receipt", donation.getExplainIfOtherReceipt());
                    } else {
                        jsonDonationObj.addProperty("explain_if_other_receipt", "");
                    }
                    if (donation.getDonorInstructions()!= null) {
                        jsonDonationObj.addProperty("donor_instructions", donation.getDonorInstructions());
                    } else {
                        jsonDonationObj.addProperty("donor_instructions", "");
                    }
                    if (donation.getAllocation1() != null) {
                        jsonDonationObj.addProperty("allocation1", donation.getAllocation1());
                    } else {
                        jsonDonationObj.addProperty("allocation1", "");
                    }
                    if (donation.getSubAmount1() >= 0) {
                        jsonDonationObj.addProperty("subtotal1", donation.getSubAmount1());
                    } else {
                        jsonDonationObj.addProperty("subtotal1", "");
                    }
                    if (donation.getAllocation2() != null) {
                        jsonDonationObj.addProperty("allocation2", donation.getAllocation2());
                    } else {
                        jsonDonationObj.addProperty("allocation2", "");
                    }
                    if (donation.getSubAmount2() >= 0) {
                        jsonDonationObj.addProperty("subtotal2", donation.getSubAmount2());
                    } else {
                        jsonDonationObj.addProperty("subtotal2", "");
                    }
                    if (donation.getAllocation3() != null) {
                        jsonDonationObj.addProperty("allocation3", donation.getAllocation3());
                    } else {
                        jsonDonationObj.addProperty("allocation3", "");
                    }
                    if (donation.getSubAmount3() >= 0) {
                        jsonDonationObj.addProperty("subtotal3", donation.getSubAmount3());
                    } else {
                        jsonDonationObj.addProperty("subtotal3", "");
                    }
                    if (donation.getAssociatedOccasion()!= null) {
                        jsonDonationObj.addProperty("associated_occasion", donation.getAssociatedOccasion());
                    } else {
                        jsonDonationObj.addProperty("associated_occasion", "");
                    }
                    if (donation.getRemarks() != null) {
                        jsonDonationObj.addProperty("remarks", donation.getRemarks());
                    } else {
                        jsonDonationObj.addProperty("remarks", "");
                    }

                    jsonDonationObj.addProperty("created_by", donation.getCreatedBy());
                    jsonDonationObj.addProperty("date_created", sdft.format(donation.getDateCreated()));
                    jsonObjDonation.add(jsonDonationObj);
                    jsonContactObj.add("donation", jsonObjDonation);
                }
            } else {
                jsonContactObj.addProperty("donation", "");
            }
        }
        
        if(isAdmin || isTeamMgt || isEventLead){
        //jsonObjTeamJoin teamJoinList
            if (teamJoinList != null && !teamJoinList.isEmpty()) {

                for (int i = 0; i < teamJoinList.size(); i++) {
                    JsonObject jsonTeamJoinObj = new JsonObject();
                    TeamJoin teamJoin = teamJoinList.get(i);

                    jsonTeamJoinObj.addProperty("team_name", teamJoin.getTeamName());
                    if(teamJoin.getPermission() == null || teamJoin.getPermission().isEmpty()){
                        jsonTeamJoinObj.addProperty("permission", "Pending");
                    }else{
                        jsonTeamJoinObj.addProperty("permission", teamJoin.getPermission());
                    }

                    if (teamJoin.getExplainIfOthers()!= null) {
                        jsonTeamJoinObj.addProperty("explain_if_others", teamJoin.getExplainIfOthers());
                    } else {
                        jsonTeamJoinObj.addProperty("explain_if_others", "");
                    }
                    if (teamJoin.getSubTeam()!= null) {
                        jsonTeamJoinObj.addProperty("sub_team", teamJoin.getSubTeam());
                    } else {
                        jsonTeamJoinObj.addProperty("sub_team", "");
                    }
                    if (teamJoin.getDateObsolete() != null) {
                        jsonTeamJoinObj.addProperty("date_obsolete", sdf.format(teamJoin.getDateObsolete()));
                    } else {
                        jsonTeamJoinObj.addProperty("date_obsolete", "");
                    }
                    if (teamJoin.getRemarks() != null) {
                        jsonTeamJoinObj.addProperty("remarks", teamJoin.getRemarks());
                    } else {
                        jsonTeamJoinObj.addProperty("remarks", "");
                    }


                    jsonTeamJoinObj.addProperty("created_by", teamJoin.getCreatedBy());
                    jsonTeamJoinObj.addProperty("date_created", sdft.format(teamJoin.getDateCreated()));
                    jsonObjTeamJoin.add(jsonTeamJoinObj);
                    jsonContactObj.add("team_join", jsonObjTeamJoin);
                }

            } else {
                jsonContactObj.addProperty("team_join", "");
            }
        }
        
        contactArray.add(jsonContactObj);
        jsonContactObj.addProperty("cid", Integer.parseInt(cidString));

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
        try {
            processRequest(request, response);
        } catch (JWTException ex) {
            Logger.getLogger(RetrieveContactIndiv.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RetrieveContactIndiv.class.getName()).log(Level.SEVERE, null, ex);
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
