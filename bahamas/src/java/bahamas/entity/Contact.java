/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.ArrayList;
import java.util.Date;


public class Contact {


    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    private int contactId;
    private String contactType;
    private String username;
    private String password;
    private boolean isAdmin;
    private boolean deactivated;
    private Date dateCreated;
    private String createdBy;
    private String name;
    private String altName;
    private String explainIfOther;
    private String profession;
    private String jobTitle;
    private String nric;
    private String gender;
    private String nationality;
    private Date dateOfBirth;
    private String profilePic;
    private String remarks;
    
    private ArrayList<Phone> phoneList;
    private ArrayList<Address> addressList;
    private ArrayList<Email> emailList;
    private ArrayList<OfficeHeld> officeHeldList;
    private ArrayList<Proxy> proxyList;
    private ArrayList<Membership> membershipList;
    private ArrayList<LanguageAssignment> languageAssignmentList;
    private ArrayList<SkillAssignment> skillAssignmentList;
    private ArrayList<EventParticipation> eventParticipationList;
    private ArrayList<Appreciation> appreciationList;
    private ArrayList<Donation> donationList;
    private boolean notification;
     private ArrayList<TeamJoin> teamJoinList;
    
    // certification that a contact have
    private ArrayList<TrainingCertified> certificationList;
    // awarding certification to contacts
    private ArrayList<TrainingCertified> certificationAwarderList;
    
    
    public Contact(){}
    
    //created by Si Hao
    public Contact(int contactId, String contactType, String username, String password, boolean isAdmin, boolean deactivated, Date dateCreated, String createdBy, String name, String altName, String explainIfOther, String profession, String jobTitle, String nric, String gender, String nationality, Date dateOfBirth, String profilePic, String remarks, boolean notification) {
        this.contactId = contactId;
        this.contactType = contactType;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.deactivated = deactivated;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.name = name;
        this.altName = altName;
        this.explainIfOther = explainIfOther;
        this.profession = profession;
        this.jobTitle = jobTitle;
        this.nric = nric;
        this.gender = gender;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.profilePic = profilePic;
        this.remarks = remarks;
        this.notification = notification;
    }

    //note by Si Hao: to be removed
    public Contact(int contactId, String username, String password, boolean deactivated, Date dateCreated, String createdBy, String name, String altName, String explainIfOther, String profession, String jobTitle, String nric, String gender, String nationality, Date dateOfBirth, String profilePic, String remarks, boolean isAdmin, ArrayList<TeamJoin> teamJoinList, String contactType, ArrayList<Phone> phoneList, ArrayList<Address> addressList, ArrayList<Email> emailList, ArrayList<OfficeHeld> officeHeldList, ArrayList<Proxy> proxyList, ArrayList<Membership> membershipList, ArrayList<LanguageAssignment> languageAssignmentList, ArrayList<SkillAssignment> skillAssignmentList, ArrayList<EventParticipation> eventParticipationList, ArrayList<Appreciation> appreciationList, ArrayList<Donation> donationList, ArrayList<TrainingCertified> certificationList, ArrayList<TrainingCertified> certificationAwarderList) {
        this.contactId = contactId;
        this.username = username;
        this.password = password;
        this.deactivated = deactivated;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.name = name;
        this.altName = altName;
        this.explainIfOther = explainIfOther;
        this.profession = profession;
        this.jobTitle = jobTitle;
        this.nric = nric;
        this.gender = gender;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.profilePic = profilePic;
        this.remarks = remarks;
        this.isAdmin = isAdmin;
        this.teamJoinList = teamJoinList;
        this.contactType = contactType;
        this.phoneList = phoneList;
        this.addressList = addressList;
        this.emailList = emailList;
        this.officeHeldList = officeHeldList;
        this.proxyList = proxyList;
        this.membershipList = membershipList;
        this.languageAssignmentList = languageAssignmentList;
        this.skillAssignmentList = skillAssignmentList;
        this.eventParticipationList = eventParticipationList;
        this.appreciationList = appreciationList;
        this.donationList = donationList;
        this.certificationList = certificationList;
        this.certificationAwarderList = certificationAwarderList;
    }

