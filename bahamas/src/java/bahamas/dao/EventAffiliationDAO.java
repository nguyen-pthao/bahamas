/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.EventAffiliation;
import com.google.gson.JsonArray;
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
public class EventAffiliationDAO {
    public static boolean addTeamAffiliation(EventAffiliation eventAffiliation) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO EVENT_AFFILIATION (EVENT_ID, TEAM_NAME, CREATED_BY, DATE_CREATED, EXPLAIN_IF_OTHER, DATE_OBSOLETE, REMARKS) VALUES (?, ?, ?, ?, ?, ?, ?)");

            //stmt.setInt(1, d.getContact().getContactId());
            ArrayList<String> teamNameArray = eventAffiliation.getTeamArray();
            for (int i = 0; i < teamNameArray.size(); i++) {
                stmt.setInt(1, eventAffiliation.getEventID());
                stmt.setString(2, teamNameArray.get(i));
                stmt.setString(3, eventAffiliation.getCreatedBy());
                stmt.setTimestamp(4, new java.sql.Timestamp(eventAffiliation.getDateCreated().getTime()));
                stmt.setString(5, eventAffiliation.getExplainIfOthers());
                if(eventAffiliation.getDateObsolete() != null){
                    stmt.setTimestamp(6, new java.sql.Timestamp(eventAffiliation.getDateObsolete().getTime())); 
                }else{
                    stmt.setTimestamp(6, null); 
                }
                stmt.setString(7, eventAffiliation.getRemarks());
                stmt.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    public static EventAffiliation retrieveAllEventAffiliation(int eventId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> teamAffiliationList;
        teamAffiliationList = new ArrayList<String>();
        
        EventAffiliation eventAffiliation = null;
        
        try {
            conn = ConnectionManager.getConnection();

            stmt = conn.prepareStatement("SELECT EVENT_ID, TEAM_NAME, CREATED_BY, DATE_CREATED, EXPLAIN_IF_OTHER, DATE_OBSOLETE, REMARKS FROM EVENT_AFFILIATION WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                
                if(eventAffiliation == null){
                    String eventID = rs.getString(1);
                    teamAffiliationList.add(rs.getString(2));
                    String createdBy = rs.getString(3);
                    Date dateCreated = sdf.parse(rs.getString(4));
                    String explainIfOthers = rs.getString(5);
                    String dateString = rs.getString(6);
                    Date dateObsolete = null;
                    if (dateString != null) {
                        dateObsolete = sdf.parse(dateString);
                    }
                    String remarks = rs.getString(7);
                    eventAffiliation = new EventAffiliation(Integer.parseInt(eventID), explainIfOthers, remarks, teamAffiliationList, createdBy, dateCreated, dateObsolete);
                }else{
                    String teamName = rs.getString(2);
                    teamAffiliationList.add(rs.getString(2));
                    eventAffiliation.setTeamArray(teamAffiliationList);
                }
                   
            }
            return eventAffiliation;

        } catch (ParseException ex) {
            Logger.getLogger(TeamJoinDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TeamJoinDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EVENT_AFFILIATION from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return null;

    }
    
    public static boolean updateTeamAffiliation(EventAffiliation eventAffiliation, int eventId) { 
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            
            stmt = conn.prepareStatement("DELETE FROM EVENT_AFFILIATION WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventId);
            stmt.executeUpdate(); 
            if(addTeamAffiliation(eventAffiliation)){
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
}
