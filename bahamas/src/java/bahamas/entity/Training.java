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
 * @author Darryl Mok
 */
public class Training {

    private int trainingId;
    private Contact contact;
    private String createdBy;
    private Date dateCreated = new java.util.Date();
    private String teamName;
    private String explainIfOther;
    private String trainingCourse;
    private String trainingBy;
    private Date trainingDate;
    private String remarks;

    public Training() {
    }

    public Training(Contact contact, String createdBy, String teamName, String explainIfOther, String trainingCourse, String trainingBy, Date trainingDate, String remarks) {
        this.contact = contact;
        this.createdBy = createdBy;
        this.teamName = teamName;
        this.explainIfOther = explainIfOther;
        this.trainingCourse = trainingCourse;
        this.trainingBy = trainingBy;
        this.trainingDate = trainingDate;
        this.remarks = remarks;
    }

    public Training(Contact contact, String createdBy, Date dateCreated, String teamName, String explainIfOther, String trainingCourse, String trainingBy, Date trainingDate, String remarks) {
        this.contact = contact;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.teamName = teamName;
        this.explainIfOther = explainIfOther;
        this.trainingCourse = trainingCourse;
        this.trainingBy = trainingBy;
        this.trainingDate = trainingDate;
        this.remarks = remarks;
    }
    
    public Training(int trainingId, Contact contact, String createdBy, Date dateCreated, String teamName, String explainIfOther, String trainingCourse, String trainingBy, Date trainingDate, String remarks) {
        this.trainingId = trainingId;
        this.contact = contact;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.teamName = teamName;
        this.explainIfOther = explainIfOther;
        this.trainingCourse = trainingCourse;
        this.trainingBy = trainingBy;
        this.trainingDate = trainingDate;
        this.remarks = remarks;
    }


    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getExplainIfOther() {
        return explainIfOther;
    }

    public void setExplainIfOther(String explainIfOther) {
        this.explainIfOther = explainIfOther;
    }

    public String getTrainingCourse() {
        return trainingCourse;
    }

    public void setTrainingCourse(String trainingCourse) {
        this.trainingCourse = trainingCourse;
    }

    public String getTrainingBy() {
        return trainingBy;
    }

    public void setTrainingBy(String trainingBy) {
        this.trainingBy = trainingBy;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
