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
public class AuditLog {
    
    private String auditLogID;
    private String username;
    private Date operationDate;
    private String operation;

    public AuditLog(String auditLogID, String username, Date operationDate, String operation) {
        this.auditLogID = auditLogID;
        this.username = username;
        this.operationDate = operationDate;
        this.operation = operation;
    }

    public String getAuditLogID() {
        return auditLogID;
    }

    public void setAuditLogID(String auditLogID) {
        this.auditLogID = auditLogID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    
    
}
