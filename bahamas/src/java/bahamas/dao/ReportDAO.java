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
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class ReportDAO {

    public ReportDAO() {
    }

    public static HashMap<Integer, ArrayList<String>> summaryTeamParticipants(String team, Date start, Date end) {
        HashMap<Integer, ArrayList<String>> resultMap = new HashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT c.CONTACT_ID,c.NAME, c.USERNAME, MIN(DATE(e.DATE_CREATED)), "
                    + "MAX(DATE(e.DATE_CREATED)) FROM CONTACT c, TEAM_JOIN t, EVENT_PARTICIPANT e "
                    + "WHERE c.CONTACT_ID=t.CONTACT_ID "
                    + "AND e.CONTACT_ID=c.CONTACT_ID AND PULLOUT=FALSE AND TEAM_NAME = ? "
                    + "GROUP BY c.CONTACT_ID,c.NAME, c.USERNAME");

            stmt.setString(1, team);
            rs = stmt.executeQuery();
            while (rs.next()) {

                int contact_id = rs.getInt(1);
                String name = rs.getString(2);
                String username = rs.getString(3);
                String firstParticapationDate = rs.getString(4);
                String lastParticapationDate = rs.getString(5);

                ArrayList<String> temp = new ArrayList<String>();
                temp.add(name);
                temp.add(username);
                temp.add(firstParticapationDate);
                temp.add(lastParticapationDate);

                resultMap.put(contact_id, temp);
            }

            Iterator<Integer> iter = resultMap.keySet().iterator();
            while (iter.hasNext()) {

                stmt = conn.prepareStatement("SELECT CONTACT_ID, COUNT(CONTACT_ID),"
                        + "SUM(HOURS_SERVED) from EVENT_PARTICIPANT where CONTACT_ID=? "
                        + "AND DATE(DATE_CREATED) BETWEEN "
                        + "? AND ? GROUP BY CONTACT_ID");

                stmt.setInt(1, iter.next());
                stmt.setDate(2, new java.sql.Date(start.getTime()));
                stmt.setDate(3, new java.sql.Date(end.getTime()));

                rs = stmt.executeQuery();
                while (rs.next()) {

                    int contact_id = rs.getInt(1);
                    int numOfSignUp = rs.getInt(2);
                    int hoursServed = rs.getInt(3);

                    ArrayList<String> temp = resultMap.get(contact_id);
                    if (temp != null) {
                        temp.add(String.valueOf(numOfSignUp));
                        temp.add(String.valueOf(hoursServed));
                    }

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

    public static HashMap<Integer, ArrayList<String>> individualParticipant(int cid, String team, Date start, Date end) {
        HashMap<Integer, ArrayList<String>> resultMap = new HashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT DATE(ep.DATE_CREATED), TEAM_NAME, EVENT_TITLE, ROLE_NAME, HOURS_SERVED FROM EVENT_PARTICIPANT ep,"
                    + "TEAM_JOIN T, EVENT e, EVENT_ROLE_ASSIGNMENT r WHERE "
                    + "ep.CONTACT_ID = t.CONTACT_ID AND ep.EVENT_ID=e.EVENT_ID AND "
                    + "r.ROLE_ID=ep.ROLE_ID AND ep.CONTACT_ID= ? AND TEAM_NAME=? "
                    + "AND DATE(ep.DATE_CREATED) BETWEEN ? AND ? ORDER BY DATE(ep.DATE_CREATED)");

            stmt.setInt(1, cid);
            stmt.setString(2, team);
            stmt.setDate(3, new java.sql.Date(start.getTime()));
            stmt.setDate(4, new java.sql.Date(end.getTime()));

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {

                String DateOfEventSignUp = rs.getString(1);
                String teamName = rs.getString(2);
                String eventTitle = rs.getString(3);
                String role = rs.getString(4);
                String hours = String.valueOf(rs.getInt(5));

                ArrayList<String> temp = new ArrayList<String>();
                temp.add(DateOfEventSignUp);
                temp.add(teamName);
                temp.add(eventTitle);
                temp.add(role);
                temp.add(hours);

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

    public static HashMap<Integer, ArrayList<String>> summaryEvents(String team, Date start, Date end) {
        HashMap<Integer, ArrayList<String>> resultMap = new HashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT DATE(EVENT_START_DATE), EVENT_TITLE, "
                    + "e.CREATED_BY, PARTICIPANT_NUMBER, EVENT_STATUS FROM EVENT e, EVENT_AFFILIATION ea " 
                    + "WHERE e.EVENT_ID=ea.EVENT_ID AND TEAM_NAME=? AND DATE(EVENT_START_DATE) "
                    + "BETWEEN ? AND ? ORDER BY DATE(EVENT_START_DATE)");

            stmt.setString(1, team);
            stmt.setDate(2, new java.sql.Date(start.getTime()));
            stmt.setDate(3, new java.sql.Date(end.getTime()));

            int counter = 0;
            rs = stmt.executeQuery();
            while (rs.next()) {

                String DateOfEvent = rs.getString(1);              
                String eventTitle = rs.getString(2);
                String eventCreator = rs.getString(3);
                String numOfParticipants = String.valueOf(rs.getInt(4));
                String status = rs.getString(5);
               
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(DateOfEvent);
                temp.add(eventTitle);
                temp.add(eventCreator);
                temp.add(numOfParticipants);
                temp.add(status);

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

}
