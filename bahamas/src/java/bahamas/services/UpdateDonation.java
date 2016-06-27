/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AppreciationDAO;
import bahamas.dao.ContactDAO;
import bahamas.dao.DonationDAO;
import bahamas.dao.RoleCheckDAO;
import bahamas.entity.Appreciation;
import bahamas.entity.Contact;
import bahamas.entity.Donation;
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

/**
 *
 * @author HUXLEY
 */
@WebServlet(name = "UpdateDonation", urlPatterns = {"/donation.update"})
public class UpdateDonation extends HttpServlet {

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

                    ContactDAO cDAO = new ContactDAO();

                    //Verified token
                    int contactId = Validator.isIntValid(jobject.get("contact_id").getAsString());
                    Contact c = cDAO.retrieveContactById(contactId);

                    if (c == null) {
                        json.addProperty("message", "fail");
                        out.println(gson.toJson(json));
                        return;
                    } else {

                        Contact user = cDAO.retrieveContactByUsername(username);
                        String userType = Validator.containsBlankField(jobject.get("user_type").getAsString());
                        if (!user.isIsAdmin() && !userType.equals("teammanager")
                                && !RoleCheckDAO.checkRole(user.getContactId(), userType)) {
                            json.addProperty("message", "fail");
                            out.println(gson.toJson(json));
                            return;
                        }
                        
                        Date dateReceived = Validator.isDateValid(jobject.get("date_received").getAsString());
                        double donationAmount = Validator.isDoubleValid(jobject.get("donation_amount").getAsString());
                        String paymentMode = Validator.containsBlankField(jobject.get("payment_mode").getAsString());
                        String explainIfOtherPayment = Validator.containsBlankField(jobject.get("explain_if_other_payment").getAsString());
                        String extTransactionRef = Validator.containsBlankField(jobject.get("ext_transaction_ref").getAsString());
                        String receiptNumber = Validator.containsBlankField(jobject.get("receipt_number").getAsString());
                        Date receiptDate = Validator.isDateValid(jobject.get("receipt_date").getAsString());
                        String receiptMode = Validator.containsBlankField(jobject.get("receipt_mode").getAsString());
                        String explainIfOtherReceipt = Validator.containsBlankField(jobject.get("explain_if_other_receipt").getAsString());
                        String donorInstruction = Validator.containsBlankField(jobject.get("donor_instruction").getAsString());
                        String allocation1 = Validator.containsBlankField(jobject.get("allocation1").getAsString());
                        double subamount1 = Validator.isDoubleValid(jobject.get("subamount1").getAsString());
                        String allocation2 = Validator.containsBlankField(jobject.get("allocation2").getAsString());
                        double subamount2 = Validator.isDoubleValid(jobject.get("subamount2").getAsString());
                        String allocation3 = Validator.containsBlankField(jobject.get("allocation3").getAsString());
                        double subamount3 = Validator.isDoubleValid(jobject.get("subamount3").getAsString());
                        String associatedOccasion = Validator.containsBlankField(jobject.get("associated_occasion").getAsString());
                        String remarks = Validator.containsBlankField(jobject.get("remarks").getAsString());

                        Donation d = new Donation(c, username, dateReceived, donationAmount, paymentMode, explainIfOtherPayment,
                                extTransactionRef, receiptMode, receiptNumber, receiptDate, explainIfOtherReceipt, donorInstruction, allocation1, subamount1,
                                allocation2, subamount2, allocation3, subamount3, associatedOccasion, remarks);

                        int donationId = Validator.isIntValid(jobject.get("donation_id").getAsString());
                        d.setDonationId(donationId);

                        if (DonationDAO.updateDonation(d)) {
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
