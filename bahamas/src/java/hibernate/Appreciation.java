package hibernate;
// Generated 14 May, 2016 6:30:30 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Appreciation generated by hbm2java
 */
public class Appreciation  implements java.io.Serializable {


     private int appreciationId;
     private int contactId;
     private int createdBy;
     private Date dateCreated;
     private String appraisalComments;
     private String appraisedBy;
     private Date appraisalDate;
     private String appreciationGesture;
     private Date appreciationDate;
     private String remarks;

    public Appreciation() {
    }

	
    public Appreciation(int appreciationId, int contactId, int createdBy, Date dateCreated) {
        this.appreciationId = appreciationId;
        this.contactId = contactId;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
    }
    public Appreciation(int appreciationId, int contactId, int createdBy, Date dateCreated, String appraisalComments, String appraisedBy, Date appraisalDate, String appreciationGesture, Date appreciationDate, String remarks) {
       this.appreciationId = appreciationId;
       this.contactId = contactId;
       this.createdBy = createdBy;
       this.dateCreated = dateCreated;
       this.appraisalComments = appraisalComments;
       this.appraisedBy = appraisedBy;
       this.appraisalDate = appraisalDate;
       this.appreciationGesture = appreciationGesture;
       this.appreciationDate = appreciationDate;
       this.remarks = remarks;
    }
   
    public int getAppreciationId() {
        return this.appreciationId;
    }
    
    public void setAppreciationId(int appreciationId) {
        this.appreciationId = appreciationId;
    }
    public int getContactId() {
        return this.contactId;
    }
    
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    public int getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    public Date getDateCreated() {
        return this.dateCreated;
    }
    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public String getAppraisalComments() {
        return this.appraisalComments;
    }
    
    public void setAppraisalComments(String appraisalComments) {
        this.appraisalComments = appraisalComments;
    }
    public String getAppraisedBy() {
        return this.appraisedBy;
    }
    
    public void setAppraisedBy(String appraisedBy) {
        this.appraisedBy = appraisedBy;
    }
    public Date getAppraisalDate() {
        return this.appraisalDate;
    }
    
    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }
    public String getAppreciationGesture() {
        return this.appreciationGesture;
    }
    
    public void setAppreciationGesture(String appreciationGesture) {
        this.appreciationGesture = appreciationGesture;
    }
    public Date getAppreciationDate() {
        return this.appreciationDate;
    }
    
    public void setAppreciationDate(Date appreciationDate) {
        this.appreciationDate = appreciationDate;
    }
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }




}


