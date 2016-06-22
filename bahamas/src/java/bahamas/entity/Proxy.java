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
    
    private Contact proxy;
    private Contact principal;
    private int proxyID;
    private int principalID;
    private Date dateCreated =  new java.util.Date();
    private String createdBy;
    private String proxyStanding;
    private Date dateObsolete;
    private String remarks;
    
    public Proxy(){}

    public Proxy(Contact proxy, Contact principal, String createdBy, String proxyStanding, Date dateObsolete, String remarks) {
        this.proxy = proxy;
        this.principal = principal;
        this.createdBy = createdBy;
        this.proxyStanding = proxyStanding;
        this.dateObsolete = dateObsolete;
        this.remarks = remarks;
    }

    public Proxy(int proxyID, int principalID, Date dateCreated, String createdBy, String proxyStanding, Date dateObsolete, String remarks) {
        this.proxyID = proxyID;
        this.principalID = principalID;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.proxyStanding = proxyStanding;
        this.dateObsolete = dateObsolete;
        this.remarks = remarks;
    }
    
    
     
    public Contact getPrincipal() {
        return principal;
    }

    public void setPrincipal(Contact principal) {
        this.principal = principal;
    }

    public Contact getProxy() {
        return proxy;
    }

    public void setProxy(Contact proxy) {
        this.proxy = proxy;
    }

    public int getProxyID() {
        return proxyID;
    }

    public void setProxyID(int proxyID) {
        this.proxyID = proxyID;
    }

    public int getPrincipalID() {
        return principalID;
    }

    public void setPrincipalID(int principalID) {
        this.principalID = principalID;
    }
    
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getProxyStanding() {
        return proxyStanding;
    }

    public void setProxyStanding(String proxyStanding) {
        this.proxyStanding = proxyStanding;
    }

    public Date getDateObsolete() {
        return dateObsolete;
    }

    public void setDateObsolete(Date dateObsolete) {
        this.dateObsolete = dateObsolete;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    

}
