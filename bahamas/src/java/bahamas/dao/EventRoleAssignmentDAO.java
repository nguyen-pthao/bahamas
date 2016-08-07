/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.EventRoleAssignment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tan.si.hao
 */
public class EventRoleAssignmentDAO {

    public static boolean addRoles(JsonArray jsonArray, int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String roleTemp = "role";
        String descriptionTemp = "description";

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO EVENT_ROLE_ASSIGNMENT (EVENT_ID, ROLE_NAME, ROLE_DESCRIPTION) VALUES (?, ?, ?)");

            //stmt.setInt(1, d.getContact().getContactId());
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                JsonObject jsonObj = jsonElement.getAsJsonObject();
                String role = jsonObj.get(roleTemp + (i + 1)).getAsString();
                String description = jsonObj.get(descriptionTemp + (i + 1)).getAsString();
                if (!role.isEmpty()) {
                    stmt.setInt(1, eventId);
                    stmt.setString(2, role);
                    stmt.setString(3, description);
                    stmt.executeUpdate();
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    public static ArrayList<EventRoleAssignment> retrieveEventRoleById(int eventID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<EventRoleAssignment> eventRoleAssignmentList = new ArrayList<EventRoleAssignment>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT ROLE_ID, EVENT_ID, ROLE_NAME, ROLE_DESCRIPTION FROM EVENT_ROLE_ASSIGNMENT WHERE EVENT_ID = (?)");
            stmt.setInt(1, eventID);

            rs = stmt.executeQuery();
            while (rs.next()) {
                int roleId = rs.getInt(1);
                int eventId = rs.getInt(2);
                String roleName = rs.getString(3);
                String roleDesc = rs.getString(4);
                
                EventRoleAssignment eventRoleAssignment = new EventRoleAssignment(roleId,eventId,roleName,roleDesc);
                eventRoleAssignmentList.add(eventRoleAssignment);
                        
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EVENT_ROLE_ASSIGNMENT from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventRoleAssignmentList;
    }
        
    public static boolean updateRoles(JsonArray jsonArray, int eventId) { 
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
           
            //stmt = conn.prepareStatement("DELETE FROM EVENT_ROLE_ASSIGNMENT WHERE EVENT_ID = (?)");
            //stmt.setInt(1, eventId);
            //stmt.executeUpdate();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                JsonObject jsonObj = jsonElement.getAsJsonObject();
                String role = jsonObj.get("event_role").getAsString();
                String description = jsonObj.get("event_desc").getAsString();
                String roleIdStr = null;
                if(!role.isEmpty()){
                    if(jsonObj.has("event_role_id")){
                        roleIdStr = jsonObj.get("event_role_id").getAsString();
                        stmt = conn.prepareStatement("UPDATE EVENT_ROLE_ASSIGNMENT SET ROLE_NAME = ?, ROLE_DESCRIPTION = ? WHERE ROLE_ID = ? AND EVENT_ID = ?");
                        stmt.setString(1, role);
                        stmt.setString(2, description);
                        stmt.setInt(3, Integer.parseInt(roleIdStr));
                        stmt.setInt(4, eventId);
                    }else{
                        stmt = conn.prepareStatement("INSERT INTO EVENT_ROLE_ASSIGNMENT (EVENT_ID, ROLE_NAME, ROLE_DESCRIPTION) VALUES (?, ?, ?)");
                        stmt.setInt(1, eventId);
                        stmt.setString(2, role);
                        stmt.setString(3, description);
                    }
                    stmt.executeUpdate();
                }
                
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    public static boolean deleteRoles(int roleId) { 
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            //get database connection
            conn = ConnectionManager.getConnection();
           
            stmt = conn.prepareStatement("DELETE FROM EVENT_ROLE_ASSIGNMENT WHERE ROLE_ID = (?)");
            stmt.setInt(1, roleId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }
    
    public static EventRoleAssignment retrieveEventRoleByRoleId(int roleID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EventRoleAssignment eventRoleAssignment = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT ROLE_ID, EVENT_ID, ROLE_NAME, ROLE_DESCRIPTION FROM EVENT_ROLE_ASSIGNMENT WHERE ROLE_ID = (?)");
            stmt.setInt(1, roleID);

            rs = stmt.executeQuery();
            while (rs.next()) {
                int roleId = rs.getInt(1);
                int eventId = rs.getInt(2);
                String roleName = rs.getString(3);
                String roleDesc = rs.getString(4);
                
                eventRoleAssignment = new EventRoleAssignment(roleId,eventId,roleName,roleDesc);
 
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EVENT_ROLE_ASSIGNMENT from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventRoleAssignment;
    }

}
