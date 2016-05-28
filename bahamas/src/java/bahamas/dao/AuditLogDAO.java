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
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tan.si.hao
 */
public class AuditLogDAO {
    //INSERT INTO auditlog (USERNAME, DATE, OPERATION) VALUES ('username', '2014/08/06 15:59:48', 'Created xxx')
    
    
    // actionType: create, update, delete
    public static void insertAuditLog(String username, String actionType, String actionMsg){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Timestamp date = new Timestamp(new java.util.Date().getTime());
        try {
            
            //new Timestamp(date.getTime())
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO auditlog (USERNAME, DATE, OPERATION) VALUES (?,?,?)");
            stmt.setString(1, username);
            stmt.setTimestamp(2, date);
            stmt.setString(3, actionType.toUpperCase() + "-" + actionMsg);
            stmt.execute();
            
            ConnectionManager.close(conn, stmt, rs);
            
        } catch (SQLException ex) {
            Logger.getLogger(AuditLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
