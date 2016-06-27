/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateMembership", urlPatterns = {"/membership.update"})
public class UpdateMembership extends HttpServlet {

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
                    int contactId = Validator.isIntValid(jobject.get("contact_id").getAsString());
                    ContactDAO cDAO = new ContactDAO();

                    Contact c = cDAO.retrieveContactById(contactId);

                    if (c == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {

                        Contact user = cDAO.retrieveContactByUsername(username);
                        String userType = Validator.containsBlankField(jobject.get("user_type").getAsString());
                        if (!user.isIsAdmin() || !(userType.equals("teammanager")
                                && RoleCheckDAO.checkRole(user.getContactId(), userType))) {
                            json.addProperty("message", "fail");
                            out.println(gson.toJson(json));
                            return;
                        }

                        int membershipId = Validator.isIntValid(jobject.get("membership_id").getAsString());
                        String membershipClass = Validator.containsBlankField(jobject.get("membership_class").getAsString());
                        String explainIfOtherClass = Validator.containsBlankField(jobject.get("explain_if_other_class").getAsString());
                        Date startMembership = Validator.isDateValid(jobject.get("start_membership").getAsString());
                        Date endMembership = Validator.isDateValid(jobject.get("end_membership").getAsString());
                        double subscriptionAmount = Validator.isDoubleValid(jobject.get("subscription_amount").getAsString());
                        String paymentMode = Validator.containsBlankField(jobject.get("payment_mode").getAsString());
                        String explainIfOtherPayment = Validator.containsBlankField(jobject.get("explain_if_other_payment").getAsString());
                        String extTransactionRef = Validator.containsBlankField(jobject.get("ext_transaction_ref").getAsString());
                        String receiptNumber = Validator.containsBlankField(jobject.get("receipt_number").getAsString());
                        Date receiptDate = Validator.isDateValid(jobject.get("receipt_date").getAsString());
                        String receiptMode = Validator.containsBlankField(jobject.get("receipt_mode").getAsString());
                        String explainIfOtherReceipt = Validator.containsBlankField(jobject.get("explain_if_other_receipt").getAsString());
                        String remarks = Validator.containsBlankField(jobject.get("remarks").getAsString());

                        Membership m = new Membership(c, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef,
                                receiptNumber, remarks, receiptMode, explainIfOtherReceipt, membershipClass, explainIfOtherClass, paymentMode, explainIfOtherPayment,
                                username);

                        m.setMembershipId(membershipId);

                        if (MembershipDAO.updateMembership(m)) {
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
