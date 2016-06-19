/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author HUXLEY
 */
public class ProxyDAO {

    public static boolean addProxy(Proxy p) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO PROXY (PRINCIPAL_ID,PROXY_ID"
                    + "DATE_CREATED,CREATED_BY,PROXY_STANDING,DATE_OBSOLETE,REMARKS)"
                    + " VALUES (?,?,?,?,?,?,?)");

            stmt.setInt(1, p.getPrincipal().getContactId());
            stmt.setInt(2, p.getProxy().getContactId());
            stmt.setTimestamp(3, new java.sql.Timestamp(p.getDateCreated().getTime()));
            stmt.setString(4, p.getCreatedBy());
            stmt.setString(5, p.getProxyStanding());
            if(p.getDateObsolete()!= null){
                stmt.setDate(6, new java.sql.Date(p.getDateObsolete().getTime()));
            }
            else{
                stmt.setDate(6, null);
            }
            
            stmt.setString(7, p.getRemarks());
          
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
