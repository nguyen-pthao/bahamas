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
public class TeamJoin {

    private String username;
    private String teamName;
    private Date dateCreated = new java.util.Date();
    private String createdBy;
    private String explainIfOthers;
    private String subTeam;
    private Date dateObsolete;
    private String remarks;
    private String permission;

    public TeamJoin(String username, String teamName, Date dateCreated, String createdBy, String explainIfOthers, String subTeam, Date dateObsolete, String remarks, String permission) {
        this.username = username;
        this.teamName = teamName;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.explainIfOthers = explainIfOthers;
        this.subTeam = subTeam;
        this.dateObsolete = dateObsolete;
        this.remarks = remarks;
        this.permission = permission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getExplainIfOthers() {
        return explainIfOthers;
    }

    public void setExplainIfOthers(String explainIfOthers) {
        this.explainIfOthers = explainIfOthers;
    }

    public String getSubTeam() {
        return subTeam;
    }

    public void setSubTeam(String subTeam) {
        this.subTeam = subTeam;
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

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    

    
    
}
