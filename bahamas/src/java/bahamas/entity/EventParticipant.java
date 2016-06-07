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
    
    private Contact participant;
    private Contact awarder;
    private int roleID;
    private Event event;
    private Date dateCreated=new java.util.Date();
    private int role;
    private boolean pullout;
    private Date datepullout;
    private String reason;
    private double hoursServed;
    private String service_comment;
    private String remarks;
    private String createdBy;

    public EventParticipant(Contact participant, Contact awarder, int roleID, Event event, Date dateCreated, int role, boolean pullout, Date datepullout, String reason, double hoursServed, String service_comment, String remarks, String createdBy) {
        this.participant = participant;
        this.awarder = awarder;
        this.roleID = roleID;
        this.event = event;
        this.dateCreated = dateCreated;
        this.role = role;
        this.pullout = pullout;
        this.datepullout = datepullout;
        this.reason = reason;
        this.hoursServed = hoursServed;
        this.service_comment = service_comment;
        this.remarks = remarks;
        this.createdBy = createdBy;
    }

    public Contact getParticipant() {
        return participant;
    }

    public void setParticipant(Contact participant) {
        this.participant = participant;
    }

    public Contact getAwarder() {
        return awarder;
    }

    public void setAwarder(Contact awarder) {
        this.awarder = awarder;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    
}
