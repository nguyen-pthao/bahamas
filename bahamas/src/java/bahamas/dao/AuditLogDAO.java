/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.AuditLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
public class AuditLogDAO {
    
    // actionType: create, update, delete
    public static void insertAuditLog(String username, String actionType, String actionMsg){
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Timestamp date = new Timestamp(new java.util.Date().getTime());
        try {
            
            //new Timestamp(date.getTime())
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO AUDITLOG (USERNAME, DATE, OPERATION) VALUES (?,?,?)");
            stmt.setString(1, username);
            stmt.setTimestamp(2, date);
            stmt.setString(3, actionType.toUpperCase() + "-" + actionMsg);
            stmt.execute();
            
            ConnectionManager.close(conn, stmt, rs);
            
        } catch (SQLException ex) {
            Logger.getLogger(AuditLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ArrayList<AuditLog> retrieveAllAuditLog() {
        ArrayList<AuditLog> auditLogList = new ArrayList<AuditLog>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT AUDITLOG_ID, USERNAME, DATE, OPERATION FROM AUDITLOG");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String auditLogID = rs.getString(1);
                String username = rs.getString(2);
                String dateStr = rs.getString(3);
                Date operationDate = format.parse(dateStr);
                String operation = rs.getString(4);
                AuditLog auditlog = new AuditLog(auditLogID,username,operationDate,operation);
                auditLogList.add(auditlog);
            }    
            
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve audit log from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return auditLogList;
        
    }
    
}
