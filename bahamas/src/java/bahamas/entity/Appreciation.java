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
public class Appreciation {
    
    private Contact contact;
    private int appreciationId;
    private Date dateCreated = new java.util.Date();
    private String appraisalComment;
    private String appraisalBy;
    private Date appraisalDate;
    private String appreciationGesture;
    private String appreciationBy;
    private Date appreciationDate;
    private String remarks;
    private String createdBy;

    public Appreciation(int appreciationId, Contact contact, Date dateCreated, 
            String appraisalComment, String appraisalBy, Date appraisalDate, String appreciationGesture, 
            String appreciationBy, Date appreciationDate, String remarks, String createdBy) {
        this.appreciationId = appreciationId;
        this.contact = contact;
        this.dateCreated = dateCreated;
        this.appraisalComment = appraisalComment;
        this.appraisalBy = appraisalBy;
        this.appraisalDate = appraisalDate;
        this.appreciationGesture = appreciationGesture;
        this.appreciationBy = appreciationBy;
        this.appreciationDate = appreciationDate;
        this.remarks = remarks;
        this.createdBy = createdBy;
    }

    public Appreciation(Contact contact, String appraisalComment, String appraisalBy, Date appraisalDate, String appreciationGesture, 
            String appreciationBy, Date appreciationDate, String remarks, String createdBy) {      
        this.contact = contact;
        this.appraisalComment = appraisalComment;
        this.appraisalBy = appraisalBy;
        this.appraisalDate = appraisalDate;
        this.appreciationGesture = appreciationGesture;
        this.appreciationBy = appreciationBy;
        this.appreciationDate = appreciationDate;
        this.remarks = remarks;
        this.createdBy = createdBy;
    }
    
    public Appreciation(int appreciationId, String appraisalComment, String appraisalBy, Date appraisalDate, String appreciationGesture, String appreciationBy, Date appreciationDate, String remarks, String createdBy, Date dateCreated) {
        this.appreciationId = appreciationId;
        this.appraisalComment = appraisalComment;
        this.appraisalBy = appraisalBy;
        this.appraisalDate = appraisalDate;
        this.appreciationGesture = appreciationGesture;
        this.appreciationBy = appreciationBy;
        this.appreciationDate = appreciationDate;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
    }
    
    public int getAppreciationId() {
        return appreciationId;
    }

    public void setAppreciationId(int appreciationId) {
        this.appreciationId = appreciationId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAppraisalComments() {
        return appraisalComment;
    }

    public void setAppraisalComments(String appraisalComment) {
        this.appraisalComment = appraisalComment;
    }

    public String getAppraisalBy() {
        return appraisalBy;
    }

    public void setAppraisalBy(String appraisalBy) {
        this.appraisalBy = appraisalBy;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public String getAppreciationGesture() {
        return appreciationGesture;
    }

    public void setAppreciationGesture(String appreciationGesture) {
        this.appreciationGesture = appreciationGesture;
    }

    public String getAppreciationBy() {
        return appreciationBy;
    }

    public void setAppreciationBy(String appreciationBy) {
        this.appreciationBy = appreciationBy;
    }

    public Date getAppreciationDate() {
        return appreciationDate;
    }

    public void setAppreciationDate(Date appreciationDate) {
        this.appreciationDate = appreciationDate;
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
