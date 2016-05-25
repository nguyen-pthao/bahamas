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
public class PaymentModeList {
    
    private ArrayList<String> PaymentModeNameList;

    public PaymentModeList(ArrayList<String> PaymentModeNameList) {
        this.PaymentModeNameList = PaymentModeNameList;
    }

    public ArrayList<String> getPaymentModeNameList() {
        return PaymentModeNameList;
    }

    public void setPaymentModeNameList(ArrayList<String> PaymentModeNameList) {
        this.PaymentModeNameList = PaymentModeNameList;
    }
    
    
    
}
