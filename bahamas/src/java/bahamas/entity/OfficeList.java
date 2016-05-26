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
public class OfficeList {
    
    private ArrayList<String> officeList;

    public OfficeList(ArrayList<String> officeList) {
        this.officeList = officeList;
    }

    public ArrayList<String> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(ArrayList<String> officeList) {
        this.officeList = officeList;
    }
    
}
