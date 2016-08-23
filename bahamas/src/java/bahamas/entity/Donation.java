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
public class Donation {

    private int contactId;
    private int donationId;
    private Contact contact;
    private Date dateCreated = new java.util.Date();
    private String createdBy;
    private Date dateReceived;
    private double donationAmount;
    private String paymentMode;
    private String explainIfOtherPayment;
    private String extTransactionRef;
    private String receiptMode;
    private String receiptNumber;
    private Date receiptDate;
    private String explainIfOtherReceipt;
    private String donorInstructions;
    private String allocation1;
    private double subAmount1;
    private String allocation2;
    private double subAmount2;
    private String allocation3;
    private double subAmount3;
    private String associatedOccasion;
    private String remarks;

    public Donation() {
    }

    public Donation(Contact contact, String createdBy, Date dateReceived,
            double donationAmount, String paymentMode, String explainIfOtherPayment,
            String extTransactionRef, String receiptMode, String receiptNumber, Date receiptDate,
            String explainIfOtherReceipt, String donorInstructions,
            String allocation1, double subAmount1, String allocation2,
            double subAmount2, String allocation3, double subAmount3, String associatedOccasion, String remarks) {
        this.contact = contact;
        this.createdBy = createdBy;
        this.dateReceived = dateReceived;
        this.donationAmount = donationAmount;
        this.paymentMode = paymentMode;
        this.explainIfOtherPayment = explainIfOtherPayment;
        this.extTransactionRef = extTransactionRef;
        this.receiptMode = receiptMode;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.explainIfOtherReceipt = explainIfOtherReceipt;
        this.donorInstructions = donorInstructions;
        this.allocation1 = allocation1;
        this.subAmount1 = subAmount1;
        this.allocation2 = allocation2;
        this.subAmount2 = subAmount2;
        this.allocation3 = allocation3;
        this.subAmount3 = subAmount3;
        this.associatedOccasion = associatedOccasion;
        this.remarks = remarks;
    }

    public Donation(int donationId, Date dateCreated, String createdBy, Date dateReceived, double donationAmount, String paymentMode, String explainIfOtherPayment, String extTransactionRef, String receiptMode, String receiptNumber, Date receiptDate, String explainIfOtherReceipt, String donorInstructions, String allocation1, double subAmount1, String allocation2, double subAmount2, String allocation3, double subAmount3, String associatedOccasion, String remarks) {
        this.donationId = donationId;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.dateReceived = dateReceived;
        this.donationAmount = donationAmount;
        this.paymentMode = paymentMode;
        this.explainIfOtherPayment = explainIfOtherPayment;
        this.extTransactionRef = extTransactionRef;
        this.receiptMode = receiptMode;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.explainIfOtherReceipt = explainIfOtherReceipt;
        this.donorInstructions = donorInstructions;
        this.allocation1 = allocation1;
        this.subAmount1 = subAmount1;
        this.allocation2 = allocation2;
        this.subAmount2 = subAmount2;
        this.allocation3 = allocation3;
        this.subAmount3 = subAmount3;
        this.associatedOccasion = associatedOccasion;
        this.remarks = remarks;
    }
    public Donation(int contactId, int donationId, Date dateCreated, String createdBy, Date dateReceived, double donationAmount, String paymentMode, String explainIfOtherPayment, String extTransactionRef, String receiptMode, String receiptNumber, Date receiptDate, String explainIfOtherReceipt, String donorInstructions, String allocation1, double subAmount1, String allocation2, double subAmount2, String allocation3, double subAmount3, String associatedOccasion, String remarks) {
        this.contactId = contactId;
        this.donationId = donationId;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.dateReceived = dateReceived;
        this.donationAmount = donationAmount;
        this.paymentMode = paymentMode;
        this.explainIfOtherPayment = explainIfOtherPayment;
        this.extTransactionRef = extTransactionRef;
        this.receiptMode = receiptMode;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.explainIfOtherReceipt = explainIfOtherReceipt;
        this.donorInstructions = donorInstructions;
        this.allocation1 = allocation1;
        this.subAmount1 = subAmount1;
        this.allocation2 = allocation2;
        this.subAmount2 = subAmount2;
        this.allocation3 = allocation3;
        this.subAmount3 = subAmount3;
        this.associatedOccasion = associatedOccasion;
        this.remarks = remarks;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public double getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
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

    public String getReceiptMode() {
        return receiptMode;
    }

    public void setReceiptMode(String receiptMode) {
        this.receiptMode = receiptMode;
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

    public String getExplainIfOtherReceipt() {
        return explainIfOtherReceipt;
    }

    public void setExplainIfOtherReceipt(String explainIfOtherReceipt) {
        this.explainIfOtherReceipt = explainIfOtherReceipt;
    }

    public String getDonorInstructions() {
        return donorInstructions;
    }

    public void setDonorInstructions(String donorInstructions) {
        this.donorInstructions = donorInstructions;
    }

    public String getAllocation1() {
        return allocation1;
    }

    public void setAllocation1(String allocation1) {
        this.allocation1 = allocation1;
    }

    public double getSubAmount1() {
        return subAmount1;
    }

    public void setSubAmount1(double subAmount1) {
        this.subAmount1 = subAmount1;
    }

    public String getAllocation2() {
        return allocation2;
    }

    public void setAllocation2(String allocation2) {
        this.allocation2 = allocation2;
    }

    public double getSubAmount2() {
        return subAmount2;
    }

    public void setSubAmount2(double subAmount2) {
        this.subAmount2 = subAmount2;
    }

    public String getAllocation3() {
        return allocation3;
    }

    public void setAllocation3(String allocation3) {
        this.allocation3 = allocation3;
    }

    public double getSubAmount3() {
        return subAmount3;
    }

    public void setSubAmount3(double subAmount3) {
        this.subAmount3 = subAmount3;
    }

    public String getAssociatedOccasion() {
        return associatedOccasion;
    }

    public void setAssociatedOccasion(String associatedOccasion) {
        this.associatedOccasion = associatedOccasion;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
