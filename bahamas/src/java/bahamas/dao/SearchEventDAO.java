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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tan.si.hao
 */
public class SearchEventDAO {

    private static SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static HashMap<Integer, Event> searchEventByTitleLocationDate(String eventTitleValue, String locationValue, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Event> eventHM = new HashMap<Integer, Event>();

        try {
            conn = ConnectionManager.getConnection();

            if (startDate == null && endDate == null) {
                stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                        + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                        + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                        + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_STATUS, REMARKS, CONTACT_ID, EMAIL, PARTICIPANT_NUMBER, EVENT_ID "
                        + "FROM EVENT WHERE EVENT_TITLE LIKE ? AND EVENT_LOCATION_NAME like ?");
                if(eventTitleValue == null){
                    stmt.setString(1, "%");
                }else{
                    stmt.setString(1, "%" + eventTitleValue + "%");
                }
                if(locationValue == null){
                    stmt.setString(2, "%");
                }else{
                    stmt.setString(2, locationValue);
                }
            } else {
                stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                        + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                        + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                        + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_STATUS, REMARKS, CONTACT_ID, EMAIL, PARTICIPANT_NUMBER, EVENT_ID "
                        + "FROM EVENT WHERE EVENT_TITLE LIKE ? AND EVENT_LOCATION_NAME = ? AND EVENT_START_DATE BETWEEN ? AND ?");
                if(eventTitleValue == null){
                    stmt.setString(1, "%");
                }else{
                    stmt.setString(1, "%" + eventTitleValue + "%");
                }
                if(locationValue == null){
                    stmt.setString(2, "%");
                }else{
                    stmt.setString(2, locationValue);
                }
                stmt.setDate(3, new java.sql.Date(startDate.getTime()));
                stmt.setDate(4, new java.sql.Date(endDate.getTime()));
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                String createdBy = rs.getString(1);
                String dateStr1 = rs.getString(2);
                Date dateCreated = datetime.parse(dateStr1);
                String eventTitle = rs.getString(3);
                String address = rs.getString(4);
                String zipcode = rs.getString(5);
                String dateStr2 = rs.getString(6);
                Date eventStartDate = datetime.parse(dateStr2);
                String dateStr3 = rs.getString(7);
                Date eventEndDate = datetime.parse(dateStr3);
                String dateStr4 = rs.getString(8);
                Date eventTimeStart = datetime.parse(dateStr4);
                String dateStr5 = rs.getString(9);
                Date eventTimeEnd = datetime.parse(dateStr5);
                boolean sendReminder = rs.getBoolean(10);
                String eventDescription = rs.getString(11);
                int minimumParticipation = rs.getInt(12);
                String eventClassName = rs.getString(13);
                String eventLocationName = rs.getString(14);
                String eventLng = rs.getString(15);
                String eventLat = rs.getString(16);
                String eventStatus = rs.getString(17);
                String remarks = rs.getString(18);
                int contactId = rs.getInt(19);
                String reminderEmail = rs.getString(20);
                int participantNumber = rs.getInt(21);
                int eventID = rs.getInt(22);

                Event event = new Event(eventID, eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, address, zipcode, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy, eventStatus, remarks, contactId, reminderEmail, participantNumber);
                eventHM.put(eventID, event);
            }

        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve event from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventHM;
    }

    public static HashMap<Integer, Event> searchEventByTitleOtherlocationDate(String eventTitleValue, String addressValue, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Event> eventHM = new HashMap<Integer, Event>();

        try {
            conn = ConnectionManager.getConnection();

            if (startDate == null && endDate == null) {
                stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                        + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                        + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                        + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_STATUS, REMARKS, CONTACT_ID, EMAIL, PARTICIPANT_NUMBER, EVENT_ID "
                        + "FROM EVENT WHERE EVENT_TITLE LIKE ? AND ADDRESS LIKE ?");
                if(eventTitleValue == null){
                    stmt.setString(1, "%");
                }else{
                    stmt.setString(1, "%" + eventTitleValue + "%");
                }
                if(addressValue == null){
                    stmt.setString(2, "%");
                }else{
                    stmt.setString(2, "%" + addressValue + "%");
                }
                
                
            } else {

                stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                        + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                        + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                        + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_STATUS, REMARKS, CONTACT_ID, EMAIL, PARTICIPANT_NUMBER, EVENT_ID "
                        + "FROM EVENT WHERE EVENT_TITLE LIKE ? AND ADDRESS LIKE ? AND EVENT_START_DATE BETWEEN ? AND ?");
              
                if(eventTitleValue == null){
                    stmt.setString(1, "%");
                }else{
                    stmt.setString(1, "%" + eventTitleValue + "%");
                }
                if(addressValue == null){
                    stmt.setString(2, "%");
                }else{
                    stmt.setString(2, "%" + addressValue + "%");
                }
                
                stmt.setDate(3, new java.sql.Date(startDate.getTime()));
                stmt.setDate(4, new java.sql.Date(endDate.getTime()));
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                String createdBy = rs.getString(1);
                String dateStr1 = rs.getString(2);
                Date dateCreated = datetime.parse(dateStr1);
                String eventTitle = rs.getString(3);
                String address = rs.getString(4);
                String zipcode = rs.getString(5);
                String dateStr2 = rs.getString(6);
                Date eventStartDate = datetime.parse(dateStr2);
                String dateStr3 = rs.getString(7);
                Date eventEndDate = datetime.parse(dateStr3);
                String dateStr4 = rs.getString(8);
                Date eventTimeStart = datetime.parse(dateStr4);
                String dateStr5 = rs.getString(9);
                Date eventTimeEnd = datetime.parse(dateStr5);
                boolean sendReminder = rs.getBoolean(10);
                String eventDescription = rs.getString(11);
                int minimumParticipation = rs.getInt(12);
                String eventClassName = rs.getString(13);
                String eventLocationName = rs.getString(14);
                String eventLng = rs.getString(15);
                String eventLat = rs.getString(16);
                String eventStatus = rs.getString(17);
                String remarks = rs.getString(18);
                int contactId = rs.getInt(19);
                String reminderEmail = rs.getString(20);
                int participantNumber = rs.getInt(21);
                int eventID = rs.getInt(22);

                Event event = new Event(eventID, eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, address, zipcode, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy, eventStatus, remarks, contactId, reminderEmail, participantNumber);
                eventHM.put(eventID, event);
            }

        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve event from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventHM;
    }
    
    public static HashMap<Integer, Event> searchEventByTeam(String teamValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Event> eventHM = new HashMap<Integer, Event>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                    + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                    + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                    + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_STATUS, REMARKS, CONTACT_ID, EMAIL, PARTICIPANT_NUMBER, EVENT_ID "
                    + "FROM EVENT WHERE EVENT_ID IN (SELECT DISTINCT EVENT_ID FROM EVENT_AFFILIATION WHERE TEAM_NAME = ? )");
            stmt.setString(1, teamValue);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                String createdBy = rs.getString(1);
                String dateStr1 = rs.getString(2);
                Date dateCreated = datetime.parse(dateStr1);
                String eventTitle = rs.getString(3);
                String address = rs.getString(4);
                String zipcode = rs.getString(5);
                String dateStr2 = rs.getString(6);
                Date eventStartDate = datetime.parse(dateStr2);
                String dateStr3 = rs.getString(7);
                Date eventEndDate = datetime.parse(dateStr3);
                String dateStr4 = rs.getString(8);
                Date eventTimeStart = datetime.parse(dateStr4);
                String dateStr5 = rs.getString(9);
                Date eventTimeEnd = datetime.parse(dateStr5);
                boolean sendReminder = rs.getBoolean(10);
                String eventDescription = rs.getString(11);
                int minimumParticipation = rs.getInt(12);
                String eventClassName = rs.getString(13);
                String eventLocationName = rs.getString(14);
                String eventLng = rs.getString(15);
                String eventLat = rs.getString(16);
                String eventStatus = rs.getString(17);
                String remarks = rs.getString(18);
                int contactId = rs.getInt(19);
                String reminderEmail = rs.getString(20);
                int participantNumber = rs.getInt(21);
                int eventID = rs.getInt(22);

                Event event = new Event(eventID, eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, address, zipcode, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy, eventStatus, remarks, contactId, reminderEmail, participantNumber);
                eventHM.put(eventID, event);
            }

        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve event from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventHM;
    }
    
    public static HashMap<Integer, Event> searchEventByParticipant(String participantName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Event> eventHM = new HashMap<Integer, Event>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                    + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                    + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                    + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_STATUS, REMARKS, CONTACT_ID, EMAIL, PARTICIPANT_NUMBER, EVENT_ID "
                    + "FROM EVENT WHERE EVENT_ID IN (SELECT DISTINCT EVENT_ID FROM EVENT_PARTICIPANT WHERE CONTACT_ID IN (SELECT CONTACT_ID FROM CONTACT WHERE NAME LIKE ?))");
            stmt.setString(1, "%"+participantName+"%");
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                String createdBy = rs.getString(1);
                String dateStr1 = rs.getString(2);
                Date dateCreated = datetime.parse(dateStr1);
                String eventTitle = rs.getString(3);
                String address = rs.getString(4);
                String zipcode = rs.getString(5);
                String dateStr2 = rs.getString(6);
                Date eventStartDate = datetime.parse(dateStr2);
                String dateStr3 = rs.getString(7);
                Date eventEndDate = datetime.parse(dateStr3);
                String dateStr4 = rs.getString(8);
                Date eventTimeStart = datetime.parse(dateStr4);
                String dateStr5 = rs.getString(9);
                Date eventTimeEnd = datetime.parse(dateStr5);
                boolean sendReminder = rs.getBoolean(10);
                String eventDescription = rs.getString(11);
                int minimumParticipation = rs.getInt(12);
                String eventClassName = rs.getString(13);
                String eventLocationName = rs.getString(14);
                String eventLng = rs.getString(15);
                String eventLat = rs.getString(16);
                String eventStatus = rs.getString(17);
                String remarks = rs.getString(18);
                int contactId = rs.getInt(19);
                String reminderEmail = rs.getString(20);
                int participantNumber = rs.getInt(21);
                int eventID = rs.getInt(22);

                Event event = new Event(eventID, eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, address, zipcode, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy, eventStatus, remarks, contactId, reminderEmail, participantNumber);
                eventHM.put(eventID, event);
            }

        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve event from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventHM;
    }

}
