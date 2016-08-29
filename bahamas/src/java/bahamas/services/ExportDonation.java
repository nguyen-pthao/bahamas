/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.AddressDAO;
import bahamas.dao.DonationDAO;
import bahamas.entity.Address;
import bahamas.entity.Donation;
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
@WebServlet(urlPatterns = {"/export.donation"})
public class ExportDonation extends HttpServlet {

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
                
                DonationDAO donationDAO = new DonationDAO();
                ArrayList<Donation> donationList = donationDAO.retrieveAllDonation();

                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat sdft = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                JsonArray donationArray = new JsonArray();

                //start
                if (donationList != null && !donationList.isEmpty()) {

                for (int i = 0; i < donationList.size(); i++) {
                    JsonObject jsonDonationObj = new JsonObject();
                    Donation donation = donationList.get(i);
                    
                   jsonDonationObj.addProperty("cid", donation.getContactId());
                    jsonDonationObj.addProperty("donation_id", donation.getDonationId());
                    if (donation.getDateReceived() != null) {
                        jsonDonationObj.addProperty("date_received", sdf.format(donation.getDateReceived()));
                    } else {
                        jsonDonationObj.addProperty("date_received", "");
                    }
                    jsonDonationObj.addProperty("donation_amount", donation.getDonationAmount());
                    jsonDonationObj.addProperty("payment_mode", donation.getPaymentMode());

                    if (donation.getExplainIfOtherPayment() != null) {
                        jsonDonationObj.addProperty("explain_if_other_payment", donation.getExplainIfOtherPayment());
                    } else {
                        jsonDonationObj.addProperty("explain_if_other_payment", "");
                    }
                    if (donation.getExtTransactionRef() != null) {
                        jsonDonationObj.addProperty("ext_transaction_ref", donation.getExtTransactionRef());
                    } else {
                        jsonDonationObj.addProperty("ext_transaction_ref", "");
                    }
                    if (donation.getReceiptNumber() != null) {
                        jsonDonationObj.addProperty("receipt_number", donation.getReceiptNumber());
                    } else {
                        jsonDonationObj.addProperty("receipt_number", "");
                    }
                    if (donation.getReceiptDate() != null) {
                        jsonDonationObj.addProperty("receipt_date", sdf.format(donation.getReceiptDate()));
                    } else {
                        jsonDonationObj.addProperty("receipt_date", "");
                    }
                    jsonDonationObj.addProperty("receipt_mode_name", donation.getReceiptMode());
                    if (donation.getExplainIfOtherReceipt() != null) {
                        jsonDonationObj.addProperty("explain_if_other_receipt", donation.getExplainIfOtherReceipt());
                    } else {
                        jsonDonationObj.addProperty("explain_if_other_receipt", "");
                    }
                    if (donation.getDonorInstructions() != null) {
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
                    if (donation.getAssociatedOccasion() != null) {
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
                    donationArray.add(jsonDonationObj);
                    
                }
                json.addProperty("message", "success");
                    json.add("list", donationArray);
                } else {

                    json.addProperty("message", "donation table is empty");
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
