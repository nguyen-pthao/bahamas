/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AuditLogDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.DonationDAO;
import bahamas.dao.MembershipDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Contact;
import bahamas.entity.Donation;
import bahamas.entity.Membership;
import bahamas.util.Authenticator;
import bahamas.util.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import is203.JWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author HUXLEY
 */
@WebServlet(name = "AddMembership", urlPatterns = {"/membership.add"})
public class AddMembership extends HttpServlet {

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
            throws ServletException, IOException, JWTException {
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

                String token = Validator.containsBlankField(jobject.get("token"));
                String username = Authenticator.verifyToken(token);

                if (username == null) {
                    json.addProperty("message", "invalid token");
                    out.println(gson.toJson(json));

                } else {
                    //Verified token
                    int contactId = Validator.isIntValid(jobject.get("contact_id"));
                    ContactDAO cDAO = new ContactDAO();

                    Contact user = cDAO.retrieveContactByUsername(username);
                    String userType = Validator.containsBlankField(jobject.get("user_type"));
                    if (!user.isIsAdmin() && !userType.equals("teammanager") && !RoleCheckDAO.checkRole(user.getContactId(), userType)) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    }

                    Contact c = cDAO.retrieveContactById(contactId);

                    if (c == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {

                        String membershipClass = Validator.containsBlankField(jobject.get("membership_class"));
                        String explainIfOtherClass = Validator.containsBlankField(jobject.get("explain_if_other_class"));
                        Date startMembership = Validator.isDateValid(jobject.get("start_membership"), "start membership");
                        Date endMembership = Validator.isDateValid(jobject.get("end_membership"), "end membership");
                        double subscriptionAmount = Validator.isDoubleValid(jobject.get("subscription_amount"));
                        String paymentMode = Validator.containsBlankField(jobject.get("payment_mode"));
                        String explainIfOtherPayment = Validator.containsBlankField(jobject.get("explain_if_other_payment"));
                        String extTransactionRef = Validator.containsBlankField(jobject.get("ext_transaction_ref"));
                        String receiptNumber = Validator.containsBlankField(jobject.get("receipt_number"));
                        Date receiptDate = Validator.isDateValid(jobject.get("receipt_date"), "receipt date");
                        String receiptMode = Validator.containsBlankField(jobject.get("receipt_mode"));
                        String explainIfOtherReceipt = Validator.containsBlankField(jobject.get("explain_if_other_receipt"));
                        String remarks = Validator.containsBlankField(jobject.get("remarks"));

                        if (startMembership != null && endMembership != null) {
                            if (endMembership.before(startMembership) || !startMembership.before(endMembership)) {
                                Validator.getErrorList().add("Start office date must be before end office date");
                            }
                        }

                        if (MembershipDAO.membershipExist(contactId, startMembership, endMembership)) {
                            Validator.getErrorList().add("Membership already exists");
                        }

                        if (!Validator.getErrorList().isEmpty()) {
                            JsonArray errorArray = new JsonArray();
                            for (String s : Validator.getErrorList()) {
                                JsonPrimitive o = new JsonPrimitive(s);
                                errorArray.add(o);
                            }
                            Validator.getErrorList().clear();
                            json.add("message", errorArray);
                            out.println(gson.toJson(json));
                            return;
                        }

                        Membership m = new Membership(c, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef,
                                receiptNumber, remarks, receiptMode, explainIfOtherReceipt, membershipClass, explainIfOtherClass, paymentMode, explainIfOtherPayment,
                                username);

                        if (MembershipDAO.addMembership(m)) {
                            AuditLogDAO.insertAuditLog(username, "ADD MEMBERSHIP", "Add membership under contact: Contact ID: " + c.getContactId());
                            json.addProperty("message", "success");
                            out.println(gson.toJson(json));
                        } else {
                            json.addProperty("message", "failure insert into system");
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
        try {
            processRequest(request, response);
        } catch (JWTException ex) {
            Logger.getLogger(AddMembership.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddMembership.class.getName()).log(Level.SEVERE, null, ex);
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
