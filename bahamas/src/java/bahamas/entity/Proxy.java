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
public class Proxy {

    private Date dateCreated = new java.util.Date();
    private String proxyStanding;
    private String remarks;
    private String createdBy;
    private Date dateObsolete;
    private Contact organisation;
    private Contact organisationRepresentative;

    public Proxy(Date dateCreated, String proxyStanding, String remarks, String createdBy, Date dateObsolete, Contact organisation, Contact organisationRepresentative) {
        this.dateCreated = dateCreated;
        this.proxyStanding = proxyStanding;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dateObsolete = dateObsolete;
        this.organisation = organisation;
        this.organisationRepresentative = organisationRepresentative;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getProxyStanding() {
        return proxyStanding;
    }

    public void setProxyStanding(String proxyStanding) {
        this.proxyStanding = proxyStanding;
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

    public Contact getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Contact organisation) {
        this.organisation = organisation;
    }

    public Contact getOrganisationRepresentative() {
        return organisationRepresentative;
    }

    public void setOrganisationRepresentative(Contact organisationRepresentative) {
        this.organisationRepresentative = organisationRepresentative;
    }

}
