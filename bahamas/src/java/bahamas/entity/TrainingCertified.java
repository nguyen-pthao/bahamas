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
public class TrainingCertified {
    
    private int certificationID;
    private String createdBy;
    private String explainIfOther;
    private String trainingCourse;
    private Date trainingDate;
    private String remarks;
    
    private Contact participant;
    private Event event;
    private Contact certifier;

    public TrainingCertified(int certificationID, String createdBy, String explainIfOther, String trainingCourse, Date trainingDate, String remarks, Contact participant, Contact certifier) {
        this.certificationID = certificationID;
        this.createdBy = createdBy;
        this.explainIfOther = explainIfOther;
        this.trainingCourse = trainingCourse;
        this.trainingDate = trainingDate;
        this.remarks = remarks;
        this.participant = participant;
        this.certifier = certifier;
    }

    public int getCertificationID() {
        return certificationID;
    }

    public void setCertificationID(int certificationID) {
        this.certificationID = certificationID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Contact getParticipant() {
        return participant;
    }

    public void setParticipant(Contact participant) {
        this.participant = participant;
    }

    public Contact getCertifier() {
        return certifier;
    }

    public void setCertifier(Contact certifier) {
        this.certifier = certifier;
    }
    
    
            
}
