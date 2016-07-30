/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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
public class ContactDAO {

    private ArrayList<Contact> contactList;
    private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ContactDAO() {
    }

    public ArrayList<Contact> retrieveAllContact() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        contactList = new ArrayList<Contact>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "from CONTACT");

            rs = stmt.executeQuery();
            while (rs.next()) {

                int contactId = Integer.parseInt(rs.getString(1));
                String contactType = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String salt = rs.getString(5);
                boolean isAdmin = rs.getBoolean(6);
                boolean isNovice = rs.getBoolean(7);
                boolean deactivated = rs.getBoolean(8);
                String dateStr = rs.getString(9);
                Date dateCreated = datetime.parse(dateStr);
                String createdBy = rs.getString(10);
                String name = rs.getString(11);
                String altName = rs.getString(12);
                String explainIfOther = rs.getString(13);
                String profession = rs.getString(14);
                String jobTitle = rs.getString(15);
                String nric = rs.getString(16);
                String gender = rs.getString(17);
                String nationality = rs.getString(18);
                Date dateOfBirth = rs.getDate(19);
                String profilePic = rs.getString(20);
                String remarks = rs.getString(21);
                boolean notification = rs.getBoolean(22);

                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                contactList.add(contact);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return contactList;
    }

    public Contact retrieveContactByUsername(String usernameInput) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT WHERE USERNAME = (?)");
            stmt.setString(1, usernameInput);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int contactId = Integer.parseInt(rs.getString(1));
                String contactType = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String salt = rs.getString(5);
                boolean isAdmin = rs.getBoolean(6);
                boolean isNovice = rs.getBoolean(7);
                boolean deactivated = rs.getBoolean(8);
                String dateStr = rs.getString(9);
                Date dateCreated = datetime.parse(dateStr);
                String createdBy = rs.getString(10);
                String name = rs.getString(11);
                String altName = rs.getString(12);
                String explainIfOther = rs.getString(13);
                String profession = rs.getString(14);
                String jobTitle = rs.getString(15);
                String nric = rs.getString(16);
                String gender = rs.getString(17);
                String nationality = rs.getString(18);
                Date dateOfBirth = rs.getDate(19);
                String profilePic = rs.getString(20);
                String remarks = rs.getString(21);
                boolean notification = rs.getBoolean(22);

                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                return contact;
            }
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }
    
    public Contact retrieveContactByNric(String nric_fin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT WHERE NRIC_FIN = (?)");
            stmt.setString(1, nric_fin);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int contactId = Integer.parseInt(rs.getString(1));
                String contactType = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String salt = rs.getString(5);
                boolean isAdmin = rs.getBoolean(6);
                boolean isNovice = rs.getBoolean(7);
                boolean deactivated = rs.getBoolean(8);
                String dateStr = rs.getString(9);
                Date dateCreated = datetime.parse(dateStr);
                String createdBy = rs.getString(10);
                String name = rs.getString(11);
                String altName = rs.getString(12);
                String explainIfOther = rs.getString(13);
                String profession = rs.getString(14);
                String jobTitle = rs.getString(15);
                String nric = rs.getString(16);
                String gender = rs.getString(17);
                String nationality = rs.getString(18);
                Date dateOfBirth = rs.getDate(19);
                String profilePic = rs.getString(20);
                String remarks = rs.getString(21);
                boolean notification = rs.getBoolean(22);

                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                return contact;
            }
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }

    public Contact retrieveContactById(int id) {
        contactList = new ArrayList<Contact>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT WHERE CONTACT_ID = (?)");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int contactId = Integer.parseInt(rs.getString(1));
                String contactType = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String salt = rs.getString(5);
                boolean isAdmin = rs.getBoolean(6);
                boolean isNovice = rs.getBoolean(7);
                boolean deactivated = rs.getBoolean(8);
                String dateStr = rs.getString(9);
                Date dateCreated = datetime.parse(dateStr);
                String createdBy = rs.getString(10);
                String name = rs.getString(11);
                String altName = rs.getString(12);
                String explainIfOther = rs.getString(13);
                String profession = rs.getString(14);
                String jobTitle = rs.getString(15);
                String nric = rs.getString(16);
                String gender = rs.getString(17);
                String nationality = rs.getString(18);
                Date dateOfBirth = rs.getDate(19);
                String profilePic = rs.getString(20);
                String remarks = rs.getString(21);
                boolean notification = rs.getBoolean(22);

                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                return contact;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }

    public static int addContact(Contact c) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int autoIncKeyFromApi = -1;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO CONTACT (CONTACT_TYPE,ISADMIN,"
                    + "DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION,USERNAME,PASSWORD,SALT)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, c.getContactType());
            stmt.setBoolean(2, c.isIsAdmin());
            stmt.setTimestamp(3, new java.sql.Timestamp(c.getDateCreated().getTime()));
            stmt.setString(4, c.getCreatedBy());
            stmt.setString(5, c.getName());
            stmt.setString(6, c.getAltName());
            stmt.setString(7, c.getExplainIfOther());
            stmt.setString(8, c.getProfession());
            stmt.setString(9, c.getJobTitle());
            stmt.setString(10, c.getNric());
            stmt.setString(11, c.getGender());
            stmt.setString(12, c.getNationality());
            if (c.getDateOfBirth() != null) {
                stmt.setDate(13, new java.sql.Date(c.getDateOfBirth().getTime()));
            } else {
                stmt.setDate(13, null);
            }
            stmt.setString(14, c.getProfilePic());
            stmt.setString(15, c.getRemarks());
            stmt.setBoolean(16, c.isNotification());
            stmt.setString(17, c.getUsername());
            stmt.setString(18, c.getPassword());
            stmt.setString(19, c.getSalt());

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);
            }

            return autoIncKeyFromApi;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return autoIncKeyFromApi;
    }

    public static boolean changeNovicePermission(Contact c, boolean isNovice) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE CONTACT SET ISNOVICE = (?) WHERE CONTACT_ID = (?)");
            stmt.setBoolean(1, isNovice);
            stmt.setInt(2, c.getContactId());

            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteContact(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM CONTACT WHERE CONTACT_ID = (?)");
            stmt.setInt(1, id);

            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }

    public static boolean updateContact(Contact c) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE CONTACT SET CONTACT_TYPE=?,"
                    + "NAME=?,ALT_NAME=?,EXPLAIN_IF_OTHER=?,PROFESSION=?,"
                    + "JOB_TITLE=?,NRIC_FIN=?,GENDER=?,NATIONALITY=?,DATE_OF_BIRTH=?,PROFILE_PIC=?,REMARKS=?,"
                    + "NOTIFICATION=? WHERE CONTACT_ID=?");
                  
            stmt.setString(1, c.getContactType());        
            stmt.setString(2, c.getName());
            stmt.setString(3, c.getAltName());
            stmt.setString(4, c.getExplainIfOther());
            stmt.setString(5, c.getProfession());
            stmt.setString(6, c.getJobTitle());
            stmt.setString(7, c.getNric());
            stmt.setString(8, c.getGender());
            stmt.setString(9, c.getNationality());
            if (c.getDateOfBirth() != null) {
                stmt.setDate(10, new java.sql.Date(c.getDateOfBirth().getTime()));
            } else {
                stmt.setDate(10, null);
            }
            stmt.setString(11, c.getProfilePic());
            stmt.setString(12, c.getRemarks());     
            stmt.setBoolean(13, c.isNotification());
            stmt.setInt(14, c.getContactId());

            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    public static boolean updateUser(Contact c) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE CONTACT SET ISADMIN=?,DEACTIVATED=?,"                 
                    + "USERNAME=?,PASSWORD=?,SALT=?,NOTIFICATION=? WHERE CONTACT_ID=?");

            
            stmt.setBoolean(1, c.isIsAdmin());          
            stmt.setBoolean(2, c.isDeactivated());        
            stmt.setString(3, c.getUsername());
            stmt.setString(4, c.getPassword());
            stmt.setString(5, c.getSalt());
            stmt.setBoolean(6, c.isNotification());
            stmt.setInt(7, c.getContactId());

            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

}
