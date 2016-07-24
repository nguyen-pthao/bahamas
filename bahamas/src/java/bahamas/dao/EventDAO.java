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
import java.sql.Statement;
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
public class EventDAO {
    
    private SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
    public static int addEvent(Event event, String createdBy) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int autoIncKeyFromApi = -1;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO EVENT (CREATED_BY, DATE_CREATED, "
                    + "EVENT_TITLE, EXPLAIN_IF_OTHER, EVENT_DATE, EVENT_TIME_START, "
                    + "EVENT_TIME_END, SEND_REMINDER, EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, "
                    + "EVENT_CLASS_NAME, EVENT_LOCATION_NAME, EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

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
            stmt.setString(13, event.getEventLng());
            stmt.setString(14, event.getEventLat());

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);
            }
            
            return autoIncKeyFromApi;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return autoIncKeyFromApi;
    }
    
    public Event retrieveContactById(int eventID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                    + "EXPLAIN_IF_OTHER, EVENT_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                    + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                    + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE FROM EVENT WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventID);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String createdBy = rs.getString(1);
                String dateStr1 = rs.getString(2);
                Date dateCreated = datetime.parse(dateStr1);
                String eventTitle = rs.getString(3);
                String explainIfOthers = rs.getString(4);
                String dateStr2 = rs.getString(5);
                Date eventDate = datetime.parse(dateStr2);
                String dateStr3 = rs.getString(6);
                Date eventTimeStart = datetime.parse(dateStr3);
                String dateStr4 = rs.getString(7);
                Date eventTimeEnd = datetime.parse(dateStr4);
                boolean sendReminder = rs.getBoolean(8);
                String eventDescription = rs.getString(9);
                int minimumParticipation = rs.getInt(10);
                String eventClassName = rs.getString(11);
                String eventLocationName = rs.getString(12);
                String eventLng = rs.getString(13);
                String eventLat = rs.getString(14);
                Event event = new Event(eventDate, eventTimeStart, eventTimeEnd, eventTitle, explainIfOthers, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng);
                return event;
                        
            }
        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve event from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }
            
}
