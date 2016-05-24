package bahamas.entity;

import java.util.*;
import java.util.Date;


public class TeamJoin  implements java.io.Serializable {

     private int contactId;
     private String teamName;
     private String permission;
     private PermissionLevelList permissionLevelList;
     private TeamAffiliationList teamAffiliationList;
     private Date dateCreated;
     private String createdBy;
     private String explainIfOther;
     private String subteam;
     private Date dateObsolete;
     private String remarks;

    public TeamJoin() {
    }

    public TeamJoin(int contactId, String teamName, String permission, PermissionLevelList permissionLevelList, TeamAffiliationList teamAffiliationList, Date dateCreated, String createdBy, String explainIfOther, String subteam, Date dateObsolete, String remarks) {
        this.contactId = contactId;
        this.teamName = teamName;
        this.permission = permission;
        this.permissionLevelList = permissionLevelList;
        this.teamAffiliationList = teamAffiliationList;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.explainIfOther = explainIfOther;
        this.subteam = subteam;
        this.dateObsolete = dateObsolete;
        this.remarks = remarks;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public PermissionLevelList getPermissionLevelList() {
        return permissionLevelList;
    }

    public void setPermissionLevelList(PermissionLevelList permissionLevelList) {
        this.permissionLevelList = permissionLevelList;
    }

    public TeamAffiliationList getTeamAffiliationList() {
        return teamAffiliationList;
    }

    public void setTeamAffiliationList(TeamAffiliationList teamAffiliationList) {
        this.teamAffiliationList = teamAffiliationList;
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

    public String getExplainIfOther() {
        return explainIfOther;
    }

    public void setExplainIfOther(String explainIfOther) {
        this.explainIfOther = explainIfOther;
    }

    public String getSubteam() {
        return subteam;
    }

    public void setSubteam(String subteam) {
        this.subteam = subteam;
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


