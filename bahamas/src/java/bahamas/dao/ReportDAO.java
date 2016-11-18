/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Phone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class ReportDAO {

    private static SimpleDateFormat formatTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
    private static SimpleDateFormat formatDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    public ReportDAO() {
    }

    public static LinkedHashMap<Integer, ArrayList<String>> summaryTeamParticipants(String team, Date start, Date end) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String lastLoginValue = "";

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT c.CONTACT_ID, c.NAME, c.USERNAME, MIN(DATE(e.EVENT_START_DATE)), "
                    + "MAX(DATE(e.EVENT_END_DATE)), c.LAST_LOGIN FROM CONTACT c, EVENT_AFFILIATION ea, "
                    + "EVENT_PARTICIPANT ep, EVENT e WHERE c.CONTACT_ID=ep.CONTACT_ID "
                    + "AND ea.EVENT_ID=ep.EVENT_ID AND e.EVENT_ID=ep.EVENT_ID "
                    + "AND PULLOUT=FALSE AND ea.TEAM_NAME = ? AND (DATE(e.EVENT_START_DATE) "
                    + "BETWEEN ? AND ? OR DATE(e.EVENT_END_DATE) BETWEEN ? AND ?)"
                    + "GROUP BY c.CONTACT_ID,c.NAME, c.USERNAME");

            stmt.setString(1, team);
            stmt.setDate(2, new java.sql.Date(start.getTime()));
            stmt.setDate(3, new java.sql.Date(end.getTime()));
            stmt.setDate(4, new java.sql.Date(start.getTime()));
            stmt.setDate(5, new java.sql.Date(end.getTime()));

            rs = stmt.executeQuery();
            while (rs.next()) {

                int contact_id = rs.getInt(1);
                String name = rs.getString(2);
                String username = rs.getString(3);
                String firstParticapationDate = rs.getString(4);
                String lastParticapationDate = rs.getString(5);
                Date lastLogin = rs.getTimestamp(6);

                if (lastLogin != null) {
                    lastLoginValue = formatTime.format(lastLogin);
                }

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, String.valueOf(contact_id));
                addEmptyString(temp, name);
                addEmptyString(temp, username);
                addEmptyString(temp, firstParticapationDate);
                addEmptyString(temp, lastParticapationDate);

                resultMap.put(contact_id, temp);
            }

            Iterator<Integer> iter = resultMap.keySet().iterator();
            while (iter.hasNext()) {

                boolean toAdd = true;
                int cid = iter.next();
                ArrayList<String> temp = resultMap.get(cid);

                stmt = conn.prepareStatement("SELECT CONTACT_ID, COUNT(CONTACT_ID),"
                        + "SUM(HOURS_SERVED) FROM EVENT_PARTICIPANT ep, EVENT_AFFILIATION ea WHERE CONTACT_ID=? "
                        + "AND ea.EVENT_ID=ep.EVENT_ID AND PULLOUT=FALSE AND ea.TEAM_NAME = ? AND DATE(ep.DATE_CREATED) BETWEEN "
                        + "? AND ? GROUP BY CONTACT_ID");

                stmt.setInt(1, cid);
                stmt.setString(2, team);
                stmt.setDate(3, new java.sql.Date(start.getTime()));
                stmt.setDate(4, new java.sql.Date(end.getTime()));

                rs = stmt.executeQuery();
                while (rs.next()) {

                    int contact_id = rs.getInt(1);
                    int numOfSignUp = rs.getInt(2);
                    int hoursServed = rs.getInt(3);

                    temp = resultMap.get(contact_id);
                    if (temp != null) {
                        toAdd = false;
                        addEmptyString(temp, String.valueOf(numOfSignUp));
                        addEmptyString(temp, String.valueOf(hoursServed));
                        addEmptyString(temp, lastLoginValue);
                    }

                }

                if (toAdd) {
                    addEmptyString(temp, "0");
                    addEmptyString(temp, "0");
                    addEmptyString(temp, "");
                }
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> individualParticipant(int cid, String team, Date start, Date end) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();

            if (team != null) {
                stmt = conn.prepareStatement("SELECT DATE(e.EVENT_START_DATE), ea.TEAM_NAME, EVENT_TITLE, "
                        + "ROLE_NAME, HOURS_SERVED, SERVICE_COMMENT FROM EVENT_PARTICIPANT ep,"
                        + "EVENT_AFFILIATION ea, EVENT e, EVENT_ROLE_ASSIGNMENT r WHERE "
                        + "ep.EVENT_ID=e.EVENT_ID AND ea.EVENT_ID=e.EVENT_ID AND "
                        + "r.ROLE_ID=ep.ROLE_ID AND r.EVENT_ID=e.EVENT_ID AND ep.CONTACT_ID= ? AND ea.TEAM_NAME=? "
                        + "AND DATE(e.EVENT_START_DATE) BETWEEN ? AND ? ORDER BY DATE(ep.DATE_CREATED)");

                stmt.setInt(1, cid);
                stmt.setString(2, team);
                stmt.setDate(3, new java.sql.Date(start.getTime()));
                stmt.setDate(4, new java.sql.Date(end.getTime()));

            } else {
                stmt = conn.prepareStatement("SELECT DATE(e.EVENT_START_DATE), ea.TEAM_NAME, EVENT_TITLE, "
                        + "ROLE_NAME, HOURS_SERVED, SERVICE_COMMENT FROM EVENT_PARTICIPANT ep,"
                        + "EVENT_AFFILIATION ea, EVENT e, EVENT_ROLE_ASSIGNMENT r WHERE "
                        + "ep.EVENT_ID=e.EVENT_ID AND ea.EVENT_ID=e.EVENT_ID AND "
                        + "r.ROLE_ID=ep.ROLE_ID AND r.EVENT_ID=e.EVENT_ID AND ep.CONTACT_ID= ? "
                        + "AND DATE(e.EVENT_START_DATE) BETWEEN ? AND ? ORDER BY DATE(e.EVENT_START_DATE)");

                stmt.setInt(1, cid);
                stmt.setDate(2, new java.sql.Date(start.getTime()));
                stmt.setDate(3, new java.sql.Date(end.getTime()));
            }

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {

                Date dateOfEventSignUp = rs.getDate(1);
                String eventSignUpDate = "";
                if (dateOfEventSignUp != null) {
                    eventSignUpDate = formatDate.format(dateOfEventSignUp);
                }

                String teamName = rs.getString(2);
                String eventTitle = rs.getString(3);
                String role = rs.getString(4);
                String hours = String.valueOf(rs.getInt(5));
                String serviceComment = rs.getString(6);

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, eventSignUpDate);
                addEmptyString(temp, teamName);
                addEmptyString(temp, eventTitle);
                addEmptyString(temp, role);
                addEmptyString(temp, hours);
                addEmptyString(temp, serviceComment);

                resultMap.put(++counter, temp);
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> summaryEvents(String team, Date start, Date end) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT e.EVENT_ID, DATE(EVENT_START_DATE), EVENT_TITLE, "
                    + "e.CREATED_BY, PARTICIPANT_NUMBER, EVENT_STATUS FROM EVENT e, EVENT_AFFILIATION ea "
                    + "WHERE e.EVENT_ID=ea.EVENT_ID AND TEAM_NAME=? AND DATE(EVENT_START_DATE) "
                    + "BETWEEN ? AND ? ORDER BY DATE(EVENT_START_DATE)");

            stmt.setString(1, team);
            stmt.setDate(2, new java.sql.Date(start.getTime()));
            stmt.setDate(3, new java.sql.Date(end.getTime()));

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                int eventId = rs.getInt(1);
                Date DateOfEvent = rs.getDate(2);
                String dateOfEvent = "";
                if (DateOfEvent != null) {
                    dateOfEvent = formatDate.format(DateOfEvent);
                }

                String eventTitle = rs.getString(3);
                String eventCreator = rs.getString(4);
                String numOfParticipants = String.valueOf(rs.getInt(5));
                String status = rs.getString(6);

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, String.valueOf(eventId));
                addEmptyString(temp, dateOfEvent);
                addEmptyString(temp, eventTitle);
                addEmptyString(temp, eventCreator);
                addEmptyString(temp, numOfParticipants);
                addEmptyString(temp, status);

                resultMap.put(++counter, temp);
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> summaryCurrentMembership(Date reference, String membershipType) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            if (membershipType != null) {
                stmt = conn.prepareStatement("SELECT c.CONTACT_ID, c.NAME, MEMBERSHIP_CLASS_NAME, START_MEMBERSHIP, "
                        + "END_MEMBERSHIP, SUBSCRIPTION_AMOUNT, RECEIPT_DATE FROM MEMBERSHIP m, "
                        + "CONTACT c WHERE m.CONTACT_ID=c.CONTACT_ID AND ? BETWEEN START_MEMBERSHIP "
                        + "AND END_MEMBERSHIP AND MEMBERSHIP_CLASS_NAME=?");

                stmt.setString(2, membershipType);

            } else {
                stmt = conn.prepareStatement("SELECT c.CONTACT_ID, c.NAME, MEMBERSHIP_CLASS_NAME, START_MEMBERSHIP, "
                        + "END_MEMBERSHIP, SUBSCRIPTION_AMOUNT, RECEIPT_DATE FROM MEMBERSHIP m, "
                        + "CONTACT c WHERE m.CONTACT_ID=c.CONTACT_ID AND ? BETWEEN START_MEMBERSHIP "
                        + "AND END_MEMBERSHIP");
            }

            stmt.setDate(1, new java.sql.Date(reference.getTime()));

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                int contactId = rs.getInt(1);
                String name = rs.getString(2);
                String membershipClass = rs.getString(3);

                Date startDate = rs.getDate(4);
                String membershipStart = "";
                if (startDate != null) {
                    membershipStart = formatDate.format(startDate);
                }

                Date endDate = rs.getDate(5);
                String membershipEnd = "";
                if (endDate != null) {
                    membershipEnd = formatDate.format(endDate);
                }

                String subscriptionAmt = rs.getString(6);

                Date rDate = rs.getDate(7);
                String receiptDate = "";
                if (rDate != null) {
                    receiptDate = formatDate.format(rDate);
                }

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, String.valueOf(contactId));
                addEmptyString(temp, name);
                addEmptyString(temp, membershipClass);
                addEmptyString(temp, membershipStart);
                addEmptyString(temp, membershipEnd);
                addEmptyString(temp, subscriptionAmt);
                addEmptyString(temp, receiptDate);

                resultMap.put(++counter, temp);
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> summaryMembership(Date start, Date end) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT c.CONTACT_ID, c.NAME, MEMBERSHIP_CLASS_NAME, START_MEMBERSHIP, "
                    + "END_MEMBERSHIP, SUBSCRIPTION_AMOUNT, RECEIPT_DATE FROM MEMBERSHIP m, "
                    + "CONTACT c WHERE m.CONTACT_ID=c.CONTACT_ID AND DATE(START_MEMBERSHIP) BETWEEN ? "
                    + "AND ?");

            stmt.setDate(1, new java.sql.Date(start.getTime()));
            stmt.setDate(2, new java.sql.Date(end.getTime()));

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                int contactId = rs.getInt(1);
                String name = rs.getString(2);
                String membershipClass = rs.getString(3);

                Date startDate = rs.getDate(4);
                String membershipStart = "";
                if (startDate != null) {
                    membershipStart = formatDate.format(startDate);
                }

                Date endDate = rs.getDate(5);
                String membershipEnd = "";
                if (endDate != null) {
                    membershipEnd = formatDate.format(endDate);
                }

                String subscriptionAmt = rs.getString(6);

                Date rDate = rs.getDate(7);
                String receiptDate = "";
                if (rDate != null) {
                    receiptDate = formatDate.format(rDate);
                }

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, String.valueOf(contactId));
                addEmptyString(temp, name);
                addEmptyString(temp, membershipClass);
                addEmptyString(temp, membershipStart);
                addEmptyString(temp, membershipEnd);
                addEmptyString(temp, subscriptionAmt);
                addEmptyString(temp, receiptDate);

                resultMap.put(++counter, temp);
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> individualMembership(int cid, Date start, Date end) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT MEMBERSHIP_CLASS_NAME, START_MEMBERSHIP, "
                    + "END_MEMBERSHIP, SUBSCRIPTION_AMOUNT, RECEIPT_DATE FROM MEMBERSHIP m, "
                    + "CONTACT c WHERE m.CONTACT_ID=c.CONTACT_ID AND m.CONTACT_ID=? AND DATE(START_MEMBERSHIP) BETWEEN ? "
                    + "AND ?");

            stmt.setInt(1, cid);
            stmt.setDate(2, new java.sql.Date(start.getTime()));
            stmt.setDate(3, new java.sql.Date(end.getTime()));

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {

                String membershipClass = rs.getString(1);

                Date startDate = rs.getDate(2);
                String membershipStart = "";
                if (startDate != null) {
                    membershipStart = formatDate.format(startDate);
                }

                Date endDate = rs.getDate(3);
                String membershipEnd = "";
                if (endDate != null) {
                    membershipEnd = formatDate.format(endDate);
                }

                String subscriptionAmt = rs.getString(4);

                Date rDate = rs.getDate(5);
                String receiptDate = "";
                if (rDate != null) {
                    receiptDate = formatDate.format(rDate);
                }

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, membershipClass);
                addEmptyString(temp, membershipStart);
                addEmptyString(temp, membershipEnd);
                addEmptyString(temp, subscriptionAmt);
                addEmptyString(temp, receiptDate);

                resultMap.put(++counter, temp);
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> summaryDonations(String paymentMode, Date start, Date end) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();

            if (paymentMode != null) {
                stmt = conn.prepareStatement("SELECT d.DATE_RECEIVED, c.NAME, DONATION_AMOUNT, PAYMENT_MODE_NAME, "
                        + "RECEIPT_NUMBER, DONOR_INSTRUCTIONS, SUBAMOUNT_1, SUBAMOUNT_2, SUBAMOUNT_3 "
                        + "FROM DONATION d, CONTACT c WHERE d.CONTACT_ID=c.CONTACT_ID AND PAYMENT_MODE_NAME=? "
                        + "AND d.DATE_RECEIVED BETWEEN ? AND ?");

                stmt.setString(1, paymentMode);
                stmt.setDate(2, new java.sql.Date(start.getTime()));
                stmt.setDate(3, new java.sql.Date(end.getTime()));

            } else {
                stmt = conn.prepareStatement("SELECT d.DATE_RECEIVED, c.NAME, DONATION_AMOUNT, PAYMENT_MODE_NAME, "
                        + "RECEIPT_NUMBER, DONOR_INSTRUCTIONS, SUBAMOUNT_1, SUBAMOUNT_2, SUBAMOUNT_3 "
                        + "FROM DONATION d, CONTACT c WHERE d.CONTACT_ID=c.CONTACT_ID "
                        + "AND d.DATE_RECEIVED BETWEEN ? AND ?");

                stmt.setDate(1, new java.sql.Date(start.getTime()));
                stmt.setDate(2, new java.sql.Date(end.getTime()));
            }

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {

                Date rDate = rs.getDate(1);
                String receivedDate = "";
                if (rDate != null) {
                    receivedDate = formatDate.format(rDate);
                }

                String name = rs.getString(2);
                String donationAmt = rs.getString(3);
                String paymentM = rs.getString(4);
                String receiptNum = rs.getString(5);
                String donorInstruc = rs.getString(6);
                String subAmt1 = rs.getString(7);
                String subAmt2 = rs.getString(8);
                String subAmt3 = rs.getString(9);

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, receivedDate);
                addEmptyString(temp, name);
                addEmptyString(temp, donationAmt);
                addEmptyString(temp, paymentM);
                addEmptyString(temp, receiptNum);
                addEmptyString(temp, donorInstruc);
                addEmptyString(temp, subAmt1);
                addEmptyString(temp, subAmt2);
                addEmptyString(temp, subAmt3);

                resultMap.put(++counter, temp);
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> individualDonations(int cid, Date start, Date end) {
        LinkedHashMap<Integer, ArrayList<String>> resultMap = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT d.DATE_RECEIVED, DONATION_AMOUNT, PAYMENT_MODE_NAME, "
                    + "RECEIPT_NUMBER, DONOR_INSTRUCTIONS, SUBAMOUNT_1, SUBAMOUNT_2, SUBAMOUNT_3 "
                    + "FROM DONATION d, CONTACT c WHERE d.CONTACT_ID=c.CONTACT_ID AND d.CONTACT_ID=? "
                    + "AND DATE_RECEIVED BETWEEN ? AND ?");

            stmt.setInt(1, cid);
            stmt.setDate(2, new java.sql.Date(start.getTime()));
            stmt.setDate(3, new java.sql.Date(end.getTime()));

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {

                Date rDate = rs.getDate(1);
                String receivedDate = "";
                if (rDate != null) {
                    receivedDate = formatDate.format(rDate);
                }

                String donationAmt = rs.getString(2);
                String paymentM = rs.getString(3);
                String receiptNum = rs.getString(4);
                String donorInstruc = rs.getString(5);
                String subAmt1 = rs.getString(6);
                String subAmt2 = rs.getString(7);
                String subAmt3 = rs.getString(8);

                ArrayList<String> temp = new ArrayList<String>();
                addEmptyString(temp, receivedDate);
                addEmptyString(temp, donationAmt);
                addEmptyString(temp, paymentM);
                addEmptyString(temp, receiptNum);
                addEmptyString(temp, donorInstruc);
                addEmptyString(temp, subAmt1);
                addEmptyString(temp, subAmt2);
                addEmptyString(temp, subAmt3);

                resultMap.put(++counter, temp);
            }

            return resultMap;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return resultMap;
    }

    public static void addEmptyString(ArrayList<String> temp, String value) {

        if (value == null) {
            String t = "";
            temp.add(t);
        } else {
            temp.add(value);
        }

    }

}
