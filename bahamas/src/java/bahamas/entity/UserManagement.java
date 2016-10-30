/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.ArrayList;

/**
 *
 * @author tan.si.hao
 */
public class UserManagement {
    private Contact contact;
    private String [] teamList;
    private String email;

    public UserManagement(Contact contact, String teams, String email) {
        this.contact = contact;
        this.teamList = teams.split("\\|");
        this.email = email;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String[] getTeamList() {
        return teamList;
    }

    public void setTeamList(String[] teamList) {
        this.teamList = teamList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
