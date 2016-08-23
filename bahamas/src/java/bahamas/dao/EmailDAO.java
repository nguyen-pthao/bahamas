/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.*;
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
public class EmailDAO {

    public static boolean addEmail(Email e, String hashID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO EMAIL (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,EMAIL,REMARKS,DATE_OBSOLETE,VERIFICATIONID)"
                    + " VALUES (?,?,?,?,?,?,?)");

            stmt.setInt(1, e.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(e.getDateCreated().getTime()));
            stmt.setString(3, e.getCreatedBy());
            stmt.setString(4, e.getEmail());
            stmt.setString(5, e.getRemarks());

            if (e.getDateObsolete() != null) {
                stmt.setDate(6, new java.sql.Date(e.getDateObsolete().getTime()));
            } else {
                stmt.setDate(6, null);
            }
            stmt.setString(7, hashID);
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static ArrayList<Email> retrieveAllEmail(Contact contact) {
        ArrayList<Email> emailList = new ArrayList<Email>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int cid = contact.getContactId();
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT DATE_CREATED, CREATED_BY, EMAIL, REMARKS, DATE_OBSOLETE, VERIFIED FROM EMAIL WHERE CONTACT_ID = (?) ORDER BY DATE_CREATED DESC");
            stmt.setString(1, Integer.toString(cid));
            rs = stmt.executeQuery();
            while (rs.next()) {

                String dateStr = rs.getString(1);
                Date dateCreated = datetime.parse(dateStr);
                String createdBy = rs.getString(2);
                String email = rs.getString(3);
                String remarks = rs.getString(4);
                String dateobs = rs.getString(5);
                Date dateObsolete = null;
                if (dateobs != null && !dateobs.isEmpty()) {
                    dateObsolete = date.parse(dateobs);
                }
                boolean verified = rs.getBoolean(6);
                ContactDAO cDAO = new ContactDAO();
                Contact c = cDAO.retrieveContactById(cid);
                
                Email e = new Email(c,email, createdBy, remarks, dateObsolete, dateCreated, verified);

                emailList.add(e);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EMAIL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return emailList;

    }

    public static boolean updateEmail(Email e) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE EMAIL SET REMARKS=?,DATE_OBSOLETE=?"
                    + " WHERE CONTACT_ID=? AND EMAIL=?");

            stmt.setString(1, e.getRemarks());

            if (e.getDateObsolete() != null) {
                stmt.setDate(2, new java.sql.Date(e.getDateObsolete().getTime()));
            } else {
                stmt.setDate(2, null);
            }
            stmt.setInt(3, e.getContact().getContactId());
            stmt.setString(4, e.getEmail());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteEmail(int id, String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM EMAIL"
                    + " WHERE CONTACT_ID=? AND EMAIL=?");

            stmt.setInt(1, id);
            stmt.setString(2, email);

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static Email retrieveEmail(String e) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID,DATE_CREATED, CREATED_BY, EMAIL, REMARKS, DATE_OBSOLETE, VERIFIED FROM EMAIL WHERE EMAIL = (?)");
            stmt.setString(1, e);
            rs = stmt.executeQuery();
            while (rs.next()) {

                int cid = rs.getInt(1);
                String dateStr = rs.getString(2);
                Date dateCreated = datetime.parse(dateStr);
                String createdBy = rs.getString(3);
                String email = rs.getString(4);
                String remarks = rs.getString(5);
                String dateobs = rs.getString(6);
                Date dateObsolete = null;
                if (dateobs != null && !dateobs.isEmpty()) {
                    dateObsolete = date.parse(dateobs);
                }
                boolean verified = rs.getBoolean(7);
                ContactDAO cDAO = new ContactDAO();
                Contact c = cDAO.retrieveContactById(cid);
                Email temp = new Email(c, email, createdBy, remarks, dateObsolete, dateCreated, verified);

                return temp;
            }

            return null;

        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EMAIL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return null;

    }

    //UPDATE EMAIL SET VERIFIED = 1 WHERE VERIFICATIONID = ""
    public static boolean updateVerification(String verificationID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE EMAIL SET VERIFIED = 1, VERIFICATIONID = NULL WHERE VERIFICATIONID = ?");

            stmt.setString(1, verificationID);

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean checkVerificationID(String verificationID) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean verified = true;
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT VERIFIED FROM EMAIL WHERE VERIFICATIONID = (?)");
            stmt.setString(1, verificationID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                verified = rs.getBoolean(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EMAIL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return verified;
    }

    public static boolean emailExist(String email) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM EMAIL WHERE EMAIL = (?)");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int count = rs.getInt(1);
                if (count >= 1) {
                    exist = true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EMAIL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return exist;
    }
    
    public static ArrayList<Email> retrieveAllEmail() {
        ArrayList<Email> emailList = new ArrayList<Email>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID, DATE_CREATED, CREATED_BY, EMAIL, REMARKS, DATE_OBSOLETE, VERIFIED FROM EMAIL");
            rs = stmt.executeQuery();
            while (rs.next()) {
                int contactId = rs.getInt(1);
                String dateStr = rs.getString(2);
                Date dateCreated = datetime.parse(dateStr);
                String createdBy = rs.getString(3);
                String email = rs.getString(4);
                String remarks = rs.getString(5);
                String dateobs = rs.getString(6);
                Date dateObsolete = null;
                if (dateobs != null && !dateobs.isEmpty()) {
                    dateObsolete = date.parse(dateobs);
                }
                boolean verified = rs.getBoolean(7);
                
                Email e = new Email(contactId ,email, createdBy, remarks, dateObsolete, dateCreated, verified);

                emailList.add(e);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EMAIL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return emailList;

    }

}
