/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.entity.UserManagement;
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
 * @author tan.si.hao
 */
public class UserManagementDAO {

    private static SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static ArrayList<UserManagement> retrieveAllUnverifiedContactsWithTeamPreference(Date userCreatedStartDate, Date userCreatedEndDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String prepareStatement = "SELECT T1.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,T1.DATE_CREATED,T1.CREATED_BY,NAME,ALT_NAME,T1.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,T1.REMARKS, T1.NOTIFICATION, LAST_LOGIN, TEAM_LIST, EMAIL FROM (SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, C.NOTIFICATION, LAST_LOGIN, TEAM_LIST FROM CONTACT C LEFT OUTER JOIN (SELECT CONTACT_ID, GROUP_CONCAT(CONCAT(TEAM_NAME)SEPARATOR '|') AS TEAM_LIST FROM TEAM_JOIN WHERE PERMISSION IS NULL GROUP BY CONTACT_ID) AS EL ON C.CONTACT_ID = EL.CONTACT_ID) AS T1, (SELECT CONTACT_ID, EMAIL FROM EMAIL WHERE CONTACT_ID NOT IN (SELECT CONTACT_ID FROM EMAIL WHERE VERIFIED = 1 GROUP BY CONTACT_ID)) AS T2 WHERE T1.CONTACT_ID = T2.CONTACT_ID";
        ArrayList<UserManagement> contactList = new ArrayList<UserManagement>();
        boolean isFirst = true;
        try {
            conn = ConnectionManager.getConnection();
            //stmt = conn.prepareStatement("SELECT T1.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,T1.DATE_CREATED,T1.CREATED_BY,NAME,ALT_NAME,T1.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,T1.REMARKS, T1.NOTIFICATION, LAST_LOGIN, TEAM_LIST, EMAIL FROM (SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, C.NOTIFICATION, LAST_LOGIN, TEAM_LIST FROM CONTACT C LEFT OUTER JOIN (SELECT CONTACT_ID, GROUP_CONCAT(CONCAT(TEAM_NAME)SEPARATOR '|') AS TEAM_LIST FROM TEAM_JOIN GROUP BY CONTACT_ID) AS EL ON C.CONTACT_ID = EL.CONTACT_ID) AS T1, (SELECT CONTACT_ID, EMAIL FROM EMAIL WHERE CONTACT_ID NOT IN (SELECT CONTACT_ID FROM EMAIL WHERE VERIFIED = 1 GROUP BY CONTACT_ID)) AS T2 WHERE T1.CONTACT_ID = T2.CONTACT_ID");
                    
            if (userCreatedStartDate != null) {
                if (!isFirst) {
                    prepareStatement += "AND T1.DATE_CREATED >= ? ";
                } else {
                    prepareStatement += " WHERE T1.DATE_CREATED >= ? ";
                    isFirst = false;
                }
            }
            if (userCreatedEndDate != null) {
                if (!isFirst) {
                    prepareStatement += "AND T1.DATE_CREATED <= ? ";
                } else {
                    prepareStatement += " WHERE T1.DATE_CREATED <= ? ";
                }
            }
            stmt = conn.prepareStatement(prepareStatement);
            int count = 1;
            if (userCreatedStartDate != null) {
                stmt.setDate(count++, new java.sql.Date(userCreatedStartDate.getTime()));
            }
            if (userCreatedEndDate != null) {
                stmt.setDate(count++, new java.sql.Date(userCreatedEndDate.getTime()));
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
                Date lastLogin = rs.getDate(23);
                String teams = rs.getString(24);
                String email = rs.getString(25);
                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification, lastLogin);
                contactList.add(new UserManagement(contact, teams, email));
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
    
    public static ArrayList<UserManagement> retrieveAllVerifiedContactsWithTeamPreference(Date userCreatedStartDate, Date userCreatedEndDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String prepareStatement = "SELECT T1.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,T1.DATE_CREATED,T1.CREATED_BY,NAME,ALT_NAME,T1.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,T1.REMARKS, T1.NOTIFICATION, LAST_LOGIN, TEAM_LIST, EMAIL FROM (SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, C.NOTIFICATION, LAST_LOGIN, TEAM_LIST FROM CONTACT C LEFT OUTER JOIN (SELECT CONTACT_ID, GROUP_CONCAT(CONCAT(TEAM_NAME)SEPARATOR '|') AS TEAM_LIST FROM TEAM_JOIN GROUP BY CONTACT_ID) AS EL ON C.CONTACT_ID = EL.CONTACT_ID) AS T1, (SELECT CONTACT_ID, EMAIL FROM EMAIL WHERE CONTACT_ID NOT IN (SELECT CONTACT_ID FROM EMAIL WHERE VERIFIED = 0 GROUP BY CONTACT_ID)) AS T2 WHERE T1.CONTACT_ID = T2.CONTACT_ID AND USERNAME IS NULL AND T1.CONTACT_ID IN (SELECT DISTINCT(CONTACT_ID) FROM EMAIL WHERE VERIFIED = 1)";
        ArrayList<UserManagement> contactList = new ArrayList<UserManagement>();
        boolean isFirst = true;
        try {
            conn = ConnectionManager.getConnection();
            //stmt = conn.prepareStatement("SELECT T1.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,T1.DATE_CREATED,T1.CREATED_BY,NAME,ALT_NAME,T1.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,T1.REMARKS, T1.NOTIFICATION, LAST_LOGIN, TEAM_LIST, EMAIL FROM (SELECT C.CONTACT_ID,CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,C.DATE_CREATED,C.CREATED_BY,NAME,ALT_NAME,C.EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,C.REMARKS, C.NOTIFICATION, LAST_LOGIN, TEAM_LIST FROM CONTACT C LEFT OUTER JOIN (SELECT CONTACT_ID, GROUP_CONCAT(CONCAT(TEAM_NAME)SEPARATOR '|') AS TEAM_LIST FROM TEAM_JOIN GROUP BY CONTACT_ID) AS EL ON C.CONTACT_ID = EL.CONTACT_ID) AS T1, (SELECT CONTACT_ID, EMAIL FROM EMAIL WHERE CONTACT_ID NOT IN (SELECT CONTACT_ID FROM EMAIL WHERE VERIFIED = 1 GROUP BY CONTACT_ID)) AS T2 WHERE T1.CONTACT_ID = T2.CONTACT_ID");
                    
            if (userCreatedStartDate != null) {
                if (!isFirst) {
                    prepareStatement += "AND T1.DATE_CREATED >= ? ";
                } else {
                    prepareStatement += " WHERE T1.DATE_CREATED >= ? ";
                    isFirst = false;
                }
            }
            if (userCreatedEndDate != null) {
                if (!isFirst) {
                    prepareStatement += "AND T1.DATE_CREATED <= ? ";
                } else {
                    prepareStatement += " WHERE T1.DATE_CREATED <= ? ";
                }
            }
            stmt = conn.prepareStatement(prepareStatement);
            int count = 1;
            if (userCreatedStartDate != null) {
                stmt.setDate(count++, new java.sql.Date(userCreatedStartDate.getTime()));
            }
            if (userCreatedEndDate != null) {
                stmt.setDate(count++, new java.sql.Date(userCreatedEndDate.getTime()));
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
                Date lastLogin = rs.getDate(23);
                String teams = rs.getString(24);
                String email = rs.getString(25);
                Contact contact = new Contact(contactId, contactType, username, password, salt, isAdmin, isNovice, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification, lastLogin);
                contactList.add(new UserManagement(contact, teams, email));
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
    
}
