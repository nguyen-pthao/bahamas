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
public class OfficeHeld {
    private Contact contact;
    private Date dateCreated;
    private Date startOffice;
    private Date endOffice;
    private String remarks;
    private String createdBy;
    //from the dropdown list
    private String officeHeldPosition;

    public OfficeHeld(Contact contact, Date dateCreated, Date startOffice, Date endOffice, String remarks, String createdBy, String officeHeldPosition) {
        this.contact = contact;
        this.dateCreated = dateCreated;
        this.startOffice = startOffice;
        this.endOffice = endOffice;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.officeHeldPosition = officeHeldPosition;
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

    public Date getStartOffice() {
        return startOffice;
    }

    public void setStartOffice(Date startOffice) {
        this.startOffice = startOffice;
    }

    public Date getEndOffice() {
        return endOffice;
    }

    public void setEndOffice(Date endOffice) {
        this.endOffice = endOffice;
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

    public String getOfficeHeldPosition() {
        return officeHeldPosition;
    }

    public void setOfficeHeldPosition(String officeHeldPosition) {
        this.officeHeldPosition = officeHeldPosition;
    }
    
    
    
    
}
