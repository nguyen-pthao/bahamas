/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.entity.Event;
import bahamas.entity.Phone;
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
    
    public Event retrieveEventById(int eventID) {
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
                Event event = new Event(eventID, eventDate, eventTimeStart, eventTimeEnd, eventTitle, explainIfOthers, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy);
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
    
    public static String eventExist(Event event) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        boolean exist = false;
        Date eventDate = null;
        Date startTime = null;
        Date endTime = null;
        try {
            eventDate = date.parse(date.format(event.getEventDate()));
            startTime = time.parse(time.format(event.getEventStart()));
            endTime = time.parse(time.format(event.getEventEnd()));
        } catch (ParseException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String errorMsg = null;
        ArrayList<String> conflctingEventName = new ArrayList<String>();
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT EVENT_TIME_START, EVENT_TIME_END, EVENT_DATE, EVENT_TITLE FROM EVENT WHERE EVENT_LOCATION_NAME = (?)");
            stmt.setString(1, event.getEventLocationName());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String strStartTime = rs.getString(1);
                String strEndTime = rs.getString(2);
                String strEventDate = rs.getString(3);
                Date startTimeDB0 = datetime.parse(strStartTime);
                Date endTimeDB0 = datetime.parse(strEndTime);
                Date eventDateDB0 = datetime.parse(strEventDate);
                Date startTimeDB = time.parse(time.format(startTimeDB0));
                Date endTimeDB = time.parse(time.format(endTimeDB0));
                Date eventDateDB = date.parse(date.format(eventDateDB0));
                
                String eventTitle = rs.getString(4);
                if ((startTime.equals(startTimeDB) || startTime.equals(endTimeDB) || (startTime.after(startTimeDB) && startTime.before(endTimeDB))) && eventDate.equals(eventDateDB)) {
                    conflctingEventName.add(eventTitle);
                } else if ((endTime.equals(startTimeDB) || endTime.equals(endTimeDB) || (endTime.after(startTimeDB) && endTime.before(endTimeDB))) && eventDate.equals(eventDateDB)) {
                    conflctingEventName.add(eventTitle);
                } else if (startTime.before(startTimeDB) && endTime.after(endTimeDB) && eventDate.equals(eventDateDB)) {
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
                    + "EXPLAIN_IF_OTHER, EVENT_DATE, EVENT_TIME_START, EVENT_TIME_END, SEND_REMINDER, "
                    + "EVENT_DESCRIPTION, MINIMUM_PARTICIPATIONS, EVENT_CLASS_NAME, EVENT_LOCATION_NAME, "
                    + "EVENT_LOCATION_LONGITUDE, EVENT_LOCATION_LATITUDE, EVENT_ID FROM EVENT");

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
                int eventID = rs.getInt(15);
                Event event = new Event(eventID, eventDate, eventTimeStart, eventTimeEnd, eventTitle, explainIfOthers, eventDescription, minimumParticipation, sendReminder, eventClassName, eventLocationName, eventLat, eventLng, dateCreated, createdBy);
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
