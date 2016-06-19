/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Donation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HUXLEY
 */
public class DonationDAO {
    
    public static boolean addDonation(Donation d) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO DONATION (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,DATE_RECEIVED,DONATION_AMOUNT,PAYMENT_MODE_NAME,"
                    + "EXPLAIN_IF_OTHER_PAYMENT,EXT_TRANSACTION_REF,RECEIPT_NUMBER,RECEIPT_DATE,"
                    + "RECEIPT_MODE_NAME,EXPLAIN_IF_OTHER_RECEIPT,DONOR_INSTRUCTIONS,ALLOCATION_1,SUBAMOUNT_1,"
                    + "ALLOCATION_2,SUBAMOUNT_2,ALLOCATION_3,SUBAMOUNT_3,ASSOCIATED_OCCASION,REMARKS)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, d.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(d.getDateCreated().getTime()));
            stmt.setString(3, d.getCreatedBy());        
            if (d.getDateReceived() != null) {
                stmt.setDate(4, new java.sql.Date(d.getDateReceived().getTime()));
            } else {
                stmt.setDate(4, null);
            }
            stmt.setDouble(5, d.getDonationAmount());
            stmt.setString(6,d.getPaymentMode());
            stmt.setString(7,d.getExplainIfOtherPayment());
            stmt.setString(8,d.getExtTransactionRef());
            stmt.setString(9,d.getReceiptNumber());
            if (d.getReceiptDate()!= null) {
                stmt.setDate(10, new java.sql.Date(d.getReceiptDate().getTime()));
            } else {
                stmt.setDate(10, null);
            }
            stmt.setString(11,d.getReceiptMode());
            stmt.setString(12,d.getExplainIfOtherReceipt());
            stmt.setString(13,d.getDonorInstructions());
            stmt.setString(14,d.getAllocation1());
            stmt.setDouble(15, d.getSubAmount1());
            stmt.setString(16,d.getAllocation2());
            stmt.setDouble(17,d.getSubAmount2());
            stmt.setString(18,d.getAllocation3());
            stmt.setDouble(19, d.getSubAmount3());
            stmt.setString(20,d.getAssociatedOccasion());
            stmt.setString(21, d.getRemarks());
                          
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
}
