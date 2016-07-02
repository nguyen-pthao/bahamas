/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.entity.TeamJoin;
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
 * @author tan.si.hao
 */
public class TeamJoinDAO {

    public static ArrayList<TeamJoin> retrieveAllTeamJoin(String username) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<TeamJoin> teamJoinList;
        teamJoinList = new ArrayList<TeamJoin>();

        try {
            conn = ConnectionManager.getConnection();

            //how all teams with date obslete filter
            //stmt = conn.prepareStatement("SELECT TEAM_NAME, T.DATE_CREATED, T.CREATED_BY, T.EXPLAIN_IF_OTHER, SUBTEAM, DATE_OBSOLETE, T.REMARKS, PERMISSION FROM TEAM_JOIN T, CONTACT C WHERE T.CONTACT_ID = C.CONTACT_ID AND (T.DATE_OBSOLETE = '0000-00-00' OR T.DATE_OBSOLETE = '' OR T.DATE_OBSOLETE IS NULL) AND C.USERNAME = (?)");
            //show all teams without date obslete filter
            stmt = conn.prepareStatement("SELECT TEAM_NAME, T.DATE_CREATED, T.CREATED_BY, T.EXPLAIN_IF_OTHER, SUBTEAM,DATE_OBSOLETE, T.REMARKS, PERMISSION FROM TEAM_JOIN T, CONTACT C WHERE T.CONTACT_ID = C.CONTACT_ID AND C.USERNAME = (?)");
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            ContactDAO cDAO = new ContactDAO();
            Contact c = cDAO.retrieveContactByUsername(username);
            while (rs.next()) {

                String teamName = rs.getString(1);
                Date dateCreated = sdf.parse(rs.getString(2));
                String createdBy = rs.getString(3);
                String explainIfOthers = rs.getString(4);
                String subTeam = rs.getString(5);
                String dateString = rs.getString(6);
                Date dateObsolete = null;
                if (dateString != null) {
                    dateObsolete = sdf.parse(dateString);
                }
                String remarks = rs.getString(7);
                String permission = rs.getString(8);
                //boolean approval = rs.getBoolean(9);

                TeamJoin teamJoin = new TeamJoin(c, teamName, dateCreated,
                        createdBy, explainIfOthers, subTeam, dateObsolete,
                        remarks, permission);

                teamJoinList.add(teamJoin);
            }
            return teamJoinList;

        } catch (ParseException ex) {
            Logger.getLogger(TeamJoinDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TeamJoinDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve TEAMJOIN from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return null;

    }
    
        public static ArrayList<TeamJoin> retrieveAllTeamJoinCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<TeamJoin> teamJoinList;
        teamJoinList = new ArrayList<TeamJoin>();

        try {
            conn = ConnectionManager.getConnection();

            //how all teams with date obslete filter
            //stmt = conn.prepareStatement("SELECT TEAM_NAME, T.DATE_CREATED, T.CREATED_BY, T.EXPLAIN_IF_OTHER, SUBTEAM, DATE_OBSOLETE, T.REMARKS, PERMISSION FROM TEAM_JOIN T, CONTACT C WHERE T.CONTACT_ID = C.CONTACT_ID AND (T.DATE_OBSOLETE = '0000-00-00' OR T.DATE_OBSOLETE = '' OR T.DATE_OBSOLETE IS NULL) AND C.USERNAME = (?)");
            //show all teams without date obslete filter
            stmt = conn.prepareStatement("SELECT TEAM_NAME, T.DATE_CREATED, T.CREATED_BY, T.EXPLAIN_IF_OTHER, SUBTEAM,DATE_OBSOLETE, T.REMARKS, PERMISSION FROM TEAM_JOIN T, CONTACT C WHERE T.CONTACT_ID = C.CONTACT_ID AND C.CONTACT_ID = (?)");
            stmt.setInt(1, cid);
            
            rs = stmt.executeQuery();
            ContactDAO cDAO = new ContactDAO();
            Contact c = cDAO.retrieveContactById(cid);
            while (rs.next()) {

                String teamName = rs.getString(1);
                Date dateCreated = sdf.parse(rs.getString(2));
                String createdBy = rs.getString(3);
                String explainIfOthers = rs.getString(4);
                String subTeam = rs.getString(5);
                String dateString = rs.getString(6);
                Date dateObsolete = null;
                if (dateString != null) {
                    dateObsolete = sdf.parse(dateString);
                }
                String remarks = rs.getString(7);
                String permission = rs.getString(8);
                //boolean approval = rs.getBoolean(9);

                TeamJoin teamJoin = new TeamJoin(c, teamName, dateCreated,
                        createdBy, explainIfOthers, subTeam, dateObsolete,
                        remarks, permission);

                teamJoinList.add(teamJoin);
            }
            return teamJoinList;

        } catch (ParseException ex) {
            Logger.getLogger(TeamJoinDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TeamJoinDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve TEAMJOIN from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return null;

    }

    public static boolean addTeamJoin(TeamJoin t) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO TEAM_JOIN (CONTACT_ID,"
                    + "TEAM_NAME,DATE_CREATED,CREATED_BY,EXPLAIN_IF_OTHER,SUBTEAM,DATE_OBSOLETE,REMARKS)"
                    + " VALUES (?,?,?,?,?,?,?,?)");

            stmt.setInt(1, t.getContact().getContactId());
            stmt.setString(2, t.getTeamName());
            stmt.setTimestamp(3, new java.sql.Timestamp(t.getDateCreated().getTime()));
            stmt.setString(4, t.getCreatedBy());
            stmt.setString(5, t.getExplainIfOthers());
            stmt.setString(6, t.getSubTeam());

            if (t.getDateObsolete() != null) {
                stmt.setDate(7, new java.sql.Date(t.getDateObsolete().getTime()));
            } else {
                stmt.setDate(7, null);
            }

            stmt.setString(8, t.getRemarks());
            //stmt.setString(9, t.getPermission());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }

    public static boolean updateTeamJoin(TeamJoin t) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE TEAM_JOIN SET "
                    + "EXPLAIN_IF_OTHER=?,SUBTEAM=?,DATE_OBSOLETE=?,REMARKS=?,PERMISSION=? "
                    + "WHERE CONTACT_ID=? AND TEAM_NAME=?");

            stmt.setString(1, t.getExplainIfOthers());
            stmt.setString(2, t.getSubTeam());

            if (t.getDateObsolete() != null) {
                stmt.setDate(3, new java.sql.Date(t.getDateObsolete().getTime()));
            } else {
                stmt.setDate(3, null);
            }

            stmt.setString(4, t.getRemarks());
            stmt.setString(5, t.getPermission());

            stmt.setInt(6, t.getContact().getContactId());
            stmt.setString(7, t.getTeamName());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;

    }
    
    public static boolean deleteTeamJoin(int id, String teamName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM TEAM_JOIN "
                    + "WHERE CONTACT_ID=? AND TEAM_NAME=?");

            stmt.setInt(1, id);
            stmt.setString(2, teamName);

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
