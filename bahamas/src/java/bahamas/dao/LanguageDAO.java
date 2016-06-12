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
            }else {
                stmt.setDate(6, null);
            }
            
            stmt.setString(7,la.getProficiency());
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
    
}
