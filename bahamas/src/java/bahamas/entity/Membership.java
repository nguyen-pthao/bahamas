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
public class Membership {
    
    private Contact contact;
    private String receiptModeName;
    private String membershipClassName;
    private String paymentModeName;
    private Date dateCreated;
    private String explainIfOtherClass;
    private Date startMembership;
    private Date endMembership;
    private double subscriptionAmount;
    private String explainIfOtherPayment;
    private String extTransactionRef;
    private String receiptNumber;
    private Date receiptDate;
    private String explainIfOtherMode;
    private String remarks;
    private String createdBy;

    public Membership(Contact contact, String membershipClassName, String paymentModeName, Date dateCreated, String explainIfOtherClass, Date startMembership, Date endMembership, double subscriptionAmount, String explainIfOtherPayment, String extTransactionRef, String receiptNumber, Date receiptDate, String explainIfOtherMode, String remarks, String createdBy) {
        this.contact = contact;
        this.membershipClassName = membershipClassName;
        this.paymentModeName = paymentModeName;
        this.dateCreated = dateCreated;
        this.explainIfOtherClass = explainIfOtherClass;
        this.startMembership = startMembership;
        this.endMembership = endMembership;
        this.subscriptionAmount = subscriptionAmount;
        this.explainIfOtherPayment = explainIfOtherPayment;
        this.extTransactionRef = extTransactionRef;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.explainIfOtherMode = explainIfOtherMode;
        this.remarks = remarks;
        this.createdBy = createdBy;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getReceiptModeName() {
        return receiptModeName;
    }

    public void setReceiptModeName(String receiptModeName) {
        this.receiptModeName = receiptModeName;
    }

    public String getMembershipClassName() {
        return membershipClassName;
    }

    public void setMembershipClassName(String membershipClassName) {
        this.membershipClassName = membershipClassName;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getExplainIfOtherClass() {
        return explainIfOtherClass;
    }

    public void setExplainIfOtherClass(String explainIfOtherClass) {
        this.explainIfOtherClass = explainIfOtherClass;
    }

    public Date getStartMembership() {
        return startMembership;
    }

    public void setStartMembership(Date startMembership) {
        this.startMembership = startMembership;
    }

    public Date getEndMembership() {
        return endMembership;
    }

    public void setEndMembership(Date endMembership) {
        this.endMembership = endMembership;
    }

    public double getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(double subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public String getExplainIfOtherPayment() {
        return explainIfOtherPayment;
    }

    public void setExplainIfOtherPayment(String explainIfOtherPayment) {
        this.explainIfOtherPayment = explainIfOtherPayment;
    }

    public String getExtTransactionRef() {
        return extTransactionRef;
    }

    public void setExtTransactionRef(String extTransactionRef) {
        this.extTransactionRef = extTransactionRef;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getExplainIfOtherMode() {
        return explainIfOtherMode;
    }

    public void setExplainIfOtherMode(String explainIfOtherMode) {
        this.explainIfOtherMode = explainIfOtherMode;
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
