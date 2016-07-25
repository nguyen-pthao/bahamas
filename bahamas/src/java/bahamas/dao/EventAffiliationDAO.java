/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.EventAffiliation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author tan.si.hao
 */
public class EventAffiliationDAO {
    public static boolean addTeamAffiliation(EventAffiliation eventAffiliation, String username) {
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
                stmt.setString(3, username);
                stmt.setTimestamp(4, new java.sql.Timestamp(eventAffiliation.getDateCreated().getTime()));
                stmt.setString(5, eventAffiliation.getExplainIfOthers());
                stmt.setTimestamp(6, new java.sql.Timestamp(eventAffiliation.getDateObsolete().getTime()));
                stmt.setString(7, username);
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
}
