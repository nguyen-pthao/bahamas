/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Appreciation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HUXLEY
 */
public class AppreciationDAO {

    public static boolean addAppreciation(Appreciation a) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO APPRECIATION (CONTACT_ID,"
                    + "DATE_CREATED,CREATED_BY,APPRAISAL_COMMENTS,APPRAISAL_BY,APPRAISAL_DATE,"
                    + "APPRAISAL_GESTURE,APPRECIATION_BY,APPRECIATION_DATE,REMARKS)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, a.getContact().getContactId());
            stmt.setTimestamp(2, new java.sql.Timestamp(a.getDateCreated().getTime()));
            stmt.setString(3, a.getCreatedBy());
            stmt.setString(4, a.getAppraisalComments());
            stmt.setString(5, a.getAppraisalBy());
            if (a.getAppraisalDate() != null) {
                stmt.setDate(6, new java.sql.Date(a.getAppraisalDate().getTime()));
            } else {
                stmt.setDate(6, null);
            }
            stmt.setString(7, a.getAppreciationGesture());
            stmt.setString(8, a.getAppreciationBy());
            if (a.getAppreciationDate() != null) {
                stmt.setDate(9, new java.sql.Date(a.getAppreciationDate().getTime()));
            }
            else{
                stmt.setDate(9,null);
            }
            stmt.setString(10, a.getRemarks());

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
