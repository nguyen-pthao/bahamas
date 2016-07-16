/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao.list;

import bahamas.dao.ConnectionManager;
import bahamas.dao.MembershipDAO;
import bahamas.entity.Membership;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tan.si.hao
 */
public class UpdateListDAO {
    
    //TYPE_OF_CONTACT_LIST
    public static boolean updateTypeOfContactList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE TYPE_OF_CONTACT_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO TYPE_OF_CONTACT_LIST (CONTACT_TYPE, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve TYPE_OF_CONTACT_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }
    
//EVENT_CLASS_LIST
    public static boolean updateEventClassList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE EVENT_CLASS_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO EVENT_CLASS_LIST (EVENT_CLASS_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EVENT_CLASS_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }
    
//EVENT_LOCATION_LIST
    public static boolean updateEventLocationList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE EVENT_LOCATION_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO EVENT_LOCATION_LIST (EVENT_LOCATION_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve EVENT_LOCATION_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }
//LSA_CLASS_LIST
    public static boolean updateLSAClassList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE LSA_CLASS_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO LSA_CLASS_LIST (SKILL_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve LSA_CLASS_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }
    
//LANGUAGE_LIST
    public static boolean updateLanguageList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE LANGUAGE_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO LANGUAGE_LIST (LANGUAGE_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve LANGUAGE_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }
    
//MEMBERSHIP_CLASS_LIST
    public static boolean updateMembershipClassList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE MEMBERSHIP_CLASS_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO MEMBERSHIP_CLASS_LIST (MEMBERSHIP_CLASS_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve MEMBERSHIP_CLASS_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }    
    
//MODE_OF_SENDING_RECEIPT_LIST
    public static boolean updateModeOfSendingReceiptList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE MODE_OF_SENDING_RECEIPT_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO MODE_OF_SENDING_RECEIPT_LIST (RECEIPT_MODE_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve MODE_OF_SENDING_RECEIPT_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }   
    
//OFFICE_LIST
    public static boolean updateOfficeList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE OFFICE_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO OFFICE_LIST (OFFICE_HELD_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve OFFICE_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }   
    
//PAYMENT_MODE_LIST
    public static boolean updatePaymentModeList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE PAYMENT_MODE_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO PAYMENT_MODE_LIST (PAYMENT_MODE_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PAYMENT_MODE_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }   
    
//PERMISSION_LEVEL_LIST
    public static boolean updatePermissionLevelList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE PERMISSION_LEVEL_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO PERMISSION_LEVEL_LIST (PERMISSION, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PERMISSION_LEVEL_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }   
    
//TEAM_AFFILIATION_LIST
    public static boolean updateTeamAffiliationList(JsonArray list){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("TRUNCATE TEAM_AFFILIATION_LIST");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO TEAM_AFFILIATION_LIST (TEAM_NAME, POSITION_NUMBER) VALUES (?, ?)");
           
            for(int i =0; i < list.size(); i++){
                JsonElement nameElement = list.get(i);
                String name = nameElement.getAsString();
                stmt.setString(1, name);
                stmt.setInt(2, i+1);
                stmt.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve TEAM_AFFILIATION_LIST from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, null);
        }
        
        return false;
    }       
}
