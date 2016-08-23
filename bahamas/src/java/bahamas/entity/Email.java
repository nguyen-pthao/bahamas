/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.Date;

/**
 *
 * @author Darryl Mok
 */
public class Email {
    private int contactId;
    private Contact contact;
    private Date dateCreated = new java.util.Date();
    private String email;
    private String remarks;
    private String createdBy;
    private Date dateObsolete;
    private boolean verified;

    public Email(Contact contact, String email, String createdBy, boolean verified) {
        this.contact = contact;
        this.email = email;
        this.createdBy = createdBy;
        this.verified = verified;
    }

    public Email(Contact contact, String email, String createdBy, String remarks, Date dateObsolete, boolean verified) {
        this.contact = contact;
        this.email = email;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dateObsolete = dateObsolete;
        this.verified = verified;
    }
    
    public Email(Contact contact, String email, String createdBy, String remarks, Date dateObsolete, Date dateCreated, boolean verified) {
        this.contact = contact;
        this.email = email;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dateObsolete = dateObsolete;
        this.dateCreated = dateCreated;
        this.verified = verified;
    }
    
    public Email(int contactId, String email, String createdBy, String remarks, Date dateObsolete, Date dateCreated, boolean verified) {
        this.contactId = contactId;
        this.email = email;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dateObsolete = dateObsolete;
        this.dateCreated = dateCreated;
        this.verified = verified;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    
    
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getDateObsolete() {
        return dateObsolete;
    }

    public void setDateObsolete(Date dateObsolete) {
        this.dateObsolete = dateObsolete;
    }

}
