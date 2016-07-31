/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author huxley.goh
 */
public class EventAffiliation {
    
    private int eventID;
    private ArrayList<String> teamArray = new ArrayList<String>();
    private Date dateCreated = new java.util.Date();
    private String createdBy;
    private String explainIfOthers;
    private Date dateObsolete;
    private String remarks;

    public EventAffiliation(int eventID, String explainIfOthers, String remarks, ArrayList<String> teamArray, String createdBy) {
        this.eventID = eventID;
        this.explainIfOthers = explainIfOthers;
        this.remarks = remarks;
        this.teamArray = teamArray;
        this.createdBy = createdBy;
    }
    
    public EventAffiliation(int eventID, String explainIfOthers, String remarks, ArrayList<String> teamArray, String createdBy, Date dateCreated, Date dateObsolete) {
        this.eventID = eventID;
        this.explainIfOthers = explainIfOthers;
        this.remarks = remarks;
        this.teamArray = teamArray;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.dateObsolete = dateObsolete;
    }

    public EventAffiliation() {
        
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public ArrayList<String> getTeamArray() {
        return teamArray;
    }

    public void setTeamArray(ArrayList<String> teamArray) {
        this.teamArray = teamArray;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getExplainIfOthers() {
        return explainIfOthers;
    }

    public void setExplainIfOthers(String explainIfOthers) {
        this.explainIfOthers = explainIfOthers;
    }

    public Date getDateObsolete() {
        return dateObsolete;
    }

    public void setDateObsolete(Date dateObsolete) {
        this.dateObsolete = dateObsolete;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
    
}
