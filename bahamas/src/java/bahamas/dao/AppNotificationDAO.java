/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.AppNotification;
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
 * @author tan.si.hao
 */
public class AppNotificationDAO {

    public static ArrayList<AppNotification> retrieveAppNotificationListByCID(int cid) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<AppNotification> appNotificationList = new ArrayList<AppNotification>();
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT `NOTIFICATION_ID`, `CONTACT_ID`, `EVENT_ID`, `STATE`, `MESSAGE` "
                    + "FROM APP_NOTIFICATION WHERE CONTACT_ID = (?) ORDER BY NOTIFICATION_ID DESC");
            stmt.setInt(1, cid);

            rs = stmt.executeQuery();
            while (rs.next()) {

                int notificationId = rs.getInt(1);
                int contactId = rs.getInt(2);
                int eventId = rs.getInt(3);
                String state = rs.getString(4);
                String message = rs.getString(5);

                AppNotification appNotification = new AppNotification(notificationId, contactId, eventId, state, message);
                appNotificationList.add(appNotification);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AppNotificationDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve APP_NOTIFICATION from database", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return appNotificationList;

    }

    public static boolean addAppNotification(AppNotification appNotification) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO APP_NOTIFICATION ("
                    + "`CONTACT_ID`, `EVENT_ID`, `STATE`, `MESSAGE`) VALUES (?,?,?,?)");

            stmt.setInt(1, appNotification.getContact_id());
            if(appNotification.getEvent_id() != null){
                stmt.setInt(2, appNotification.getEvent_id());
            } else {
                stmt.setString(2, null);
            }
            stmt.setString(3, appNotification.getState());
            stmt.setString(4, appNotification.getMessage());

            result = stmt.executeUpdate();

            return result == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return false;
    }

    public static boolean updateMembership(int notificationId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("DELETE FROM APP_NOTIFICATION WHERE NOTIFICATION_ID=?");

            //stmt.setBoolean(1, readStatus);
            stmt.setInt(1, notificationId);
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
