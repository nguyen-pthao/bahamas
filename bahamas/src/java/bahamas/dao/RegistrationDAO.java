/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class RegistrationDAO {

    private static SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean addRegistration(int form_id, int contact_id, String name, String nric_fin, String nationality,
            Date dob, String gender, String profession, String jobTitle, String remarks, int countryCode,
            String phoneNumber, String email, String language, String proficiency, String skillName, String teamName,
            boolean first, boolean second, boolean requestUser) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO REGISTRATION (FORM_ID,CONTACT_ID,"
                    + "DATE_CREATED,NAME,NRIC_FIN,NATIONALITY,DATE_OF_BIRTH,GENDER,PROFESSION,JOB_TITLE,REMARKS"
                    + "COUNTRY_CODE,PHONE_NUMBER,EMAIL,LANGUAGE_NAME,PROFICIENCY,SKILL_NAME,TEAM_NAME,FIRST_CHECK,"
                    + "SECOND_CHECK,REQUEST_USER)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, form_id);
            stmt.setInt(2, contact_id);
            stmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
            stmt.setString(4, name);
            stmt.setString(5, nric_fin);
            stmt.setString(6, nationality);
            stmt.setDate(7, new java.sql.Date(dob.getTime()));
            stmt.setString(8, gender);
            stmt.setString(9, profession);
            stmt.setString(10, jobTitle);
            stmt.setString(11, remarks);
            stmt.setInt(12, countryCode);
            stmt.setString(13, phoneNumber);
            stmt.setString(14, email);
            stmt.setString(15, language);
            stmt.setString(16, proficiency);
            stmt.setString(17, skillName);
            stmt.setString(18, teamName);
            stmt.setBoolean(19, first);
            stmt.setBoolean(20, second);
            stmt.setBoolean(21, requestUser);

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> retrieveAllRegistration() {
        LinkedHashMap<Integer, ArrayList<String>> results = new LinkedHashMap<Integer, ArrayList<String>>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT r.DATE_CREATED, CODE, "
                    + "CONTACT_ID,NAME,NRIC_FIN,FIRST_CHECK,"
                    + "SECOND_CHECK FROM REGISTRATION ");

            int counter = 1;
            rs = stmt.executeQuery();
            while (rs.next()) {

                String dateCreated = datetime.format(rs.getTimestamp(1));
                String code = rs.getString(2);
                String cid = rs.getString(3);
                String name = rs.getString(4);
                String nric = rs.getString(5);
                String first_check = String.valueOf(rs.getBoolean(6));
                String second_check = String.valueOf(rs.getBoolean(7));

                ArrayList<String> formList = new ArrayList<String>();
                formList.add(dateCreated);
                formList.add(code);
                formList.add(cid);
                formList.add(name);
                formList.add(nric);
                formList.add(first_check);
                formList.add(second_check);

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
