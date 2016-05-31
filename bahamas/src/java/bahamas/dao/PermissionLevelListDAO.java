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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus
 */
public class PermissionLevelListDAO {
    
    private ArrayList<String> permissionLevelList;
    

    public PermissionLevelListDAO() {
    }
    
    public ArrayList<String> retrievePermissionLevelList() {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        permissionLevelList = new ArrayList<String>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM PERMISSION_LEVEL_LIST");

            rs = stmt.executeQuery();
            while (rs.next()) {
                permissionLevelList.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(PermissionLevelListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve permission level list from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return permissionLevelList;
    }
    
}
