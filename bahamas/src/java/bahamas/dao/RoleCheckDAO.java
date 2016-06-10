/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tan.si.hao
 */
public class RoleCheckDAO {
    
    public static boolean checkRole(int cid, String teamName, String permission){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM TEAM_JOIN WHERE (DATE_OBSOLETE = '0000-00-00' "
                    + "OR DATE_OBSOLETE = '' OR DATE_OBSOLETE IS NULL) AND CONTACT_ID = (?) AND TEAM_NAME = (?) AND PERMISSION = (?) ");
            stmt.setString(1, Integer.toString(cid));
            stmt.setString(2, teamName);
            stmt.setString(3, permission);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = Integer.parseInt(rs.getString(1));
                if(count >= 1){
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve TEAMJOIN from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return false;
    }
    
    public static boolean checkRole(int cid, String permission){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) AS COUNT FROM TEAM_JOIN WHERE (DATE_OBSOLETE = '0000-00-00' "
                    + "OR DATE_OBSOLETE = '' OR DATE_OBSOLETE IS NULL) AND CONTACT_ID = (?) AND PERMISSION = (?) ");
            stmt.setString(1, Integer.toString(cid));
            stmt.setString(2, permission);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count = Integer.parseInt(rs.getString(1));
                if(count >= 1){
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleCheckDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve TEAMJOIN from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return false;
    }
    
}
