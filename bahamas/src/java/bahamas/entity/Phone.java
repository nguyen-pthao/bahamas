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
public class Phone {

    private Contact contact;
    private Date dateCreated = new java.util.Date();
    private int countryCode;
    private String phoneNumber;
    private String remarks;
    private String createdBy;
    private Date dateObsolete;

    public Phone(Contact contact, int countryCode, String phoneNumber, String createdBy) {
        this.contact = contact;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;

    }

    public Phone(Contact contact, int countryCode, String phoneNumber, String createdBy, 
            String remarks, Date dateObsolete ) {
        this.contact = contact;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dateObsolete = dateObsolete;
            
    }

    public Phone(int countryCode, String phoneNumber, String remarks, String createdBy, Date dateCreated, Date dateObsolete) {
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.dateObsolete = dateObsolete;
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

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
