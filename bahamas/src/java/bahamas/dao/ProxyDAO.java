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
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            if (p.getDateObsolete() != null) {
                stmt.setDate(6, new java.sql.Date(p.getDateObsolete().getTime()));
            } else {
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

    public static ArrayList<Proxy> retrieveByProxyCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Proxy> proxyList;
        proxyList = new ArrayList<Proxy>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT PRINCIPAL_ID, PROXY_ID, DATE_CREATED, CREATED_BY, PROXY_STANDING, DATE_OBSOLETE, REMARKS FROM PROXY WHERE PROXY_ID = (?) ");
            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int proxyID = rs.getInt(1);
                int principalID = rs.getInt(2);
                Date dateCreated = rs.getTimestamp(3);
                String createdBy = rs.getString(4);
                String proxyStanding = rs.getString(5);
                Date dateObsolete = rs.getDate(6);
                String remarks = rs.getString(7);

                Proxy proxy = new Proxy(proxyID, principalID, dateCreated, createdBy, proxyStanding, dateObsolete, remarks);
                proxyList.add(proxy);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PROXY from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return proxyList;
    }

    public static ArrayList<Proxy> retrieveByPrincipalCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Proxy> principalList;
        principalList = new ArrayList<Proxy>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT PRINCIPAL_ID, PROXY_ID, DATE_CREATED, CREATED_BY, PROXY_STANDING, DATE_OBSOLETE, REMARKS FROM PROXY WHERE PRINCIPAL_ID = (?) ");
            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int proxyID = rs.getInt(1);
                int principalID = rs.getInt(2);
                Date dateCreated = rs.getTimestamp(3);
                String createdBy = rs.getString(4);
                String proxyStanding = rs.getString(5);
                Date dateObsolete = rs.getDate(6);
                String remarks = rs.getString(7);

                Proxy proxy = new Proxy(proxyID, principalID, dateCreated, createdBy, proxyStanding, dateObsolete, remarks);
                principalList.add(proxy);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MembershipDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve PROXY from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return principalList;
    }

    public static boolean updateProxy(Proxy p) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("UPDATE PROXY SET "
                    + "PROXY_STANDING=?,DATE_OBSOLETE=?,REMARKS=? "
                    + "WHERE PRINCIPAL_ID=? AND PROXY_ID=?");

            stmt.setString(1, p.getProxyStanding());
            if (p.getDateObsolete() != null) {
                stmt.setDate(2, new java.sql.Date(p.getDateObsolete().getTime()));
            } else {
                stmt.setDate(2, null);
            }

            stmt.setString(3, p.getRemarks());
            stmt.setInt(4, p.getPrincipal().getContactId());
            stmt.setInt(5, p.getProxy().getContactId());
            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean deleteProxy(int principalId, int proxyId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM PROXY "
                    + "WHERE PRINCIPAL_ID=? AND PROXY_ID=?");

            stmt.setInt(1,principalId);
            stmt.setInt(2,proxyId);
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
