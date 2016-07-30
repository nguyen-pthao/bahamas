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
    
    private int eventId;
    private Date dateCreated = new java.util.Date();
    private Date eventStartDate;
    private Date eventEndDate;
    private Date eventStartTime;
    private Date eventEndTime;
    private String eventTitle;
    private String explainIfOthers;
    private String eventDescription;
    private int minimumParticipation;
    private boolean sendReminder;
    private String eventClassName;
    private String eventLocationName;
    private String eventLat;
    private String eventLng;
    private String createdBy;
    private String eventStatus;
   
    private ArrayList<Contact> participants;
    private ArrayList<EventRoleAssignment> roleList;
    private ArrayList<EventAffiliation> teamAffliated;
    private boolean workerRelated;
    private ArrayList<Notice> noticeList;

    public Event(Date eventStartDate, Date eventEndDate, Date eventStartTime, Date eventEndTime, String eventTitle, String explainIfOthers, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName, String eventLat, String eventLng, Date dateCreated, String eventStatus) {
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventTitle = eventTitle;
        this.explainIfOthers = explainIfOthers;
        this.eventDescription = eventDescription;
        this.minimumParticipation = minimumParticipation;
        this.sendReminder = sendReminder;
        this.eventClassName = eventClassName;
        this.eventLocationName = eventLocationName;
        this.eventLat = eventLat;
        this.eventLng = eventLng;
        this.dateCreated = dateCreated;
        this.eventStatus = eventStatus;
    }
    
    public Event(Date eventStartDate, Date eventEndDate, Date eventStartTime, Date eventEndTime, String eventTitle, String explainIfOthers, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName, String eventLat, String eventLng, String eventStatus) {
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventTitle = eventTitle;
        this.explainIfOthers = explainIfOthers;
        this.eventDescription = eventDescription;
        this.minimumParticipation = minimumParticipation;
        this.sendReminder = sendReminder;
        this.eventClassName = eventClassName;
        this.eventLocationName = eventLocationName;
        this.eventLat = eventLat;
        this.eventLng = eventLng;
        this.eventStatus = eventStatus;
    }

    public Event(int eventId, Date eventStartDate, Date eventEndDate, Date eventStartTime, Date eventEndTime, String eventTitle, String explainIfOthers, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName, String eventLat, String eventLng, Date dateCreated, String createdBy, String eventStatus) {
        this.eventId = eventId;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventTitle = eventTitle;
        this.explainIfOthers = explainIfOthers;
        this.eventDescription = eventDescription;
        this.minimumParticipation = minimumParticipation;
        this.sendReminder = sendReminder;
        this.eventClassName = eventClassName;
        this.eventLocationName = eventLocationName;
        this.eventLat = eventLat;
        this.eventLng = eventLng;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.eventStatus = eventStatus;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public Date getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Date eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public Date getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Date eventEndTime) {
        this.eventEndTime = eventEndTime;
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
    
    public int getMinimumParticipation() {
        return minimumParticipation;
    }
    
    public void setMinimumParticipation(int minimumParticipation) {
        this.minimumParticipation = minimumParticipation;
    }

    public String getEventLat() {
        return eventLat;
    }

    public void setEventLat(String eventLat) {
        this.eventLat = eventLat;
    }

    public String getEventLng() {
        return eventLng;
    }

    public void setEventLng(String eventLng) {
        this.eventLng = eventLng;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }
    
}
