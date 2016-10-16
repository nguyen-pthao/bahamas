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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tan.si.hao
 */
public class SearchContactDAO {

    private static SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static HashMap<Integer, Contact> searchContactByNameAltnameNationality(String nameValue, String altNameValue, String nationalityValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Contact> contactHM = new HashMap<Integer, Contact>();
        boolean isFirst = true;
        String prepareStatement = "Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT";
        
        
        try {
            conn = ConnectionManager.getConnection();
            /*
            stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT WHERE NAME LIKE ? AND ALT_NAME LIKE ? AND NATIONALITY LIKE ?");
            */
            if (nameValue != null) {
                prepareStatement += " WHERE NAME LIKE ? ";
                isFirst = false;
            }
            if (altNameValue != null) {
                if(!isFirst){
                    prepareStatement += "AND ALT_NAME LIKE ? ";
                } else {
                    prepareStatement += " WHERE ALT_NAME LIKE ? ";
                }
            }
            if (nationalityValue != null) {
                if(!isFirst){
                    prepareStatement += "AND NATIONALITY LIKE ? ";
                } else {
                    prepareStatement += " WHERE NATIONALITY LIKE ? ";
                }
            }
            
            stmt = conn.prepareStatement(prepareStatement);
            int count = 1;
            if (nameValue != null) {
                stmt.setString(count, "%" + nameValue + "%");
                count++;
            }
            if (altNameValue != null) {
                stmt.setString(count, "%" + altNameValue + "%");
                count++;
            }
            if (nationalityValue != null) {
                stmt.setString(count, "%" + nationalityValue + "%");
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

                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                contactHM.put(contactId, contact);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SearchContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return contactHM;
    }

    public static HashMap<Integer, Contact> searchContactByTeam(String team, boolean otherTeam) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Contact> contactHM = new HashMap<Integer, Contact>();

        try {
            conn = ConnectionManager.getConnection();

            if (otherTeam) {
                stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                        + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                        + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                        + "FROM CONTACT WHERE CONTACT_ID IN ( SELECT DISTINCT CONTACT_ID FROM TEAM_JOIN WHERE EXPLAIN_IF_OTHER LIKE ? AND PERMISSION IS NOT NULL AND IF(DATE_OBSOLETE IS NOT NULL,DATE_OBSOLETE >= CURDATE(),TRUE))");
            } else {
                stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                        + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                        + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                        + "FROM CONTACT WHERE CONTACT_ID IN ( SELECT DISTINCT CONTACT_ID FROM TEAM_JOIN WHERE TEAM_NAME LIKE ? AND PERMISSION IS NOT NULL AND IF(DATE_OBSOLETE IS NOT NULL,DATE_OBSOLETE >= CURDATE(),TRUE))");
            }
            stmt.setString(1, "%"+team+"%");

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
                contactHM.put(contactId, contact);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SearchContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return contactHM;
    }

    public static HashMap<Integer, Contact> searchContactByAppreciationGesture(String appreciationGesture) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Contact> contactHM = new HashMap<Integer, Contact>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT WHERE CONTACT_ID IN ( SELECT DISTINCT CONTACT_ID FROM APPRECIATION WHERE APPRECIATION_GESTURE LIKE ?)");

            stmt.setString(1, "%" + appreciationGesture + "%");

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
                contactHM.put(contactId, contact);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SearchContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return contactHM;
    }

    public static HashMap<Integer, Contact> searchContactByLanguageName(String languageName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Contact> contactHM = new HashMap<Integer, Contact>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT WHERE CONTACT_ID IN ( SELECT DISTINCT CONTACT_ID FROM LANGUAGE_ASSIGNMENT WHERE LANGUAGE_NAME = ? AND IF(DATE_OBSOLETE IS NOT NULL,DATE_OBSOLETE >= CURDATE(),TRUE))");

            stmt.setString(1, languageName);

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
                contactHM.put(contactId, contact);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SearchContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return contactHM;
    }

    public static HashMap<Integer, Contact> searchContactBySkillName(String skillName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, Contact> contactHM = new HashMap<Integer, Contact>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM CONTACT WHERE CONTACT_ID IN ( SELECT DISTINCT CONTACT_ID FROM SKILL_ASSIGNMENT WHERE SKILL_NAME = ? AND IF(DATE_OBSOLETE IS NOT NULL,DATE_OBSOLETE >= CURDATE(),TRUE) )");

            stmt.setString(1, skillName);

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
                contactHM.put(contactId, contact);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SearchContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return contactHM;
    }

}
