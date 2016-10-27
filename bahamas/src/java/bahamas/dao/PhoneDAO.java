/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.entity.Phone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            stmt.setString(5, p.getPhoneNumber());
            stmt.setString(6, p.getRemarks());

            if (p.getDateObsolete() != null) {
                stmt.setDate(7, new java.sql.Date(p.getDateObsolete().getTime()));
            } else {
                stmt.setDate(7, null);
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

    public static ArrayList<Phone> retrieveAllPhone(Contact contact) {
        ArrayList<Phone> phoneList = new ArrayList<Phone>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int cid = contact.getContactId();
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT COUNTRY_CODE, PHONE_NUMBER, REMARKS, CREATED_BY, DATE_CREATED, DATE_OBSOLETE FROM PHONE WHERE CONTACT_ID = (?) ORDER BY DATE_CREATED DESC");
            stmt.setString(1, Integer.toString(cid));
            rs = stmt.executeQuery();
            while (rs.next()) {

                int countryCode = rs.getInt(1);
                String phoneNumber = rs.getString(2);
                String remarks = rs.getString(3);
                String createdBy = rs.getString(4);
                String dateStr = rs.getString(5);
                Date dateCreated = datetime.parse(dateStr);
                String dateobs = rs.getString(6);
                Date dateObsolete = null;
                if (dateobs != null && !dateobs.isEmpty()) {
                    dateObsolete = date.parse(dateobs);
                }

                Phone p = new Phone(countryCode, phoneNumber, remarks, createdBy, dateCreated, dateObsolete);

                phoneList.add(p);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return phoneList;

    }

    public ArrayList<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(ArrayList<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public static boolean updatePhone(Phone p) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE PHONE SET "
                    + "COUNTRY_CODE=?,REMARKS=?,DATE_OBSOLETE=? "
                    + "WHERE CONTACT_ID=? AND PHONE_NUMBER=?");

            stmt.setInt(1, p.getCountryCode());
            stmt.setString(2, p.getRemarks());

            if (p.getDateObsolete() != null) {
                stmt.setDate(3, new java.sql.Date(p.getDateObsolete().getTime()));
            } else {
                stmt.setDate(3, null);
            }

            stmt.setInt(4, p.getContact().getContactId());
            stmt.setString(5, p.getPhoneNumber());
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deletePhone(int id, String phone) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM PHONE "
                    + "WHERE CONTACT_ID=? AND PHONE_NUMBER=?");

            stmt.setInt(1, id);
            stmt.setString(2, phone);
   
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
     
    public static boolean phoneExist(int id, int countryCode, String phone) {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM PHONE WHERE COUNTRY_CODE = (?) AND PHONE_NUMBER = (?) AND CONTACT_ID = (?)");            
            stmt.setInt(1, countryCode);
            stmt.setString(2, phone);
            stmt.setInt(3, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int count = rs.getInt(1);
                if(count >= 1){
                    exist = true;
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return exist;
    }
     
    public static ArrayList<Phone> retrieveAllPhone() {
        ArrayList<Phone> phoneList = new ArrayList<Phone>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID, COUNTRY_CODE, PHONE_NUMBER, REMARKS, CREATED_BY, DATE_CREATED, DATE_OBSOLETE FROM PHONE ");

            rs = stmt.executeQuery();
            while (rs.next()) {
                int cid = rs.getInt(1);
                int countryCode = rs.getInt(2);
                String phoneNumber = rs.getString(3);
                String remarks = rs.getString(4);
                String createdBy = rs.getString(5);
                String dateStr = rs.getString(6);
                Date dateCreated = datetime.parse(dateStr);
                String dateobs = rs.getString(7);
                Date dateObsolete = null;
                if (dateobs != null && !dateobs.isEmpty()) {
                    dateObsolete = date.parse(dateobs);
                }

                Phone p = new Phone(cid, countryCode, phoneNumber, createdBy, dateCreated, remarks, dateObsolete);

                phoneList.add(p);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return phoneList;

    }
    
}
