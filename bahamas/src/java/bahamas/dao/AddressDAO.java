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
public class AddressDAO {

    public static boolean addAddress(Address a) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO ADDRESS (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,ADDRESS,COUNTRY,ZIPCODE,REMARKS,DATE_OBSOLETE)"
                    + " VALUES (?,?,?,?,?,?,?,?)");

            stmt.setInt(1, a.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(a.getDateCreated().getTime()));
            stmt.setString(3, a.getCreatedBy());
            stmt.setString(4, a.getAddress());
            stmt.setString(5, a.getCountry());
            stmt.setInt(6, a.getZipcode());
            stmt.setString(7, a.getRemarks());

            if (a.getDateObsolete() != null) {
                stmt.setDate(8, new java.sql.Date(a.getDateObsolete().getTime()));
            } else {
                stmt.setDate(8, null);
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
