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
public class ModeOfSendingReceiptList {
    
    private ArrayList<String> receiptModeNameList;

    public ModeOfSendingReceiptList(ArrayList<String> receiptModeNameList) {
        this.receiptModeNameList = receiptModeNameList;
    }
    

    public ArrayList<String> getReceiptModeNameList() {
        return receiptModeNameList;
    }

    public void setReceiptModeNameList(ArrayList<String> receiptModeNameList) {
        this.receiptModeNameList = receiptModeNameList;
    }
    
    
}
