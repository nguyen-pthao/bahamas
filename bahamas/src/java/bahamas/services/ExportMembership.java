/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.services;

import bahamas.dao.MembershipDAO;
import bahamas.entity.Membership;
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
@WebServlet(name = "ExportMembership", urlPatterns = {"/export.membership"})
public class ExportMembership extends HttpServlet {

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
                
                MembershipDAO membershipDAO = new MembershipDAO();
                ArrayList<Membership> aList = membershipDAO.retrieveAllMembership();

                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat sdft = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                JsonArray membershipArray = new JsonArray();

                //start
                if (aList != null && !aList.isEmpty()) {

                    for (int i = 0; i < aList.size(); i++) {
                        JsonObject jsonMembershipObj = new JsonObject();
                        Membership membership = aList.get(i);

                        if (membership.getCid() > 0) {
                            jsonMembershipObj.addProperty("cid", membership.getCid());
                        } else {
                            jsonMembershipObj.addProperty("cid", "");
                        }

                        jsonMembershipObj.addProperty("membership", membership.getMembershipId());

                        
                        if (membership.getStartMembership()!= null) {
                            jsonMembershipObj.addProperty("start_membership", sdf.format(membership.getStartMembership()));
                        } else {
                            jsonMembershipObj.addProperty("start_membership", "");
                        }
                        if (membership.getEndMembership() != null) {
                            jsonMembershipObj.addProperty("end_membership", sdf.format(membership.getEndMembership()));
                        } else {
                            jsonMembershipObj.addProperty("end_membership", "");
                        }
                        if (membership.getMembershipClassName()!= null) {
                            jsonMembershipObj.addProperty("membership_class", membership.getMembershipClassName());
                        } else {
                            jsonMembershipObj.addProperty("membership_class", "");
                        }
                        if (membership.getExplainIfOtherClass()!= null) {
                            jsonMembershipObj.addProperty("explain_if_other_class", membership.getMembershipClassName());
                        } else {
                            jsonMembershipObj.addProperty("explain_if_other_class", "");
                        }
                        if (membership.getPaymentModeName()!= null) {
                            jsonMembershipObj.addProperty("payment_mode", membership.getPaymentModeName());
                        } else {
                            jsonMembershipObj.addProperty("payment_mode", "");
                        }
                        if (membership.getExplainIfOtherPayment()!= null) {
                            jsonMembershipObj.addProperty("explain_if_other_payment", membership.getExplainIfOtherPayment());
                        } else {
                            jsonMembershipObj.addProperty("explain_if_other_payment", "");
                        }
                        if (membership.getReceiptDate()!= null) {
                            jsonMembershipObj.addProperty("receipt_date", sdf.format(membership.getReceiptDate()));
                        } else {
                            jsonMembershipObj.addProperty("receipt_date", "");
                        }
                        if (membership.getSubscriptionAmount() >0 ) {
                            jsonMembershipObj.addProperty("subscription_amount", membership.getSubscriptionAmount());
                        } else {
                            jsonMembershipObj.addProperty("subscription_amount", "");
                        }
                        if (membership.getExtTransactionRef()!= null) {
                            jsonMembershipObj.addProperty("ext_transaction_ref", membership.getExtTransactionRef());
                        } else {
                            jsonMembershipObj.addProperty("ext_transaction_ref", "");
                        }
                        if (membership.getReceiptNumber()!= null) {
                            jsonMembershipObj.addProperty("receipt_number", membership.getReceiptNumber());
                        } else {
                            jsonMembershipObj.addProperty("receipt_number", "");
                        }
                        if (membership.getRemarks() != null) {
                            jsonMembershipObj.addProperty("remarks", membership.getRemarks());
                        } else {
                            jsonMembershipObj.addProperty("remarks", "");
                        }
                        if (membership.getReceiptModeName()!= null) {
                            jsonMembershipObj.addProperty("receipt_mode", membership.getReceiptModeName());
                        } else {
                            jsonMembershipObj.addProperty("receipt_mode", "");
                        }
                        if (membership.getExplainIfOtherReceipt()!= null) {
                            jsonMembershipObj.addProperty("explain_if_other_receipt", membership.getExplainIfOtherReceipt());
                        } else {
                            jsonMembershipObj.addProperty("explain_if_other_receipt", "");
                        }
                        jsonMembershipObj.addProperty("created_by", membership.getCreatedBy());
                        jsonMembershipObj.addProperty("date_created", sdft.format(membership.getDateCreated()));
                        membershipArray.add(jsonMembershipObj);

                    }
                    json.addProperty("message", "success");
                    json.add("membershiplist", membershipArray);
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
