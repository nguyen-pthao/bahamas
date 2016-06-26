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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class SkillDAO {

    public static boolean addSkill(SkillAssignment sa) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO SKILL_ASSIGNMENT (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,EXPLAIN_IF_OTHER,REMARKS,DATE_OBSOLETE,SKILL_NAME)"
                    + " VALUES (?,?,?,?,?,?,?)");

            stmt.setInt(1, sa.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(sa.getDateCreated().getTime()));
            stmt.setString(3, sa.getCreatedBy());
            stmt.setString(4, sa.getExplainIfOther());
            stmt.setString(5, sa.getRemarks());

            if (sa.getDateObsolete() != null) {
                stmt.setDate(6, new java.sql.Date(sa.getDateObsolete().getTime()));
            } else {
                stmt.setDate(6, null);
            }

            stmt.setString(7, sa.getSkillName());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static ArrayList<SkillAssignment> retrieveSkillByCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<SkillAssignment> skillList;
        skillList = new ArrayList<SkillAssignment>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT SKILL_NAME,EXPLAIN_IF_OTHER, DATE_OBSOLETE, "
                    + "REMARKS, CREATED_BY, DATE_CREATED FROM SKILL_ASSIGNMENT WHERE CONTACT_ID = (?) ");
            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            while (rs.next()) {

                String skillName = rs.getString(1);
                String explainIfOther = rs.getString(2);
                Date dateObsolete = rs.getDate(3);
                String remarks = rs.getString(4);
                String createdBy = rs.getString(5);
                Date dateCreated = rs.getTimestamp(6);

                SkillAssignment skillAssignment = new SkillAssignment(skillName, explainIfOther, dateObsolete, remarks, createdBy, dateCreated);
                skillList.add(skillAssignment);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve SKILL from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return skillList;
    }

    public static boolean updateSkill(SkillAssignment sa) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE SKILL_ASSIGNMENT SET "
                    + "EXPLAIN_IF_OTHER=?,REMARKS=?,DATE_OBSOLETE=? "
                    + "WHERE CONTACT_ID=? AND SKILL_NAME=?");

            stmt.setString(1, sa.getExplainIfOther());
            stmt.setString(2, sa.getRemarks());

            if (sa.getDateObsolete() != null) {
                stmt.setDate(3, new java.sql.Date(sa.getDateObsolete().getTime()));
            } else {
                stmt.setDate(3, null);
            }

            stmt.setInt(4, sa.getContact().getContactId());
            stmt.setString(5, sa.getSkillName());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteSkill(int id, String skillName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM SKILL_ASSIGNMENT "
                    + "WHERE CONTACT_ID=? AND SKILL_NAME=?");

            stmt.setInt(1, id);
            stmt.setString(2, skillName);

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

}
