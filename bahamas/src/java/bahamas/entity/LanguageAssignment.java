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
public class LanguageAssignment {
    
    private Contact contact;
    private String language;
    private Date dateCreated = new java.util.Date();
    private String explainIfOther;
    private Date dateObsolete;
    private String proficiency;
    private String remarks;
    private String createdBy;

    public LanguageAssignment(Contact contact, String language, String explainIfOther, Date dateObsolete, String proficiency, String remarks, String createdBy) {
        this.contact = contact;
        this.language = language;
        this.dateCreated = dateCreated;
        this.explainIfOther = explainIfOther;
        this.dateObsolete = dateObsolete;
        this.proficiency = proficiency;
        this.remarks = remarks;
        this.createdBy = createdBy;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Date getDateObsolete() {
        return dateObsolete;
    }

    public void setDateObsolete(Date dateObsolete) {
        this.dateObsolete = dateObsolete;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
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
