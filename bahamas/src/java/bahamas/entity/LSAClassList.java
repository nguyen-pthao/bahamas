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
public class LSAClassList {
    
    private ArrayList<String> skillNameList;

    public LSAClassList(ArrayList<String> skillNameList) {
        this.skillNameList = skillNameList;
    }

    public ArrayList<String> getSkillNameList() {
        return skillNameList;
    }

    public void setSkillNameList(ArrayList<String> skillNameList) {
        this.skillNameList = skillNameList;
    }

}
