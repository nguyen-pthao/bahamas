package bahamas.entity;

import bahamas.entity.LsaClassList;
import java.util.*;
import java.util.Date;


public class SkillAssignment  implements java.io.Serializable {

     private int contactId;
     private String skillName;
     private LsaClassList lsaClassList;
     private Date dateCreated;
     private String createdBy;
     private String explainIfOther;
     private Date dateObsolete;
     private String remarks;

    public SkillAssignment() {
    }

    public SkillAssignment(int contactId, String skillName, LsaClassList lsaClassList, Date dateCreated, String createdBy, String explainIfOther, Date dateObsolete, String remarks) {
        this.contactId = contactId;
        this.skillName = skillName;
        this.lsaClassList = lsaClassList;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.explainIfOther = explainIfOther;
        this.dateObsolete = dateObsolete;
        this.remarks = remarks;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public LsaClassList getLsaClassList() {
        return lsaClassList;
    }

    public void setLsaClassList(LsaClassList lsaClassList) {
        this.lsaClassList = lsaClassList;
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


