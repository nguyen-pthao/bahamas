/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Appreciation;
import bahamas.entity.Contact;
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
                    + "APPRECIATION_GESTURE,APPRECIATION_BY,APPRECIATION_DATE,REMARKS)"
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
            } else {
                stmt.setDate(9, null);
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

    public static ArrayList<Appreciation> retrieveAppreciation(int cid) {
        ArrayList<Appreciation> appreciationList = new ArrayList<Appreciation>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT APPRECIATION_ID, APPRAISAL_COMMENTS, APPRAISAL_BY, APPRAISAL_DATE, APPRECIATION_GESTURE, APPRECIATION_BY, APPRECIATION_DATE, REMARKS, CREATED_BY, DATE_CREATED FROM APPRECIATION WHERE CONTACT_ID = (?) ORDER BY DATE_CREATED DESC");
            stmt.setString(1, Integer.toString(cid));
            rs = stmt.executeQuery();
            while (rs.next()) {

                int appreciationId = rs.getInt(1);
                String appraisalComment = rs.getString(2);
                String appraisalBy = rs.getString(3);
                Date appraisalDate = rs.getDate(4);
                String appreciationGesture = rs.getString(5);
                String appreciationBy = rs.getString(6);
                Date appreciationDate = rs.getDate(7);
                String remarks = rs.getString(8);
                String createdBy = rs.getString(9);
                Date dateCreated = rs.getTimestamp(10);

                Appreciation a = new Appreciation(appreciationId, appraisalComment, appraisalBy, appraisalDate, appreciationGesture, appreciationBy, appreciationDate, remarks, createdBy, dateCreated);

                appreciationList.add(a);
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve APPRECIATION from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return appreciationList;

    }

    public static boolean updateAppreciation(Appreciation a) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE APPRECIATION SET CONTACT_ID=?,"
                    + "APPRAISAL_COMMENTS=?,APPRAISAL_BY=?,APPRAISAL_DATE=?,"
                    + "APPRECIATION_GESTURE=?,APPRECIATION_BY=?,APPRECIATION_DATE=?,REMARKS=? "
                    + "WHERE APPRECIATION_ID=?");

            stmt.setInt(1, a.getContact().getContactId());
            stmt.setString(2, a.getAppraisalComments());
            stmt.setString(3, a.getAppraisalBy());
            if (a.getAppraisalDate() != null) {
                stmt.setDate(4, new java.sql.Date(a.getAppraisalDate().getTime()));
            } else {
                stmt.setDate(4, null);
            }
            stmt.setString(5, a.getAppreciationGesture());
            stmt.setString(6, a.getAppreciationBy());
            if (a.getAppreciationDate() != null) {
                stmt.setDate(7, new java.sql.Date(a.getAppreciationDate().getTime()));
            } else {
                stmt.setDate(7, null);
            }
            stmt.setString(8, a.getRemarks());
            stmt.setInt(9, a.getAppreciationId());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteAppreciation(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM APPRECIATION "
                    + "WHERE APPRECIATION_ID=?");
            
            stmt.setInt(1, id);

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
