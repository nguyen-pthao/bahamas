/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.OfficeHeld;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HUXLEY
 */
public class OfficeHeldDAO {

    public static boolean addOfficeHeld(OfficeHeld o) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO OFFICE_HELD (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,START_OFFICE,END_OFFICE,OFFICE_HELD_NAME,REMARKS)"
                    + " VALUES (?,?,?,?,?,?,?)");

            stmt.setInt(1, o.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(o.getDateCreated().getTime()));
            stmt.setString(3, o.getCreatedBy());

            if (o.getStartOffice() != null) {
                stmt.setDate(4, new java.sql.Date(o.getStartOffice().getTime()));
            } else {
                stmt.setDate(4, null);
            }

            if (o.getEndOffice() != null) {
                stmt.setDate(5, new java.sql.Date(o.getEndOffice().getTime()));
            } else {
                stmt.setDate(5, null);
            }

            stmt.setString(6, o.getOfficeHeldPosition());
            stmt.setString(7, o.getRemarks());

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
