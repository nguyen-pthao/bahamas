/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Phone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author HUXLEY
 */
public class PhoneDAO {

    private ArrayList<Phone> phoneList;

    public PhoneDAO() {
    }

    public static boolean addPhone(Phone p) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO PHONE (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,COUNTRY_CODE,PHONE_NUMBER,REMARKS,DATE_OBSOLETE)"
                    + " VALUES (?,?,?,?,?,?,?)");

            stmt.setInt(1, p.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(p.getDateCreated().getTime()));
            stmt.setString(3, p.getCreatedBy());
            stmt.setInt(4, p.getCountryCode());
            stmt.setInt(5, p.getPhoneNumber());
            stmt.setString(6, p.getRemarks());
            
            if(p.getDateObsolete() != null){
                stmt.setDate(7, new java.sql.Date(p.getDateObsolete().getTime()));
            }   

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public ArrayList<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(ArrayList<Phone> phoneList) {
        this.phoneList = phoneList;
    }

}
