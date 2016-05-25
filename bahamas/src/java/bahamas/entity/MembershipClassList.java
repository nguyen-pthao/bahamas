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
public class MembershipClassList {
    
    private ArrayList<String> MembershipClassNameList;

    public MembershipClassList(ArrayList<String> MembershipClassNameList) {
        this.MembershipClassNameList = MembershipClassNameList;
    }

    public ArrayList<String> getMembershipClassNameList() {
        return MembershipClassNameList;
    }

    public void setMembershipClassNameList(ArrayList<String> MembershipClassNameList) {
        this.MembershipClassNameList = MembershipClassNameList;
    }
    
}
