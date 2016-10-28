/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Phone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class FormDAO {

    private static SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean addForm(String code, Date startTime, Date endTime, String createdBy) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO FORM (CODE,"
                    + "DATE_CREATED,START_DATE_TIME,END_DATE_TIME,CREATED_BY)"
                    + " VALUES (?,?,?,?,?)");

            stmt.setString(1, code);
            stmt.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
            stmt.setTimestamp(3, new java.sql.Timestamp(startTime.getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(endTime.getTime()));
            stmt.setString(5, createdBy);

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean updateForm(int id, String code, Date startTime, Date endTime) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE FORM SET "
                    + "CODE=?,START_DATE_TIME=?,END_DATE_TIME=? "
                    + "WHERE FORM_ID=?");

            stmt.setInt(1, id);
            stmt.setString(2, code);
            stmt.setTimestamp(3, new java.sql.Timestamp(startTime.getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(endTime.getTime()));

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteForm(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM FORM "
                    + "WHERE FORM_ID=?");

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

    public static LinkedHashMap<Integer, ArrayList<String>> retrieveAllForm() {
        LinkedHashMap<Integer, ArrayList<String>> results = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CREATED_BY, CODE"
                    + ", START_DATE_TIME, END_DATE_TIME FROM FORM ");

            rs = stmt.executeQuery();
            int counter = 1;
            while (rs.next()) {

                String createdBy = rs.getString(1);
                String code = rs.getString(2);
                String startTime = datetime.format(rs.getTimestamp(3));
                String endTime = datetime.format(rs.getTimestamp(4));

                ArrayList<String> formList = new ArrayList<String>();             
                formList.add(createdBy);
                formList.add(code);           
                formList.add(startTime);
                formList.add(endTime);
                
                results.put(counter++, formList);
             
            }

            return results;

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PHONE from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return results;

    }

}
