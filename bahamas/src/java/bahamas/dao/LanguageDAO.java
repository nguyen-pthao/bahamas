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
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class LanguageDAO {

    public static boolean addLanguage(LanguageAssignment la) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO LANGUAGE_ASSIGNMENT (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,EXPLAIN_IF_OTHER,REMARKS,DATE_OBSOLETE,PROFICIENCY,LANGUAGE_NAME)"
                    + " VALUES (?,?,?,?,?,?,?,?)");

            stmt.setInt(1, la.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(la.getDateCreated().getTime()));
            stmt.setString(3, la.getCreatedBy());
            stmt.setString(4, la.getExplainIfOther());
            stmt.setString(5, la.getRemarks());

            if (la.getDateObsolete() != null) {
                stmt.setDate(6, new java.sql.Date(la.getDateObsolete().getTime()));
            } else {
                stmt.setDate(6, null);
            }

            stmt.setString(7, la.getProficiency());
            stmt.setString(8, la.getLanguage());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static ArrayList<LanguageAssignment> retrieveLanguageByCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<LanguageAssignment> languageList;
        languageList = new ArrayList<LanguageAssignment>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT LANGUAGE_NAME,EXPLAIN_IF_OTHER, DATE_OBSOLETE, "
                    + "REMARKS, CREATED_BY, DATE_CREATED, PROFICIENCY FROM LANGUAGE_ASSIGNMENT WHERE CONTACT_ID = (?) ORDER BY DATE_CREATED DESC");
            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            while (rs.next()) {

                String languageName = rs.getString(1);
                String explainIfOther = rs.getString(2);
                Date dateObsolete = rs.getDate(3);
                String remarks = rs.getString(4);
                String createdBy = rs.getString(5);
                Date dateCreated = rs.getTimestamp(6);
                String proficiency = rs.getString(7);

                LanguageAssignment languageAssignment = new LanguageAssignment(languageName, explainIfOther, dateObsolete, remarks, createdBy, dateCreated,proficiency);
                languageList.add(languageAssignment);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve LANGUAGE_ASSIGNNMENT from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return languageList;
    }

    public static boolean updateLanguage(LanguageAssignment la) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE LANGUAGE_ASSIGNMENT SET EXPLAIN_IF_OTHER=?,"
                    + "REMARKS=?,DATE_OBSOLETE=?,PROFICIENCY=? "
                    + "WHERE CONTACT_ID=? AND LANGUAGE_NAME=?");

            stmt.setString(1, la.getExplainIfOther());
            stmt.setString(2, la.getRemarks());

            if (la.getDateObsolete() != null) {
                stmt.setDate(3, new java.sql.Date(la.getDateObsolete().getTime()));
            } else {
                stmt.setDate(3, null);
            }

            stmt.setString(4, la.getProficiency());
            stmt.setInt(5, la.getContact().getContactId());
            stmt.setString(6, la.getLanguage());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteLanguage(int id, String name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM LANGUAGE_ASSIGNMENT WHERE CONTACT_ID=? AND LANGUAGE_NAME=?");

            stmt.setInt(1, id);
            stmt.setString(2, name);

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    
    public static boolean languageExist(int id, String languageName) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM LANGUAGE_ASSIGNMENT WHERE CONTACT_ID = (?) AND LANGUAGE_NAME = (?)");
            stmt.setInt(1, id);
            stmt.setString(2, languageName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int count = rs.getInt(1);
                if (count >= 1) {
                    exist = true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve LANGUAGE_ASSIGNNMENT from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return exist;
    }

}
