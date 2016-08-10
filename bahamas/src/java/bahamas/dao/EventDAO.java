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
                    + "EVENT_TITLE, ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, "
                    + "EVENT_TIME_END, SEND_REMINDER, EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, "
                    + "EVENT_CLASS_NAME, EVENT_LOCATION_NAME, EVENT_STATUS, EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            //stmt.setInt(1, d.getContact().getContactId());
            stmt.setString(1, createdBy);
            stmt.setTimestamp(2, new java.sql.Timestamp(event.getDateCreated().getTime()));
            stmt.setString(3, event.getEventTitle());
            stmt.setString(4, event.getAddress());
            stmt.setString(5, event.getZipcode());
            stmt.setTimestamp(6, new java.sql.Timestamp(event.getEventStartDate().getTime()));
            stmt.setTimestamp(7, new java.sql.Timestamp(event.getEventEndDate().getTime()));
            stmt.setTimestamp(8, new java.sql.Timestamp(event.getEventStartTime().getTime()));
            stmt.setTimestamp(9, new java.sql.Timestamp(event.getEventEndTime().getTime()));
            stmt.setBoolean(10, event.isSendReminder());
            stmt.setString(11, event.getEventDescription());
            stmt.setInt(12, event.getMinimumParticipation());
            stmt.setString(13, event.getEventClassName());
            stmt.setString(14, event.getEventLocationName());
            stmt.setString(15, event.getEventStatus());
            stmt.setString(16, event.getEventLng());
            stmt.setString(17, event.getEventLat());

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
    
    public Event retrieveEventById(int eventID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                    + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                    + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                    + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_STATUS FROM EVENT WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventID);

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
                Event event = new Event(eventID, eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, address, zipcode, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy, eventStatus);
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
    
    public static String eventExist(Event event, String eventId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        boolean exist = false;
        Date eventStartDate = null;
        Date eventEndDate = null;
        Date startTime = null;
        Date endTime = null;
        try {
            eventStartDate = date.parse(date.format(event.getEventStartDate()));
            eventEndDate = date.parse(date.format(event.getEventEndDate()));
            startTime = time.parse(time.format(event.getEventStartTime()));
            endTime = time.parse(time.format(event.getEventEndTime()));
        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String errorMsg = null;
        ArrayList<String> conflctingEventName = new ArrayList<String>();
        
        try {
            conn = ConnectionManager.getConnection();
            if(eventId == null){
                stmt = conn.prepareStatement("SELECT EVENT_TIME_START, EVENT_TIME_END, EVENT_START_DATE, EVENT_END_DATE, EVENT_TITLE FROM EVENT "
                        + "WHERE EVENT_LOCATION_NAME = (?) AND (DATE(EVENT_START_DATE) <= (?) "
                        + "AND (DATE(EVENT_END_DATE) >= (?)) OR (DATE(EVENT_START_DATE) <= (?)) "
                        + "AND (DATE(EVENT_END_DATE) >= (?)) OR (DATE(EVENT_START_DATE) >= (?)) "
                        + "AND DATE(EVENT_END_DATE) <= (?))");
            }else{
                stmt = conn.prepareStatement("SELECT EVENT_TIME_START, EVENT_TIME_END, EVENT_START_DATE, EVENT_END_DATE, EVENT_TITLE FROM EVENT "
                        + "WHERE EVENT_LOCATION_NAME = (?) AND (DATE(EVENT_START_DATE) <= (?) "
                        + "AND (DATE(EVENT_END_DATE) >= (?)) OR (DATE(EVENT_START_DATE) <= (?)) "
                        + "AND (DATE(EVENT_END_DATE) >= (?)) OR (DATE(EVENT_START_DATE) >= (?)) "
                        + "AND DATE(EVENT_END_DATE) <= (?)) AND EVENT_ID <> (?)");
            }
            
            stmt.setString(1, event.getEventLocationName());
            stmt.setTimestamp(2, new java.sql.Timestamp(eventStartDate.getTime()));
            stmt.setTimestamp(3, new java.sql.Timestamp(eventStartDate.getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(eventEndDate.getTime()));
            stmt.setTimestamp(5, new java.sql.Timestamp(eventEndDate.getTime()));
            stmt.setTimestamp(6, new java.sql.Timestamp(eventStartDate.getTime()));
            stmt.setTimestamp(7, new java.sql.Timestamp(eventEndDate.getTime()));
            if(eventId != null){
                stmt.setInt(8, Integer.parseInt(eventId));
            }
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                String strStartTime = rs.getString(1);
                String strEndTime = rs.getString(2);
                //String strEventStartDate = rs.getString(3);
                //String strEventEndDate = rs.getString(4);
                Date startTimeDB0 = datetime.parse(strStartTime);
                Date endTimeDB0 = datetime.parse(strEndTime);
                //Date eventStartDateDB0 = datetime.parse(strEventStartDate);
                //Date eventEndDateDB0 = datetime.parse(strEventEndDate);
                Date startTimeDB = time.parse(time.format(startTimeDB0));
                Date endTimeDB = time.parse(time.format(endTimeDB0));
                //Date eventStartDB = date.parse(date.format(eventStartDateDB0));
                //Date eventEndDB = date.parse(date.format(eventEndDateDB0));
                
                String eventTitle = rs.getString(5);
                //if ((startTime.equals(startTimeDB) || startTime.equals(endTimeDB) || (startTime.after(startTimeDB) && startTime.before(endTimeDB))) && eventDate.equals(eventDateDB)) {
                if (startTime.equals(startTimeDB) || (startTime.after(startTimeDB) && startTime.before(endTimeDB))) {
                    conflctingEventName.add(eventTitle);
                //} else if (endTime.equals(startTimeDB) || endTime.equals(endTimeDB) || (endTime.after(startTimeDB) && endTime.before(endTimeDB))) {
                } else if (endTime.equals(endTimeDB) || (endTime.after(startTimeDB) && endTime.before(endTimeDB))) {
                    conflctingEventName.add(eventTitle);
                //} else if (startTime.before(startTimeDB) && endTime.after(endTimeDB) && eventDate.equals(eventDateDB)) {
                } else if (startTime.before(startTimeDB) && endTime.after(endTimeDB)) {
                    conflctingEventName.add(eventTitle);
                }
            }
            if(!conflctingEventName.isEmpty()){
                if(conflctingEventName.size() == 1){
                    errorMsg = "Location is already taken up by event ";
                }else{
                    errorMsg = "Location is already taken up by events ";
                }
                for(int i = 0; i < conflctingEventName.size() -1; i++){
                    errorMsg += conflctingEventName.get(i) + ", ";
                }
                return errorMsg += conflctingEventName.get(conflctingEventName.size() -1) + ". Do you want to proceed?";
                
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EMAIL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return errorMsg;
    }
    
    public ArrayList<Event> retrieveAllEvents() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Event> eventList = null;
        try {
            eventList = new ArrayList<Event>();
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                    + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                    + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                    + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_ID, EVENT_STATUS FROM EVENT");

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
                int eventID = rs.getInt(17);
                String eventStatus = rs.getString(18);
                Event event = new Event(eventID, eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, address, zipcode, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy, eventStatus);
                eventList.add(event);      
            }
        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve event from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventList;
    }
    
    public static boolean deleteEvent(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM EVENT_AFFILIATION WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventId);
            stmt.executeUpdate(); 
            stmt = conn.prepareStatement("DELETE FROM EVENT_ROLE_ASSIGNMENT WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
            stmt = conn.prepareStatement("DELETE FROM EVENT WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    public static boolean updateEventDetails(Event event) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE EVENT SET EVENT_TITLE = ?, ADDRESS = ?, ZIPCODE = ?, "
                    + "EVENT_START_DATE = ?, EVENT_END_DATE = ?, "
                    + "EVENT_TIME_START = ?, EVENT_TIME_END = ?, "
                    + "SEND_REMINDER = ?, EVENT_DESCRIPTION = ?, MINIMUM_PARTICIPATIONS = ?, "
                    + "EVENT_CLASS_NAME = ?, EVENT_LOCATION_NAME = ?, EVENT_STATUS = ?, "
                    + "EVENT_LOCATION_LONGITUDE = ?, EVENT_LOCATION_LATITUDE = ? "
                    + "WHERE EVENT_ID = ?");

            stmt.setString(1, event.getEventTitle());
            stmt.setString(2, event.getAddress());
            stmt.setString(3, event.getZipcode());
            stmt.setTimestamp(4, new java.sql.Timestamp(event.getEventStartDate().getTime()));
            stmt.setTimestamp(5, new java.sql.Timestamp(event.getEventEndDate().getTime()));
            stmt.setTimestamp(6, new java.sql.Timestamp(event.getEventStartTime().getTime()));
            stmt.setTimestamp(7, new java.sql.Timestamp(event.getEventEndTime().getTime()));
            stmt.setBoolean(8, event.isSendReminder());
            stmt.setString(9, event.getEventDescription());
            stmt.setInt(10, event.getMinimumParticipation());
            stmt.setString(11, event.getEventClassName());
            stmt.setString(12, event.getEventLocationName());
            stmt.setString(13, event.getEventStatus());
            stmt.setString(14, event.getEventLng());
            stmt.setString(15, event.getEventLat());
            stmt.setInt(16, event.getEventId());
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    public ArrayList<Event> retrieveAllEventsGroupByEventTitle() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Event> eventList = null;
        try {
            eventList = new ArrayList<Event>();
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CREATED_BY, DATE_CREATED, EVENT_TITLE, "
                    + "ADDRESS, ZIPCODE, EVENT_START_DATE, EVENT_END_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                    + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                    + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_ID, EVENT_STATUS FROM EVENT ORDER BY EVENT_TITLE, EVENT_START_DATE DESC");

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
                int eventID = rs.getInt(17);
                String eventStatus = rs.getString(18);
                Event event = new Event(eventID, eventStartDate, eventEndDate, eventTimeStart, eventTimeEnd, eventTitle, address, zipcode, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy, eventStatus);
                eventList.add(event);      
            }
        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve event from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventList;
    }
    
}
