package bahamas.entity;

import java.util.*;

import java.util.Date;


public class TrainingCertificated  implements java.io.Serializable {


     private int certificationId;
     private int contactId;
     private String createdBy;
     private Date dateCreated;
     private String explainIfOther;
     private String remarks;
     private String trainingCourse;

    public TrainingCertificated() {
    }

    public TrainingCertificated(int certificationId, int contactId, String createdBy, Date dateCreated, String explainIfOther, String remarks, String trainingCourse) {
        this.certificationId = certificationId;
        this.contactId = contactId;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.explainIfOther = explainIfOther;
        this.remarks = remarks;
        this.trainingCourse = trainingCourse;
    }

    public int getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
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

    public String getExplainIfOther() {
        return explainIfOther;
    }

    public void setExplainIfOther(String explainIfOther) {
        this.explainIfOther = explainIfOther;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTrainingCourse() {
        return trainingCourse;
    }

    public void setTrainingCourse(String trainingCourse) {
        this.trainingCourse = trainingCourse;
    }
    
    
	
    




}


