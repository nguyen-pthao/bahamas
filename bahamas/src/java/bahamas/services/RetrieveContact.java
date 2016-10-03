/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;


import bahamas.dao.ContactDAO;
import bahamas.dao.EmailDAO;
import bahamas.dao.PhoneDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.dao.TeamJoinDAO;
import bahamas.entity.Contact;
import bahamas.entity.Email;
import bahamas.entity.Phone;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author TANSIHAO
 */
@WebServlet(name = "RetrieveContact", urlPatterns = {"/contact.retrieve"})
public class RetrieveContact extends HttpServlet {

    private static final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
    //private static final HashMap<String, String> emailHM = new HashMap<String, String>();
    private static String sendEmailList = "";
    private static HashMap<Integer, Contact> contactidContactHM = new HashMap<Integer, Contact>();
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
            throws ServletException, IOException, JWTException {
        response.setContentType("application/JSON;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JsonObject json = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            //String email = "";
            
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
                String teamNameFilter = "";
                if (jobject.has("teamFilter")) {
                    teamNameFilter = jobject.get("teamFilter").getAsString();
                }

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
                ArrayList<TeamJoin> userTeamJoinList = TeamJoinDAO.validTeamJoin(contact.getContactId());
                HashMap<String, String> userTeamJoinHM = new HashMap<String, String>();
                ArrayList<String> teamNameList = new ArrayList<String>();
                
                if (userTeamJoinList != null) {
                    for (TeamJoin teamJoin : userTeamJoinList) {
                        userTeamJoinHM.put(teamJoin.getTeamName(), teamJoin.getTeamName());
                        teamNameList.add(teamJoin.getTeamName());
                    }
                }
                

                //ContactDAO contactDAO = new ContactDAO();
                ArrayList<Contact> contactList = contactDAO.retrieveAllContactWithEmailPhone();
                for(Contact contactTemp:contactList){
                    contactidContactHM.put(contactTemp.getContactId(), contactTemp);
                }

                if (teamNameFilter.isEmpty()) {
                    //contactList = contactDAO.retrieveAllContact();
                    //for(Contact contactTemp:contactList){
                    //    contactidContactHM.put(contactTemp.getContactId(), contactTemp);
                    //}
                } else if (teamNameFilter.equals("my_team")) {
                    contactList = contactDAO.retrieveAllContactInTeams(teamNameList);
                } else if (teamNameFilter.equals("donors")) {
                    contactList = contactDAO.retrieveAllDonor();
                } else if (teamNameFilter.equals("current_members")) {
                    contactList = contactDAO.retrieveAllCurrentMember();
                } else if (teamNameFilter.equals("expired_members")) {
                    contactList = contactDAO.retrieveAllExpiredMember();
                } else {
                    ArrayList<String> tempList = new ArrayList<String>();
                    tempList.add(teamNameFilter);
                    contactList = contactDAO.retrieveAllContactInTeams(tempList);
                }

                if (!contactList.isEmpty() && !contact.isIsNovice()) {

                    json.addProperty("message", "success");

                    if (contact.isIsAdmin()) {
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "teammanager")) {
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "eventleader")) {
                        //emailHM.clear();
                        JsonArray contactArray = retrieveAll(contactList, true);
                        json.add("contact", contactArray);
                    } else if (RoleCheckDAO.checkRole(contact.getContactId(), "associate")) {
                        //emailHM.clear();
                        JsonArray contactArray = retrieveAll(contactList, false);
                        json.add("contact", contactArray);
                    }
                    //Iterator iter = emailHM.keySet().iterator();
                    //while (iter.hasNext()) {
                    //    email += " " + (String) iter.next();
                    //}
                    //json.addProperty("emailList", email);
                    json.addProperty("emailList", sendEmailList);
                    out.println(gson.toJson(json));
                    sendEmailList = "";
                    //emailHM.clear();
                } else {
                    //json.addProperty("message", "fail");
                    json.addProperty("contact", "");
                    json.addProperty("emailList", "");
                    out.println(gson.toJson(json));
                    sendEmailList = "";
                }

            }

        }
    }

    private static JsonArray retrieveAll(ArrayList<Contact> contactList, boolean unlock) {

        JsonArray contactArray = new JsonArray();
        JsonObject jsonContactObj;
        for (Contact c : contactList) {

            //ArrayList<Email> emailList = EmailDAO.retrieveAllEmail(c);
            //ArrayList<Phone> phoneList = PhoneDAO.retrieveAllPhone(c);

            String permissionLevel = "";
            if (c.isIsNovice()) {
                permissionLevel = "Novice";
            } else if (c.isIsAdmin()) {
                permissionLevel = "Admin";
            } else if (RoleCheckDAO.checkRole(c.getContactId(), "teammanager")) {
                permissionLevel = "Team Manager";
            } else if (RoleCheckDAO.checkRole(c.getContactId(), "eventleader")) {
                permissionLevel = "Event Leader";
            } else if (RoleCheckDAO.checkRole(c.getContactId(), "associate")) {
                permissionLevel = "Associate";
            }

            //String emailStr = "";
            //String phoneStr = "";
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

                try {
                    Date todayDate = new Date();
                    Date todayDateWithoutTime = sdf.parse(sdf.format(todayDate));
                    ArrayList<String> emailDisplayList = new ArrayList<String>();
                    for (int i = 0; i < emailList.size(); i++) {
                        Email email = emailList.get(i);
                        Date ObsDateWithoutTime = null;
                        if (email.getDateObsolete() != null) {
                            ObsDateWithoutTime = sdf.parse(sdf.format(email.getDateObsolete()));
                        }
                        if (email.getDateObsolete() != null && !ObsDateWithoutTime.equals(todayDateWithoutTime) && !ObsDateWithoutTime.before(todayDateWithoutTime)) {
                            emailDisplayList.add(email.getEmail());
                        } else if (email.getDateObsolete() == null) {
                            emailDisplayList.add(email.getEmail());
                        }
                    }
                    if (!emailDisplayList.isEmpty()) {
                        for (int i = 0; i < emailDisplayList.size() - 1; i++) {
                            emailStr += emailDisplayList.get(i) + " | ";
                            emailHM.put(c.getName() + "<" + emailDisplayList.get(i) + ">;", c.getName() + "<" + emailDisplayList.get(i) + ">;");
                        }
                        emailStr += emailDisplayList.get(emailDisplayList.size() - 1);
                        emailHM.put(c.getName() + "<" + emailDisplayList.get(emailDisplayList.size() - 1) + ">;", c.getName() + "<" + emailDisplayList.get(emailDisplayList.size() - 1) + ">;");
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(RetrieveContact.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (!phoneList.isEmpty()) {
                try {
                    Date todayDate = new Date();
                    Date todayDateWithoutTime = sdf.parse(sdf.format(todayDate));
                    ArrayList<String> phoneDisplayList = new ArrayList<String>();
                    for (int i = 0; i < phoneList.size(); i++) {
                        Phone phone = phoneList.get(i);
                        Date ObsDateWithoutTime = null;
                        if (phone.getDateObsolete() != null) {
                            ObsDateWithoutTime = sdf.parse(sdf.format(phone.getDateObsolete()));
                        }
                        if (phone.getDateObsolete() != null && !ObsDateWithoutTime.equals(todayDateWithoutTime) && !ObsDateWithoutTime.before(todayDateWithoutTime)) {
                            phoneDisplayList.add("+" + phone.getCountryCode() + "-" + phone.getPhoneNumber());
                        } else if (phone.getDateObsolete() == null) {
                            phoneDisplayList.add("+" + phone.getCountryCode() + "-" + phone.getPhoneNumber());
                        }
                    }
                    if (!phoneDisplayList.isEmpty()) {
                        for (int i = 0; i < phoneDisplayList.size() - 1; i++) {
                            phoneStr += phoneDisplayList.get(i) + " | ";
                        }
                        phoneStr += phoneDisplayList.get(phoneDisplayList.size() - 1);
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(RetrieveContact.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            */

            jsonContactObj = new JsonObject();
            jsonContactObj.addProperty("name", name);
            Contact tempContact = contactidContactHM.get(c.getContactId());
            if (unlock) {
                //jsonContactObj.addProperty("phone", phoneStr);
                if(tempContact.getPhoneStrList() != null){
                    jsonContactObj.addProperty("phone", tempContact.getPhoneStrList());
                }else{
                    jsonContactObj.addProperty("phone", "");
                }
            }
            //jsonContactObj.addProperty("email", emailStr);
            if(tempContact.getEmailStrList() != null){
                    jsonContactObj.addProperty("email", tempContact.getEmailStrList());
            }else{
                jsonContactObj.addProperty("email", "");
            }
            jsonContactObj.addProperty("contact_type", contactType);
            if(c.getUsername() == null){
                jsonContactObj.addProperty("permission_level", " - ");
            } else {
                jsonContactObj.addProperty("permission_level", permissionLevel);
            }
            jsonContactObj.addProperty("cid", c.getContactId());
            if(tempContact.getSendEmailFormat() != null){
                sendEmailList += tempContact.getSendEmailFormat() + "; ";
            }
            contactArray.add(jsonContactObj);

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
        try {
            processRequest(request, response);
        } catch (JWTException ex) {
            Logger.getLogger(RetrieveContact.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RetrieveContact.class.getName()).log(Level.SEVERE, null, ex);
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
