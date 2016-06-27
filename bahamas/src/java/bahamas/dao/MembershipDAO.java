/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.entity.Membership;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tan.si.hao
 */
public class MembershipDAO {

    public MembershipDAO() {
    }

    public static ArrayList<Membership> retrieveAllMembership() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Membership> membershipList;
        membershipList = new ArrayList<Membership>();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT MEMBERSHIP_ID,CONTACT_ID, START_MEMBERSHIP, END_MEMBERSHIP,"
                    + "RECEIPT_DATE,SUBSCRIPTION_AMOUNT, EXT_TRANSACTION_REF, RECEIPT_NUMBER,"
                    + "REMARKS, RECEIPT_MODE_NAME, EXPLAIN_IF_OTHER_RECEIPT, MEMBERSHIP_CLASS_NAME, EXPLAIN_IF_OTHER_CLASS,"
                    + "PAYMENT_MODE_NAME, EXPLAIN_IF_OTHER_PAYMENT, CREATED_BY, DATE_CREATED "
                    + "FROM MEMBERSHIP");

            rs = stmt.executeQuery();
            while (rs.next()) {
                int mid = rs.getInt(1);
                int cid = rs.getInt(2);
                Date startMembership = rs.getDate(3);
                Date endMembership = rs.getDate(4);
                Date receiptDate = rs.getDate(5);
                double subscriptionAmount = rs.getDouble(6);
                String extTransactionRef = rs.getString(7);
                String receiptNumber = rs.getString(8);
                String remarks = rs.getString(9);
                String receiptModeName = rs.getString(10);
                String explainIfOtherMode = rs.getString(11);
                String membershipClassName = rs.getString(12);
                String explainIfOtherClass = rs.getString(13);
                String paymentModeName = rs.getString(14);
                String explainIfOtherPayment = rs.getString(15);
                String createdBy = rs.getString(16);
                Date dateCreated = rs.getTimestamp(17);

                Membership membership = new Membership(mid, cid, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef, receiptNumber, remarks, receiptModeName, explainIfOtherMode, membershipClassName, explainIfOtherClass, paymentModeName, explainIfOtherPayment, createdBy, dateCreated);
                membershipList.add(membership);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve MEMBERSHIP from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return membershipList;
    }

    public static ArrayList<Membership> retrieveMembershipByCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Membership> membershipList;
        membershipList = new ArrayList<Membership>();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT MEMBERSHIP_ID,CONTACT_ID, START_MEMBERSHIP, END_MEMBERSHIP,"
                    + "RECEIPT_DATE,SUBSCRIPTION_AMOUNT, EXT_TRANSACTION_REF, RECEIPT_NUMBER,"
                    + "REMARKS, RECEIPT_MODE_NAME, EXPLAIN_IF_OTHER_RECEIPT, MEMBERSHIP_CLASS_NAME, EXPLAIN_IF_OTHER_CLASS,"
                    + "PAYMENT_MODE_NAME, EXPLAIN_IF_OTHER_PAYMENT, CREATED_BY, DATE_CREATED "
                    + "FROM MEMBERSHIP WHERE CONTACT_ID = (?)");
            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int mid = rs.getInt(1);
                cid = rs.getInt(2);
                Date startMembership = rs.getDate(3);
                Date endMembership = rs.getDate(4);
                Date receiptDate = rs.getDate(5);
                double subscriptionAmount = rs.getDouble(6);
                String extTransactionRef = rs.getString(7);
                String receiptNumber = rs.getString(8);
                String remarks = rs.getString(9);
                String receiptModeName = rs.getString(10);
                String explainIfOtherMode = rs.getString(11);
                String membershipClassName = rs.getString(12);
                String explainIfOtherClass = rs.getString(13);
                String paymentModeName = rs.getString(14);
                String explainIfOtherPayment = rs.getString(15);
                String createdBy = rs.getString(16);
                Date dateCreated = rs.getTimestamp(17);

                Membership membership = new Membership(mid, cid, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef, receiptNumber, remarks, receiptModeName, explainIfOtherMode, membershipClassName, explainIfOtherClass, paymentModeName, explainIfOtherPayment, createdBy, dateCreated);
                membershipList.add(membership);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve MEMBERSHIP from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return membershipList;
    }

    public static ArrayList<Membership> retrieveMembershipByUsername(String username) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Membership> membershipList;
        membershipList = new ArrayList<Membership>();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT M.CONTACT_ID, START_MEMBERSHIP, END_MEMBERSHIP,RECEIPT_DATE,SUBSCRIPTION_AMOUNT, EXT_TRANSACTION_REF, RECEIPT_NUMBER,M.REMARKS, RECEIPT_MODE_NAME, EXPLAIN_IF_OTHER_RECEIPT, MEMBERSHIP_CLASS_NAME, EXPLAIN_IF_OTHER_CLASS,PAYMENT_MODE_NAME, EXPLAIN_IF_OTHER_PAYMENT, M.CREATED_BY, M.DATE_CREATED FROM MEMBERSHIP M, CONTACT C WHERE M.CONTACT_ID = C.CONTACT_ID AND USERNAME = (?)");
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int mid = rs.getInt(1);
                int cid = rs.getInt(2);
                Date startMembership = rs.getDate(3);
                Date endMembership = rs.getDate(4);
                Date receiptDate = rs.getDate(5);
                double subscriptionAmount = rs.getDouble(6);
                String extTransactionRef = rs.getString(7);
                String receiptNumber = rs.getString(8);
                String remarks = rs.getString(9);
                String receiptModeName = rs.getString(10);
                String explainIfOtherMode = rs.getString(11);
                String membershipClassName = rs.getString(12);
                String explainIfOtherClass = rs.getString(13);
                String paymentModeName = rs.getString(14);
                String explainIfOtherPayment = rs.getString(15);
                String createdBy = rs.getString(16);
                Date dateCreated = rs.getTimestamp(17);
                
                Membership membership = new Membership(mid,cid, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef, receiptNumber, remarks, receiptModeName, explainIfOtherMode, membershipClassName, explainIfOtherClass, paymentModeName, explainIfOtherPayment, createdBy, dateCreated);
                membershipList.add(membership);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve MEMBERSHIP from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return membershipList;
    }

    public static boolean addMembership(Membership m) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO MEMBERSHIP (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,EXPLAIN_IF_OTHER_CLASS,START_MEMBERSHIP,END_MEMBERSHIP,"
                    + "SUBSCRIPTION_AMOUNT,EXPLAIN_IF_OTHER_PAYMENT,EXT_TRANSACTION_REF,RECEIPT_NUMBER,RECEIPT_DATE,"
                    + "EXPLAIN_IF_OTHER_RECEIPT,RECEIPT_MODE_NAME,MEMBERSHIP_CLASS_NAME,PAYMENT_MODE_NAME,REMARKS)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, m.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(m.getDateCreated().getTime()));
            stmt.setString(3, m.getCreatedBy());
            stmt.setString(4, m.getExplainIfOtherClass());

            if (m.getStartMembership() != null) {
                stmt.setDate(5, new java.sql.Date(m.getStartMembership().getTime()));
            } else {
                stmt.setDate(5, null);
            }

            if (m.getEndMembership() != null) {
                stmt.setDate(6, new java.sql.Date(m.getEndMembership().getTime()));
            } else {
                stmt.setDate(6, null);
            }

            stmt.setDouble(7, m.getSubscriptionAmount());
            stmt.setString(8, m.getExplainIfOtherPayment());
            stmt.setString(9, m.getExtTransactionRef());
            stmt.setString(10, m.getReceiptNumber());

            if (m.getReceiptDate() != null) {
                stmt.setDate(11, new java.sql.Date(m.getReceiptDate().getTime()));
            } else {
                stmt.setDate(11, null);
            }

            stmt.setString(12, m.getExplainIfOtherReceipt());
            stmt.setString(13, m.getReceiptModeName());
            stmt.setString(14, m.getMembershipClassName());
            stmt.setString(15, m.getPaymentModeName());
            stmt.setString(16, m.getRemarks());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean updateMembership(Membership m) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE MEMBERSHIP SET CONTACT_ID=?,EXPLAIN_IF_OTHER_CLASS=?,START_MEMBERSHIP=?,END_MEMBERSHIP=?,"
                    + "SUBSCRIPTION_AMOUNT=?,EXPLAIN_IF_OTHER_PAYMENT=?,EXT_TRANSACTION_REF=?,RECEIPT_NUMBER=?,RECEIPT_DATE=?,"
                    + "EXPLAIN_IF_OTHER_RECEIPT=?,RECEIPT_MODE_NAME=?,MEMBERSHIP_CLASS_NAME=?,PAYMENT_MODE_NAME=?,REMARK=? "
                    + "WHERE MEMBERSHIP_ID=?");

            stmt.setInt(1, m.getContact().getContactId());
            stmt.setString(2, m.getExplainIfOtherClass());

            if (m.getStartMembership() != null) {
                stmt.setDate(3, new java.sql.Date(m.getStartMembership().getTime()));
            } else {
                stmt.setDate(3, null);
            }

            if (m.getEndMembership() != null) {
                stmt.setDate(4, new java.sql.Date(m.getEndMembership().getTime()));
            } else {
                stmt.setDate(4, null);
            }

            stmt.setDouble(5, m.getSubscriptionAmount());
            stmt.setString(6, m.getExplainIfOtherPayment());
            stmt.setString(7, m.getExtTransactionRef());
            stmt.setString(8, m.getReceiptNumber());

            if (m.getReceiptDate() != null) {
                stmt.setDate(9, new java.sql.Date(m.getReceiptDate().getTime()));
            } else {
                stmt.setDate(9, null);
            }

            stmt.setString(10, m.getExplainIfOtherReceipt());
            stmt.setString(11, m.getReceiptModeName());
            stmt.setString(12, m.getMembershipClassName());
            stmt.setString(13, m.getPaymentModeName());
            stmt.setString(14, m.getRemarks());
            stmt.setInt(15, m.getMembershipId());
      

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteMembership(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM MEMBERSHIP WHERE MEMBERSHIP_ID=?");

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
