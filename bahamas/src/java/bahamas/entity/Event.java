/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.*;

/**
 *
 * @author huxley.goh
 */
public class Event {
    
    private long eventId;
    private Date dateCreated = new java.util.Date();
    private Date eventDate;
    private Date eventStart;
    private Date eventEnd;
    private String eventTitle;
    private String explainIfOthers;
    private String eventDescription;
    private int minimumParticipation;
    private boolean sendReminder;
    private String eventClassName;
    private String eventLocationName;
    private ArrayList<Contact> participants;
    private ArrayList<Role> roleList;
    private ArrayList<EventAffiliation> teamAffliated;
    private boolean workerRelated;
    private ArrayList<Notice> noticeList;

    public Event(Date eventDate, Date eventStart, Date eventEnd, String eventTitle, String explainIfOthers, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName) {
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventTitle = eventTitle;
        this.explainIfOthers = explainIfOthers;
        this.eventDescription = eventDescription;
        this.minimumParticipation = minimumParticipation;
        this.sendReminder = sendReminder;
        this.eventClassName = eventClassName;
        this.eventLocationName = eventLocationName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getEventStart() {
        return eventStart;
    }

    public void setEventStart(Date eventStart) {
        this.eventStart = eventStart;
    }

    public Date getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Date eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getExplainIfOthers() {
        return explainIfOthers;
    }

    public void setExplainIfOthers(String explainIfOthers) {
        this.explainIfOthers = explainIfOthers;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public boolean isSendReminder() {
        return sendReminder;
    }

    public void setSendReminder(boolean sendReminder) {
        this.sendReminder = sendReminder;
    }

    public String getEventClassName() {
        return eventClassName;
    }

    public void setEventClassName(String eventClassName) {
        this.eventClassName = eventClassName;
    }

    public String getEventLocationName() {
        return eventLocationName;
    }

    public void setEventLocationName(String eventLocationName) {
        this.eventLocationName = eventLocationName;
    }
    
    
    
    
    
    
}
