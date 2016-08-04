/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.EventParticipant;
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
public class EventParticipantDAO {
    
    public static boolean addEventParticipant(EventParticipant eventParticipant) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO EVENT_PARTICIPANT (`CONTACT_ID`, `AWARDER_ID`, `ROLE_ID`, "
                    + "`EVENT_ID`, `CREATED_BY`, `DATE_CREATED`, `PULLOUT`, `DATE_PULLOUT`, `REASON`, `HOURS_SERVED`, `SERVICE_COMMENT`, `REMARKS`)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setInt(1, eventParticipant.getContactID());
            stmt.setInt(2, eventParticipant.getAwarderID());
            stmt.setInt(3, eventParticipant.getRoleID());
            stmt.setInt(4, eventParticipant.getEventID());
            stmt.setString(5, eventParticipant.getCreatedBy());
            if (eventParticipant.getDateCreated() != null) {
                stmt.setDate(6, new java.sql.Date(eventParticipant.getDateCreated().getTime()));
            } else {
                stmt.setDate(6, null);
            }   
            stmt.setBoolean(7, eventParticipant.isPullout()); 
            if (eventParticipant.getDatepullout() != null) {
                stmt.setDate(8, new java.sql.Date(eventParticipant.getDatepullout().getTime()));
            } else {
                stmt.setDate(8, null);
            }
            stmt.setString(9, eventParticipant.getReason());
            stmt.setDouble(10, eventParticipant.getHoursServed());
            stmt.setString(11, eventParticipant.getService_comment());
            stmt.setString(12, eventParticipant.getRemarks());
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }
    
    public static ArrayList<EventParticipant> retrieveEventParticipantbyID(int eventID) {
        ArrayList<EventParticipant> eventParticipantList = new ArrayList<EventParticipant>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID, AWARDER_ID, ROLE_ID, EVENT_ID, CREATED_BY, "
                    + "DATE_CREATED, PULLOUT, DATE_PULLOUT, REASON, HOURS_SERVED, SERVICE_COMMENT, REMARKS "
                    + "FROM EVENT_PARTICIPANT WHERE EVENT_ID = (?)");
            stmt.setString(1, Integer.toString(eventID));
            rs = stmt.executeQuery();
            while (rs.next()) {

                int contactID = rs.getInt(1);
                Integer awarderID  = null;
                if(rs.getString(2) != null){
                    awarderID = rs.getInt(2);
                }
                int roleID = rs.getInt(3);
                //int eventID = rs.getInt(4);
                String createdBy = rs.getString(5);
                String dateCreatedStr = rs.getString(6);
                Date dateCreated = null;
                if (dateCreatedStr != null && !dateCreatedStr.isEmpty()) {
                    dateCreated = date.parse(dateCreatedStr);
                }
                boolean pullout = rs.getBoolean(7);
                String datePulloutStr = rs.getString(8);
                Date datepullout = null;
                if (datePulloutStr != null && !datePulloutStr.isEmpty()) {
                    datepullout = date.parse(datePulloutStr);
                }
                String reason = rs.getString(9);
                double hoursServed = rs.getDouble(10);
                String service_comment = rs.getString(11);
                String remarks = rs.getString(12);
                
                EventParticipant eventParticipant = new EventParticipant(contactID, awarderID, roleID, eventID, createdBy, dateCreated, pullout, datepullout, reason, hoursServed, service_comment, remarks);
                eventParticipantList.add(eventParticipant);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EMAIL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return eventParticipantList;

    }
    
}