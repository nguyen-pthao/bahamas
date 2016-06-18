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
    private int cid;
    private Date startMembership;
    private Date endMembership;
    private Date receiptDate;
    private double subscriptionAmount;
    private String extTransactionRef;
    private String receiptNumber;
    private String remarks;
    private String receiptModeName;
    private String explainIfOtherMode;
    private String membershipClassName;
    private String explainIfOtherClass;
    private String paymentModeName;
    private String explainIfOtherPayment;
    private String createdBy;
    private Date dateCreated = new java.util.Date();
    
    //Without Contact contact
    public Membership(int cid, Date startMembership, Date endMembership, Date receiptDate, double subscriptionAmount, String extTransactionRef, String receiptNumber, String remarks, String receiptModeName, String explainIfOtherMode, String membershipClassName, String explainIfOtherClass, String paymentModeName, String explainIfOtherPayment, String createdBy, Date dateCreated) {
        this.cid = cid;
        this.startMembership = startMembership;
        this.endMembership = endMembership;
        this.receiptDate = receiptDate;
        this.subscriptionAmount = subscriptionAmount;
        this.extTransactionRef = extTransactionRef;
        this.receiptNumber = receiptNumber;
        this.remarks = remarks;
        this.receiptModeName = receiptModeName;
        this.explainIfOtherMode = explainIfOtherMode;
        this.membershipClassName = membershipClassName;
        this.explainIfOtherClass = explainIfOtherClass;
        this.paymentModeName = paymentModeName;
        this.explainIfOtherPayment = explainIfOtherPayment;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
    }
    
    
    
    //Without String cid
    public Membership(Contact contact, Date startMembership, Date endMembership, Date receiptDate, double subscriptionAmount, String extTransactionRef, String receiptNumber, String remarks, String receiptModeName, String explainIfOtherMode, String membershipClassName, String explainIfOtherClass, String paymentModeName, String explainIfOtherPayment, String createdBy) {
        this.contact = contact;
        this.startMembership = startMembership;
        this.endMembership = endMembership;
        this.receiptDate = receiptDate;
        this.subscriptionAmount = subscriptionAmount;
        this.extTransactionRef = extTransactionRef;
        this.receiptNumber = receiptNumber;
        this.remarks = remarks;
        this.receiptModeName = receiptModeName;
        this.explainIfOtherMode = explainIfOtherMode;
        this.membershipClassName = membershipClassName;
        this.explainIfOtherClass = explainIfOtherClass;
        this.paymentModeName = paymentModeName;
        this.explainIfOtherPayment = explainIfOtherPayment;
        this.createdBy = createdBy;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public double getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(double subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceiptModeName() {
        return receiptModeName;
    }

    public void setReceiptModeName(String receiptModeName) {
        this.receiptModeName = receiptModeName;
    }

    public String getExplainIfOtherMode() {
        return explainIfOtherMode;
    }

    public void setExplainIfOtherMode(String explainIfOtherMode) {
        this.explainIfOtherMode = explainIfOtherMode;
    }

    public String getMembershipClassName() {
        return membershipClassName;
    }

    public void setMembershipClassName(String membershipClassName) {
        this.membershipClassName = membershipClassName;
    }

    public String getExplainIfOtherClass() {
        return explainIfOtherClass;
    }

    public void setExplainIfOtherClass(String explainIfOtherClass) {
        this.explainIfOtherClass = explainIfOtherClass;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public String getExplainIfOtherPayment() {
        return explainIfOtherPayment;
    }

    public void setExplainIfOtherPayment(String explainIfOtherPayment) {
        this.explainIfOtherPayment = explainIfOtherPayment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
}
