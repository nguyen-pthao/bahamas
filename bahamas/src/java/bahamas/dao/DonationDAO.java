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
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            stmt.setString(6, d.getPaymentMode());
            stmt.setString(7, d.getExplainIfOtherPayment());
            stmt.setString(8, d.getExtTransactionRef());
            stmt.setString(9, d.getReceiptNumber());
            if (d.getReceiptDate() != null) {
                stmt.setDate(10, new java.sql.Date(d.getReceiptDate().getTime()));
            } else {
                stmt.setDate(10, null);
            }
            stmt.setString(11, d.getReceiptMode());
            stmt.setString(12, d.getExplainIfOtherReceipt());
            stmt.setString(13, d.getDonorInstructions());
            stmt.setString(14, d.getAllocation1());
            stmt.setDouble(15, d.getSubAmount1());
            stmt.setString(16, d.getAllocation2());
            stmt.setDouble(17, d.getSubAmount2());
            stmt.setString(18, d.getAllocation3());
            stmt.setDouble(19, d.getSubAmount3());
            stmt.setString(20, d.getAssociatedOccasion());
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

    public static ArrayList<Donation> retrieveDonationByCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Donation> donationList;
        donationList = new ArrayList<Donation>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT DONATION_ID, DATE_CREATED, CREATED_BY, DATE_RECEIVED, "
                    + "DONATION_AMOUNT, PAYMENT_MODE_NAME, EXPLAIN_IF_OTHER_PAYMENT, EXT_TRANSACTION_REF, "
                    + "RECEIPT_MODE_NAME, RECEIPT_NUMBER, RECEIPT_DATE, EXPLAIN_IF_OTHER_RECEIPT, "
                    + "DONOR_INSTRUCTIONS, ALLOCATION_1, SUBAMOUNT_1, ALLOCATION_2, SUBAMOUNT_2, "
                    + "ALLOCATION_3, SUBAMOUNT_3, ASSOCIATED_OCCASION, REMARKS FROM DONATION "
                    + "WHERE CONTACT_ID = (?) ");

            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int donationId = rs.getInt(1);
                Date dateCreated = rs.getTimestamp(2);
                String createdBy = rs.getString(3);
                Date dateReceived = rs.getDate(4);
                double donationAmount = rs.getDouble(5);
                String paymentMode = rs.getString(6);
                String explainIfOtherPayment = rs.getString(7);
                String extTransactionRef = rs.getString(8);
                String receiptMode = rs.getString(9);
                String receiptNumber = rs.getString(10);
                Date receiptDate = rs.getDate(11);
                String explainIfOtherReceipt = rs.getString(12);
                String donorInstructions = rs.getString(13);
                String allocation1 = rs.getString(14);
                double subAmount1 = rs.getDouble(15);
                String allocation2 = rs.getString(16);
                double subAmount2 = rs.getDouble(17);
                String allocation3 = rs.getString(18);
                double subAmount3 = rs.getDouble(19);
                String associatedOccasion = rs.getString(20);
                String remarks = rs.getString(21);

                Donation donation = new Donation(donationId, dateCreated, createdBy, dateReceived, donationAmount, paymentMode, explainIfOtherPayment, extTransactionRef, receiptMode, receiptNumber, receiptDate, explainIfOtherReceipt, donorInstructions, allocation1, subAmount1, allocation2, subAmount2, allocation3, subAmount3, associatedOccasion, remarks);
                donationList.add(donation);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve DONATION from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return donationList;
    }

    public static boolean updateDonation(Donation d) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE DONATION SET CONTACT_ID=?,"
                    + "DATE_RECEIVED=?,DONATION_AMOUNT=?,PAYMENT_MODE_NAME=?,"
                    + "EXPLAIN_IF_OTHER_PAYMENT=?,EXT_TRANSACTION_REF=?,RECEIPT_NUMBER=?,RECEIPT_DATE=?,"
                    + "RECEIPT_MODE_NAME=?,EXPLAIN_IF_OTHER_RECEIPT=?,DONOR_INSTRUCTIONS=?,ALLOCATION_1=?,SUBAMOUNT_1=?,"
                    + "ALLOCATION_2=?,SUBAMOUNT_2=?,ALLOCATION_3=?,SUBAMOUNT_3=?,ASSOCIATED_OCCASION=?,REMARKS=? WHERE "
                    + "DONATION_ID=?");

            stmt.setInt(1, d.getContact().getContactId());
            if (d.getDateReceived() != null) {
                stmt.setDate(2, new java.sql.Date(d.getDateReceived().getTime()));
            } else {
                stmt.setDate(2, null);
            }
            stmt.setDouble(3, d.getDonationAmount());
            stmt.setString(4, d.getPaymentMode());
            stmt.setString(5, d.getExplainIfOtherPayment());
            stmt.setString(6, d.getExtTransactionRef());
            stmt.setString(7, d.getReceiptNumber());
            if (d.getReceiptDate() != null) {
                stmt.setDate(8, new java.sql.Date(d.getReceiptDate().getTime()));
            } else {
                stmt.setDate(8, null);
            }
            stmt.setString(9, d.getReceiptMode());
            stmt.setString(10, d.getExplainIfOtherReceipt());
            stmt.setString(11, d.getDonorInstructions());
            stmt.setString(12, d.getAllocation1());
            stmt.setDouble(13, d.getSubAmount1());
            stmt.setString(14, d.getAllocation2());
            stmt.setDouble(15, d.getSubAmount2());
            stmt.setString(16, d.getAllocation3());
            stmt.setDouble(17, d.getSubAmount3());
            stmt.setString(18, d.getAssociatedOccasion());
            stmt.setString(19, d.getRemarks());
            stmt.setInt(20, d.getDonationId());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteDonation(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM DONATION WHERE "
                    + "DONATION_ID=?");
           
            stmt.setInt(1, id);

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
