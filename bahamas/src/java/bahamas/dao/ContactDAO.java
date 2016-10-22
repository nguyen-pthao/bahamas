/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import com.google.gson.JsonArray;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,REMARKS,NOTIFICATION,USERNAME,PASSWORD,SALT)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

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
            stmt.setString(14, c.getRemarks());
            stmt.setBoolean(15, c.isNotification());
            stmt.setString(16, c.getUsername());
            stmt.setString(17, c.getPassword());
            stmt.setString(18, c.getSalt());

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
                    + "JOB_TITLE=?,NRIC_FIN=?,GENDER=?,NATIONALITY=?,DATE_OF_BIRTH=?,REMARKS=?,"
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
            stmt.setString(11, c.getRemarks());
            stmt.setBoolean(12, c.isNotification());
            stmt.setInt(13, c.getContactId());

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

    public static String updateImage(String username, String newImage) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String profilePic = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT PROFILE_PIC FROM CONTACT WHERE USERNAME=?");
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            while (rs.next()) {
                profilePic = rs.getString(1);
            }

            stmt = conn.prepareStatement("UPDATE CONTACT SET PROFILE_PIC=? WHERE USERNAME=?");
            stmt.setString(1, newImage);
            stmt.setString(2, username);

            if (stmt.executeUpdate() == 1) {
                return profilePic;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }

    public ArrayList<Contact> retrieveAllDonor() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        contactList = new ArrayList<Contact>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT DISTINCT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,"
                    + "ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,"
                    + "C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,"
                    + "DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, NOTIFICATION FROM CONTACT C, DONATION D "
                    + "WHERE C.CONTACT_ID = D.CONTACT_ID");

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

    public ArrayList<Contact> retrieveAllCurrentMember() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        contactList = new ArrayList<Contact>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT DISTINCT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,"
                    + "PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, NOTIFICATION "
                    + "FROM CONTACT C, MEMBERSHIP M "
                    + "WHERE C.CONTACT_ID = M.CONTACT_ID AND END_MEMBERSHIP >= CURDATE()");

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

    public ArrayList<Contact> retrieveAllExpiredMember() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        contactList = new ArrayList<Contact>();

        try {
            conn = ConnectionManager.getConnection();
            /*
             stmt = conn.prepareStatement("SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,"
             + "SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,"
             + "ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,"
             + "NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, NOTIFICATION "
             + "FROM CONTACT C, MEMBERSHIP M "
             + "WHERE C.CONTACT_ID = M.CONTACT_ID AND END_MEMBERSHIP < CURDATE() "
             + "GROUP BY C.CONTACT_ID");
             */

            stmt = conn.prepareStatement("SELECT CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS, NOTIFICATION FROM CONTACT WHERE CONTACT_ID IN (SELECT CONTACT_ID FROM MEMBERSHIP WHERE CONTACT_ID NOT IN (SELECT DISTINCT CONTACT_ID FROM MEMBERSHIP M WHERE END_MEMBERSHIP >= CURDATE()))");

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

    public ArrayList<Contact> retrieveAllContactInTeams(ArrayList<String> teamNameList) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String teamQuery = "";
        contactList = new ArrayList<Contact>();
        if (teamNameList != null && !teamNameList.isEmpty()) {

            for (int i = 0; i < teamNameList.size() - 1; i++) {
                teamQuery += "\"" + teamNameList.get(i) + "\", ";
            }
            teamQuery += "\"" + teamNameList.get(teamNameList.size() - 1) + "\"";

        }
        try {
            conn = ConnectionManager.getConnection();

            stmt = conn.prepareStatement("SELECT DISTINCT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, "
                    + "NOTIFICATION FROM CONTACT C, TEAM_JOIN T "
                    + "WHERE C.CONTACT_ID = T.CONTACT_ID AND TEAM_NAME IN (" + teamQuery + ") AND "
                    + "PERMISSION IS NOT NULL AND IF(DATE_OBSOLETE IS NOT NULL,DATE_OBSOLETE >= CURDATE(),TRUE)");

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

    /*
     public ArrayList<Contact> retrieveAllContactInATeam(String teamName) {
     Connection conn = null;
     PreparedStatement stmt = null;
     ResultSet rs = null;
        
     try {
     conn = ConnectionManager.getConnection();
            
     stmt = conn.prepareStatement("SELECT DISTINCT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
     + "DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,"
     + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, "
     + "NOTIFICATION FROM CONTACT C, TEAM_JOIN T "
     + "WHERE C.CONTACT_ID = T.CONTACT_ID AND TEAM_NAME = \"" + teamName + "\" AND "
     + "PERMISSION IS NOT NULL AND IF(DATE_OBSOLETE IS NOT NULL,DATE_OBSOLETE >= CURDATE(),TRUE)");
            
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
     */
    //return everything
    //SELECT * FROM (SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, C.NOTIFICATION, E.EMAIL FROM CONTACT C LEFT OUTER JOIN EMAIL E ON C.CONTACT_ID = E.CONTACT_ID) AS T1 LEFT OUTER JOIN (SELECT C.CONTACT_ID, COUNTRY_CODE, PHONE_NUMBER FROM CONTACT C LEFT OUTER JOIN PHONE P ON C.CONTACT_ID = P.CONTACT_ID) AS T2 ON T1.CONTACT_ID = T2.CONTACT_ID ORDER BY T1.CONTACT_ID
    public ArrayList<Contact> retrieveAllContactWithEmailPhone() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        contactList = new ArrayList<Contact>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, C.NOTIFICATION, EMAIL_LIST, PHONE_LIST, EMAIL_LIST2, LAST_LOGIN FROM CONTACT C LEFT OUTER JOIN (SELECT C.CONTACT_ID, GROUP_CONCAT(CONCAT(EMAIL)SEPARATOR ' | ') AS EMAIL_LIST, GROUP_CONCAT(CONCAT(NAME,'<',EMAIL,'>') SEPARATOR '; ') AS EMAIL_LIST2 FROM CONTACT C, EMAIL E WHERE C.CONTACT_ID = E.CONTACT_ID AND IF(E.DATE_OBSOLETE IS NOT NULL,E.DATE_OBSOLETE >= CURDATE(),TRUE) GROUP BY CONTACT_ID) AS T1 ON C.CONTACT_ID = T1.CONTACT_ID LEFT OUTER JOIN (SELECT C.CONTACT_ID, GROUP_CONCAT(CONCAT('+' ,COUNTRY_CODE,'-',PHONE_NUMBER)SEPARATOR ' | ') AS PHONE_LIST FROM CONTACT C, PHONE P WHERE C.CONTACT_ID = P.CONTACT_ID AND IF(P.DATE_OBSOLETE IS NOT NULL,P.DATE_OBSOLETE >= CURDATE(),TRUE) GROUP BY CONTACT_ID) AS T2 ON C.CONTACT_ID = T2.CONTACT_ID");

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
                String emailList = rs.getString(23);
                String phoneList = rs.getString(24);
                String emailList2 = rs.getString(25);
                Date lastLogin = rs.getDate(26);

                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification, emailList, phoneList, emailList2, lastLogin);
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

    public static boolean updateLastLogin(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE CONTACT SET LAST_LOGIN=? WHERE USERNAME=?");

            stmt.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
            stmt.setString(2, username);

            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }

    public ArrayList<Contact> retrieveAllContactWithEmailPhone(Date userCreatedStartDateFencing, Date userCreatedEndDateFencing, Date userLoginStartDate, Date userLoginEndDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        contactList = new ArrayList<Contact>();
        String statement = "SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,"
                + "C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,"
                + "C.REMARKS, C.NOTIFICATION, EMAIL_LIST, PHONE_LIST, EMAIL_LIST2, LAST_LOGIN FROM CONTACT C "
                + "LEFT OUTER JOIN (SELECT C.CONTACT_ID, GROUP_CONCAT(CONCAT(EMAIL)SEPARATOR ' | ') AS EMAIL_LIST, "
                + "GROUP_CONCAT(CONCAT(NAME,'<',EMAIL,'>') SEPARATOR '; ') AS EMAIL_LIST2 "
                + "FROM CONTACT C, EMAIL E WHERE C.CONTACT_ID = E.CONTACT_ID "
                + "AND IF(E.DATE_OBSOLETE IS NOT NULL,E.DATE_OBSOLETE >= CURDATE(),TRUE) "
                + "GROUP BY CONTACT_ID) AS T1 ON C.CONTACT_ID = T1.CONTACT_ID "
                + "LEFT OUTER JOIN (SELECT C.CONTACT_ID, GROUP_CONCAT(CONCAT('+' ,COUNTRY_CODE,'-',PHONE_NUMBER)SEPARATOR ' | ') AS PHONE_LIST "
                + "FROM CONTACT C, PHONE P WHERE C.CONTACT_ID = P.CONTACT_ID AND IF(P.DATE_OBSOLETE IS NOT NULL,P.DATE_OBSOLETE >= CURDATE(),TRUE) "
                + "GROUP BY CONTACT_ID) AS T2 ON C.CONTACT_ID = T2.CONTACT_ID AND USERNAME IS NOT NULL";
        boolean isFirst = true;

        if (userCreatedStartDateFencing != null && userCreatedEndDateFencing != null) {
            if (isFirst) {
                statement += " WHERE DATE(C.DATE_CREATED) >= ? AND DATE(C.DATE_CREATED) <= ? ";
                isFirst = false;
            } else {
                statement += " AND DATE(C.DATE_CREATED) >= ? AND DATE(C.DATE_CREATED) <= ? ";
            }
        } else if (userCreatedStartDateFencing != null) {
            if (isFirst) {
                statement += " WHERE DATE(C.DATE_CREATED) >= ? ";
                isFirst = false;
            } else {
                statement += " AND DATE(C.DATE_CREATED) >= ? ";
            }
        } else if (userCreatedEndDateFencing != null) {
            if (isFirst) {
                statement += " WHERE DATE(C.DATE_CREATED) <= ? ";
                isFirst = false;
            } else {
                statement += " AND DATE(C.DATE_CREATED) <= ? ";
            }
        }

        if (userLoginStartDate != null && userLoginEndDate != null) {
            if (isFirst) {
                statement += " WHERE DATE(LAST_LOGIN) >= ? AND DATE(LAST_LOGIN) <= ? ";
                isFirst = false;
            } else {
                statement += " AND DATE(LAST_LOGIN) >= ? AND DATE(LAST_LOGIN) <= ? ";
            }
        } else if (userLoginStartDate != null) {
            if (isFirst) {
                statement += " WHERE DATE(LAST_LOGIN) >= ? ";
                isFirst = false;
            } else {
                statement += " AND DATE(LAST_LOGIN) >= ? ";
            }
        } else if (userLoginEndDate != null) {
            if (isFirst) {
                statement += " WHERE DATE(LAST_LOGIN) <= ? ";
                isFirst = false;
            } else {
                statement += " AND DATE(LAST_LOGIN) <= ? ";
            }
        }
        
        statement = "SELECT * FROM (" + statement + ") AS TEMP WHERE USERNAME IS NOT NULL";
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(statement);
            int count = 1;
            try {

                if (userCreatedStartDateFencing != null && userCreatedEndDateFencing != null) {
                    stmt.setDate(count++, new java.sql.Date(userCreatedStartDateFencing.getTime()));
                    stmt.setDate(count++, new java.sql.Date(userCreatedEndDateFencing.getTime()));

                } else if (userCreatedStartDateFencing != null) {
                    stmt.setDate(count++, new java.sql.Date(userCreatedStartDateFencing.getTime()));
                } else if (userCreatedEndDateFencing != null) {
                    stmt.setDate(count++, new java.sql.Date(userCreatedEndDateFencing.getTime()));
                }
                if (userLoginStartDate != null && userLoginEndDate != null) {
                    stmt.setDate(count++, new java.sql.Date(userLoginStartDate.getTime()));
                    stmt.setDate(count++, new java.sql.Date(userLoginEndDate.getTime()));
                } else if (userLoginStartDate != null) {
                    stmt.setDate(count++, new java.sql.Date(userLoginStartDate.getTime()));
                } else if (userLoginEndDate != null) {
                    stmt.setDate(count++, new java.sql.Date(userLoginEndDate.getTime()));
                }

            } catch (SQLException ex) {
                Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, null, ex);

            }

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
                String emailList = rs.getString(23);
                String phoneList = rs.getString(24);
                String emailList2 = rs.getString(25);
                Date lastLogin = rs.getDate(26);

                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification, emailList, phoneList, emailList2, lastLogin);
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

    public boolean updateToDeactive(JsonArray contactIdJsonArray) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement("UPDATE CONTACT SET DEACTIVATED=? WHERE CONTACT_ID= ? ");

            for (int i = 0; i < contactIdJsonArray.size(); i++) {
                String contactIdTemp = contactIdJsonArray.get(i).getAsString();
                stmt.setBoolean(1, true);
                stmt.setString(2, contactIdTemp);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }

    public boolean updateToActive(JsonArray contactIdJsonArray) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement("UPDATE CONTACT SET DEACTIVATED=? WHERE CONTACT_ID= ? ");

            for (int i = 0; i < contactIdJsonArray.size(); i++) {
                String contactIdTemp = contactIdJsonArray.get(i).getAsString();
                stmt.setBoolean(1, false);
                stmt.setString(2, contactIdTemp);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }

}
