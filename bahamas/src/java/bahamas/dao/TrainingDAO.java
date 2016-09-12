/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.entity.LanguageAssignment;
import bahamas.entity.SkillAssignment;
import bahamas.entity.Training;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class TrainingDAO {

    public static boolean addTraining(Training t) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO TRAINING (CONTACT_ID,"
                    + "CREATED_BY,DATE_CREATED,TEAM_NAME,EXPLAIN_IF_OTHER,TRAINING_COURSE,TRAINING_BY,TRAINING_DATE,REMARKS)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)");

            stmt.setInt(1, t.getContact().getContactId());
            stmt.setString(2, t.getCreatedBy());
            stmt.setTimestamp(3, new java.sql.Timestamp(t.getDateCreated().getTime()));
            stmt.setString(4, t.getTeamName());
            stmt.setString(5, t.getExplainIfOther());
            stmt.setString(6, t.getTrainingCourse());
            stmt.setString(7, t.getTrainingBy());

            if (t.getTrainingDate() != null) {
                stmt.setDate(8, new java.sql.Date(t.getTrainingDate().getTime()));
            } else {
                stmt.setDate(8, null);
            }

            stmt.setString(9, t.getRemarks());
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteTraining(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM TRAINING WHERE TRAINING_ID=?");

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

    public static boolean updateTraining(Training t) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE TRAINING SET TEAM_NAME=?,"
                    + "EXPLAIN_IF_OTHER=?,TRAINING_COURSE=?,TRAINING_BY=?,"
                    + "TRAINING_DATE=?,REMARKS=? WHERE TRAINING_ID=? AND CONTACT_ID=?");

            stmt.setString(1, t.getTeamName());
            stmt.setString(2, t.getExplainIfOther());
            stmt.setString(3, t.getTrainingCourse());
            stmt.setString(4, t.getTrainingBy());

            if (t.getTrainingDate() != null) {
                stmt.setDate(5, new java.sql.Date(t.getTrainingDate().getTime()));
            } else {
                stmt.setDate(5, null);
            }

            stmt.setString(6, t.getRemarks());
            stmt.setInt(7, t.getTrainingId());
            stmt.setInt(8, t.getContact().getContactId());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static Training retrieveTrainingById(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT TRAINING_ID,CONTACT_ID,CREATED_BY,DATE_CREATED,"
                    + "TEAM_NAME,EXPLAIN_IF_OTHER,TRAINING_COURSE,TRAINING_BY,TRAINING_DATE,REMARKS "
                    + "FROM TRAINING WHERE TRAINING_ID = (?) ORDER BY DATE_CREATED DESC");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            ContactDAO cDAO = new ContactDAO();
            while (rs.next()) {
                
                int tid = rs.getInt(1);
                Contact c = cDAO.retrieveContactById(rs.getInt(2));
                String createdBy = rs.getString(3);
                Date dateCreated = rs.getTimestamp(4);
                String teamName = rs.getString(5);
                String explainIfOther = rs.getString(6);
                String trainingCourse = rs.getString(7);
                String trainingBy = rs.getString(8);

                Date trainingDate = null;
                if (rs.getDate(9) != null) {
                    trainingDate = new Date(rs.getDate(9).getTime());
                }

                String remarks = rs.getString(10);

                Training newTraining = new Training(tid,c, createdBy, dateCreated, teamName, explainIfOther, trainingCourse, trainingBy, trainingDate, remarks);
                return newTraining;
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve Training from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }
    
    public static ArrayList<Training> retrieveTrainingByCId(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Training> trainingList = new ArrayList<Training>();
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT TRAINING_ID,CONTACT_ID,CREATED_BY,DATE_CREATED,"
                    + "TEAM_NAME,EXPLAIN_IF_OTHER,TRAINING_COURSE,TRAINING_BY,TRAINING_DATE,REMARKS "
                    + "FROM TRAINING WHERE CONTACT_ID = (?) ORDER BY DATE_CREATED DESC");
            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            ContactDAO cDAO = new ContactDAO();
            while (rs.next()) {
                
                int tid = rs.getInt(1);
                Contact c = cDAO.retrieveContactById(rs.getInt(2));
                String createdBy = rs.getString(3);
                Date dateCreated = rs.getTimestamp(4);
                String teamName = rs.getString(5);
                String explainIfOther = rs.getString(6);
                String trainingCourse = rs.getString(7);
                String trainingBy = rs.getString(8);

                Date trainingDate = null;
                if (rs.getDate(9) != null) {
                    trainingDate = new Date(rs.getDate(9).getTime());
                }

                String remarks = rs.getString(10);

                Training newTraining = new Training(tid, c, createdBy, dateCreated, teamName, explainIfOther, trainingCourse, trainingBy, trainingDate, remarks);
                trainingList.add(newTraining);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve Training from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return trainingList;
    }

    public static ArrayList<Training> retrieveAllTraining() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Training> trainingList = new ArrayList<Training>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT TRAINING_ID,CONTACT_ID,CREATED_BY,DATE_CREATED,"
                    + "TEAM_NAME,EXPLAIN_IF_OTHER,TRAINING_COURSE,TRAINING_BY,TRAINING_DATE,REMARKS "
                    + "FROM TRAINING ORDER BY DATE_CREATED DESC");

            rs = stmt.executeQuery();
            ContactDAO cDAO = new ContactDAO();
            while (rs.next()) {

                int training_id = rs.getInt(1);
                Contact c = cDAO.retrieveContactById(rs.getInt(2));
                String createdBy = rs.getString(3);
                Date dateCreated = rs.getTimestamp(4);
                String teamName = rs.getString(5);
                String explainIfOther = rs.getString(6);
                String trainingCourse = rs.getString(7);
                String trainingBy = rs.getString(8);

                Date trainingDate = null;
                if (rs.getDate(9) != null) {
                    trainingDate = new Date(rs.getDate(9).getTime());
                }

                String remarks = rs.getString(10);

                Training newTraining = new Training(training_id, c, createdBy, dateCreated, teamName, explainIfOther, trainingCourse, trainingBy, trainingDate, remarks);
                trainingList.add(newTraining);
            }

            return trainingList;

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve Training from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return trainingList;
    }
}
