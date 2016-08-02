/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.Date;

/**
 *
 * @author tan.si.hao
 */
public class EventParticipant {
    
    private int contactID;
    private Integer awarderID;
    private int roleID;
    private int eventID;
    private String createdBy;
    private Date dateCreated = new java.util.Date();
    private boolean pullout;
    private Date datepullout;
    private String reason;
    private double hoursServed;
    private String service_comment;
    private String remarks;

    public EventParticipant(int contactID, Integer awarderID, int roleID, int eventID, String createdBy, boolean pullout, Date datepullout, String reason, double hoursServed, String service_comment, String remarks) {
        this.contactID = contactID;
        this.awarderID = awarderID;
        this.roleID = roleID;
        this.eventID = eventID;
        this.createdBy = createdBy;
        this.pullout = pullout;
        this.datepullout = datepullout;
        this.reason = reason;
        this.hoursServed = hoursServed;
        this.service_comment = service_comment;
        this.remarks = remarks;
    }

    public EventParticipant(int contactID, Integer awarderID, int roleID, int eventID, String createdBy, Date dateCreated, boolean pullout, Date datepullout, String reason, double hoursServed, String service_comment, String remarks) {
        this.contactID = contactID;
        this.awarderID = awarderID;
        this.roleID = roleID;
        this.eventID = eventID;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.pullout = pullout;
        this.datepullout = datepullout;
        this.reason = reason;
        this.hoursServed = hoursServed;
        this.service_comment = service_comment;
        this.remarks = remarks;
    }
    
    

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public Integer getAwarderID() {
        return awarderID;
    }

    public void setAwarderID(Integer awarderID) {
        this.awarderID = awarderID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isPullout() {
        return pullout;
    }

    public void setPullout(boolean pullout) {
        this.pullout = pullout;
    }

    public Date getDatepullout() {
        return datepullout;
    }

    public void setDatepullout(Date datepullout) {
        this.datepullout = datepullout;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getHoursServed() {
        return hoursServed;
    }

    public void setHoursServed(double hoursServed) {
        this.hoursServed = hoursServed;
    }

    public String getService_comment() {
        return service_comment;
    }

    public void setService_comment(String service_comment) {
        this.service_comment = service_comment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}