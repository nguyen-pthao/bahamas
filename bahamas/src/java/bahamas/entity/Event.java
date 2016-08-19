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
    private String address;
    private String zipcode;
    private String eventDescription;
    private int minimumParticipation;
    private boolean sendReminder;
    private String eventClassName;
    private String eventLocationName;
    private String eventLat;
    private String eventLng;
    private String createdBy;
    private String eventStatus;
    private String remarks;
   
    private ArrayList<Contact> participants;
    private ArrayList<EventRoleAssignment> roleList;
    private ArrayList<EventAffiliation> teamAffliated;
    private boolean workerRelated;
    private ArrayList<Notice> noticeList;

    public Event(Date eventStartDate, Date eventEndDate, Date eventStartTime, Date eventEndTime, String eventTitle, String address, String zipcode, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName, String eventLat, String eventLng, Date dateCreated, String eventStatus, String remarks) {
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventTitle = eventTitle;
        this.address = address;
        this.zipcode = zipcode;
        this.eventDescription = eventDescription;
        this.minimumParticipation = minimumParticipation;
        this.sendReminder = sendReminder;
        this.eventClassName = eventClassName;
        this.eventLocationName = eventLocationName;
        this.eventLat = eventLat;
        this.eventLng = eventLng;
        this.dateCreated = dateCreated;
        this.eventStatus = eventStatus;
        this.remarks = remarks;
    }
    
    public Event(Date eventStartDate, Date eventEndDate, Date eventStartTime, Date eventEndTime, String eventTitle, String address, String zipcode, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName, String eventLat, String eventLng, String eventStatus, String remarks) {
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventTitle = eventTitle;
        this.address = address;
        this.zipcode = zipcode;
        this.eventDescription = eventDescription;
        this.minimumParticipation = minimumParticipation;
        this.sendReminder = sendReminder;
        this.eventClassName = eventClassName;
        this.eventLocationName = eventLocationName;
        this.eventLat = eventLat;
        this.eventLng = eventLng;
        this.eventStatus = eventStatus;
        this.remarks = remarks;
    }
    public Event(int eventId, Date eventStartDate, Date eventEndDate, Date eventStartTime, Date eventEndTime, String eventTitle, String address, String zipcode, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName, String eventLat, String eventLng, String eventStatus, String remarks) {
        this.eventId = eventId;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventTitle = eventTitle;
        this.address = address;
        this.zipcode = zipcode;
        this.eventDescription = eventDescription;
        this.minimumParticipation = minimumParticipation;
        this.sendReminder = sendReminder;
        this.eventClassName = eventClassName;
        this.eventLocationName = eventLocationName;
        this.eventLat = eventLat;
        this.eventLng = eventLng;
        this.eventStatus = eventStatus;
        this.remarks = remarks;
    }

    public Event(int eventId, Date eventStartDate, Date eventEndDate, Date eventStartTime, Date eventEndTime, String eventTitle, String address, String zipcode, String eventDescription, int minimumParticipation, boolean sendReminder, String eventClassName, String eventLocationName, String eventLat, String eventLng, Date dateCreated, String createdBy, String eventStatus, String remarks) {
        this.eventId = eventId;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventTitle = eventTitle;
        this.address = address;
        this.zipcode = zipcode;
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
        this.remarks = remarks;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