    // note by Si Hao: to be removed
    //user with no login credentials
    public Contact(int contactId, Date dateCreated, String createdBy, String name, String altName, String explainIfOther, String profession, String jobTitle, String nric, String gender, String nationality, Date dateOfBirth, String profilePic, String remarks) {
        this.contactId = contactId;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.name = name;
        this.altName = altName;
        this.explainIfOther = explainIfOther;
        this.profession = profession;
        this.jobTitle = jobTitle;
        this.nric = nric;
        this.gender = gender;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.profilePic = profilePic;
        this.remarks = remarks;
    }

    public Contact(String contactType, boolean isAdmin, Date dateCreated, String createdBy, String name, String altName, String explainIfOther, String profession, String jobTitle, String nric, String gender, String nationality, Date dateOfBirth, String profilePic, String remarks) {
        this.contactType = contactType;
        this.isAdmin = isAdmin;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.name = name;
        this.altName = altName;
        this.explainIfOther = explainIfOther;
        this.profession = profession;
        this.jobTitle = jobTitle;
        this.nric = nric;
        this.gender = gender;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.profilePic = profilePic;
        this.remarks = remarks;
    }

    
    
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public String getExplainIfOther() {
        return explainIfOther;
    }

    public void setExplainIfOther(String explainIfOther) {
        this.explainIfOther = explainIfOther;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public ArrayList<TeamJoin> getTeamJoinList() {
        return teamJoinList;
    }

    public void setTeamJoinList(ArrayList<TeamJoin> teamJoinList) {
        this.teamJoinList = teamJoinList;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public ArrayList<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(ArrayList<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public ArrayList<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }

    public ArrayList<Email> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<Email> emailList) {
        this.emailList = emailList;
    }

    public ArrayList<OfficeHeld> getOfficeHeldList() {
        return officeHeldList;
    }

    public void setOfficeHeldList(ArrayList<OfficeHeld> officeHeldList) {
        this.officeHeldList = officeHeldList;
    }

    public ArrayList<Proxy> getProxyList() {
        return proxyList;
    }

    public void setProxyList(ArrayList<Proxy> proxyList) {
        this.proxyList = proxyList;
    }

    public ArrayList<Membership> getMembershipList() {
        return membershipList;
    }

    public void setMembershipList(ArrayList<Membership> membershipList) {
        this.membershipList = membershipList;
    }

    public ArrayList<LanguageAssignment> getLanguageAssignmentList() {
        return languageAssignmentList;
    }

    public void setLanguageAssignmentList(ArrayList<LanguageAssignment> languageAssignmentList) {
        this.languageAssignmentList = languageAssignmentList;
    }

    public ArrayList<SkillAssignment> getSkillAssignmentList() {
        return skillAssignmentList;
    }

    public void setSkillAssignmentList(ArrayList<SkillAssignment> skillAssignmentList) {
        this.skillAssignmentList = skillAssignmentList;
    }

    public ArrayList<EventParticipation> getEventParticipationList() {
        return eventParticipationList;
    }

    public void setEventParticipationList(ArrayList<EventParticipation> eventParticipationList) {
        this.eventParticipationList = eventParticipationList;
    }

    public ArrayList<Appreciation> getAppreciationList() {
        return appreciationList;
    }

    public void setAppreciationList(ArrayList<Appreciation> appreciationList) {
        this.appreciationList = appreciationList;
    }

    public ArrayList<Donation> getDonationList() {
        return donationList;
    }

    public void setDonationList(ArrayList<Donation> donationList) {
        this.donationList = donationList;
    }

    public ArrayList<TrainingCertified> getCertificationList() {
        return certificationList;
    }

    public void setCertificationList(ArrayList<TrainingCertified> certificationList) {
        this.certificationList = certificationList;
    }

    public ArrayList<TrainingCertified> getCertificationAwarderList() {
        return certificationAwarderList;
    }

    public void setCertificationAwarderList(ArrayList<TrainingCertified> certificationAwarderList) {
        this.certificationAwarderList = certificationAwarderList;
    }
    
    
    

    
    
}
