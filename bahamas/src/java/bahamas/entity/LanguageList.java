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
public class LanguageList {
    
    private ArrayList<String> LanguageNameList;

    public LanguageList(ArrayList<String> LanguageNameList) {
        this.LanguageNameList = LanguageNameList;
    }

    public ArrayList<String> getLanguageNameList() {
        return LanguageNameList;
    }

    public void setLanguageNameList(ArrayList<String> LanguageNameList) {
        this.LanguageNameList = LanguageNameList;
    }
    
    
}
