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
}
