/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.ArrayList;

/**
 *
 * @author Darryl Mok
 */
public class ContactTypeList {
    
    private ArrayList<String> contactTypeList;

    public ContactTypeList(ArrayList<String> contactTypeList) {
        this.contactTypeList = contactTypeList;
    }

    public ArrayList<String> getContactTypeList() {
        return contactTypeList;
    }

    public void setContactTypeList(ArrayList<String> contactTypeList) {
        this.contactTypeList = contactTypeList;
    }
    
}
