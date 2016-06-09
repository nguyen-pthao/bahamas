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
public class Address {

    private Contact contact;
    private Date dateCreated = new java.util.Date();
    private int zipcode;
    private String address;
    private String country;
    private String remarks;
    private String createdBy;
    private Date dateObsolete;

    public Address(Contact contact, String country, int zipcode, String address, String createdBy) {
        this.contact = contact;
        this.zipcode = zipcode;
        this.address = address;
        this.country = country;
        this.createdBy = createdBy;
    }

   
      public Address(Contact contact, String country, int zipcode, String address, String createdBy, String remarks, Date dateObsolete) {
        this.contact = contact;
        this.zipcode = zipcode;
        this.address = address;
        this.country = country;
        this.createdBy = createdBy;
        this.remarks = remarks;
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

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
