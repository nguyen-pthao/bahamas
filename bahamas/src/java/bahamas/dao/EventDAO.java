/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Event;
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
 * @author tan.si.hao
 */
public class EventDAO {
        
    public static boolean addEvent(Event event, String createdBy) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO EVENT (CREATED_BY, DATE_CREATED, "
                    + "EVENT_TITLE, EXPLAIN_IF_OTHER, EVENT_DATE, EVENT_TIME_START, "
                    + "EVENT_TIME_END, SEND_REMINDER, EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, "
                    + "EVENT_CLASS_NAME, EVENT_LOCATION_NAME) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            //stmt.setInt(1, d.getContact().getContactId());
            stmt.setString(1, createdBy);
            stmt.setTimestamp(2, new java.sql.Timestamp(event.getDateCreated().getTime()));
            stmt.setString(3, event.getEventTitle());
            stmt.setString(4, event.getExplainIfOthers());
            stmt.setTimestamp(5, new java.sql.Timestamp(event.getEventDate().getTime()));
            stmt.setTimestamp(6, new java.sql.Timestamp(event.getEventStart().getTime()));
            stmt.setTimestamp(7, new java.sql.Timestamp(event.getEventEnd().getTime()));
            stmt.setBoolean(8, event.isSendReminder());
            stmt.setString(9, event.getEventDescription());
            stmt.setInt(10, event.getMinimumParticipation());
            stmt.setString(11, event.getEventClassName());
            stmt.setString(12, event.getEventLocationName());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    public static int retrieveEventID(String eventTitle, Date eventTimeStart, Date eventTimeEnd) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int eventId = 0;
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT EVENT_ID FROM EVENT where EVENT_TITLE = (?) AND EVENT_TIME_START = (?) AND EVENT_TIME_END = (?)");

            stmt.setString(1, eventTitle);
            stmt.setDate(2, new java.sql.Date(eventTimeStart.getTime()));
            stmt.setDate(3, new java.sql.Date(eventTimeEnd.getTime()));

            rs = stmt.executeQuery();
            while (rs.next()) {
                eventId = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EVENT from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventId;
    }
            
}
