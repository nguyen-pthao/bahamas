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
public class EmailDAO {

    public static boolean addEmail(Email e) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO EMAIL (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,EMAIL,REMARKS,DATE_OBSOLETE)"
                    + " VALUES (?,?,?,?,?,?)");

            stmt.setInt(1, e.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(e.getDateCreated().getTime()));
            stmt.setString(3, e.getCreatedBy());
            stmt.setString(4, e.getEmail());
            stmt.setString(5, e.getRemarks());

            if (e.getDateObsolete() != null) {
                stmt.setDate(6, new java.sql.Date(e.getDateObsolete().getTime()));
            }else {
                stmt.setDate(6, null);
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

}
