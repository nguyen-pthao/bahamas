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
            
}
