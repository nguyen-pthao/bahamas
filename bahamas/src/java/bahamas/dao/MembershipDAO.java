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
            stmt = conn.prepareStatement("SELECT CONTACT_ID, START_MEMBERSHIP, END_MEMBERSHIP,"
                    + "RECEIPT_DATE,SUBSCRIPTION_AMOUNT, EXT_TRANSACTION_REF, RECEIPT_NUMBER,"
                    + "REMARKS, RECEIPT_MODE_NAME, EXPLAIN_IF_OTHER_RECEIPT, MEMBERSHIP_CLASS_NAME, EXPLAIN_IF_OTHER_CLASS,"
                    + "PAYMENT_MODE_NAME, EXPLAIN_IF_OTHER_PAYMENT, CREATED_BY, DATE_CREATED "
                    + "FROM MEMBERSHIP");

            rs = stmt.executeQuery();
            while (rs.next()) {

                int cid = rs.getInt(1);
                Date startMembership = rs.getDate(2);              
                Date endMembership = rs.getDate(3);
                Date receiptDate = rs.getDate(4);
                double subscriptionAmount = rs.getDouble(5);
                String extTransactionRef = rs.getString(6);
                String receiptNumber = rs.getString(7);
                String remarks = rs.getString(8);
                String receiptModeName = rs.getString(9);
                String explainIfOtherMode = rs.getString(10);
                String membershipClassName = rs.getString(11);
                String explainIfOtherClass = rs.getString(12);
                String paymentModeName = rs.getString(13);
                String explainIfOtherPayment = rs.getString(14);
                String createdBy = rs.getString(15);
                Date dateCreated = rs.getTimestamp(16);

                Membership membership = new Membership(cid, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef, receiptNumber, remarks, receiptModeName, explainIfOtherMode, membershipClassName, explainIfOtherClass, paymentModeName, explainIfOtherPayment, createdBy, dateCreated);
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
            stmt = conn.prepareStatement("SELECT CONTACT_ID, START_MEMBERSHIP, END_MEMBERSHIP,"
                    + "RECEIPT_DATE,SUBSCRIPTION_AMOUNT, EXT_TRANSACTION_REF, RECEIPT_NUMBER,"
                    + "REMARKS, RECEIPT_MODE_NAME, EXPLAIN_IF_OTHER_RECEIPT, MEMBERSHIP_CLASS_NAME, EXPLAIN_IF_OTHER_CLASS,"
                    + "PAYMENT_MODE_NAME, EXPLAIN_IF_OTHER_PAYMENT, CREATED_BY, DATE_CREATED "
                    + "FROM MEMBERSHIP WHERE CONTACT_ID = (?)");
            stmt.setInt(1, cid);
            
            rs = stmt.executeQuery();
            while (rs.next()) {

                cid = rs.getInt(1);
                Date startMembership = rs.getDate(2);              
                Date endMembership = rs.getDate(3);
                Date receiptDate = rs.getDate(4);
                double subscriptionAmount = rs.getDouble(5);
                String extTransactionRef = rs.getString(6);
                String receiptNumber = rs.getString(7);
                String remarks = rs.getString(8);
                String receiptModeName = rs.getString(9);
                String explainIfOtherMode = rs.getString(10);
                String membershipClassName = rs.getString(11);
                String explainIfOtherClass = rs.getString(12);
                String paymentModeName = rs.getString(13);
                String explainIfOtherPayment = rs.getString(14);
                String createdBy = rs.getString(15);
                Date dateCreated = rs.getTimestamp(16);

                Membership membership = new Membership(cid, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef, receiptNumber, remarks, receiptModeName, explainIfOtherMode, membershipClassName, explainIfOtherClass, paymentModeName, explainIfOtherPayment, createdBy, dateCreated);
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
    
    //SELECT USERNAME, M.CONTACT_ID, START_MEMBERSHIP, END_MEMBERSHIP,RECEIPT_DATE,SUBSCRIPTION_AMOUNT, EXT_TRANSACTION_REF, RECEIPT_NUMBER,M.REMARKS, RECEIPT_MODE_NAME, EXPLAIN_IF_OTHER_RECEIPT, MEMBERSHIP_CLASS_NAME, EXPLAIN_IF_OTHER_CLASS,PAYMENT_MODE_NAME, EXPLAIN_IF_OTHER_PAYMENT, M.CREATED_BY, M.DATE_CREATED FROM MEMBERSHIP M, CONTACT C WHERE M.CONTACT_ID = C.CONTACT_ID AND USERNAME = "username1"
    
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

                int cid = rs.getInt(1);
                Date startMembership = rs.getDate(2);              
                Date endMembership = rs.getDate(3);
                Date receiptDate = rs.getDate(4);
                double subscriptionAmount = rs.getDouble(5);
                String extTransactionRef = rs.getString(6);
                String receiptNumber = rs.getString(7);
                String remarks = rs.getString(8);
                String receiptModeName = rs.getString(9);
                String explainIfOtherMode = rs.getString(10);
                String membershipClassName = rs.getString(11);
                String explainIfOtherClass = rs.getString(12);
                String paymentModeName = rs.getString(13);
                String explainIfOtherPayment = rs.getString(14);
                String createdBy = rs.getString(15);
                Date dateCreated = rs.getTimestamp(16);

                Membership membership = new Membership(cid, startMembership, endMembership, receiptDate, subscriptionAmount, extTransactionRef, receiptNumber, remarks, receiptModeName, explainIfOtherMode, membershipClassName, explainIfOtherClass, paymentModeName, explainIfOtherPayment, createdBy, dateCreated);
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
}
