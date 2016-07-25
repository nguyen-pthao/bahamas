/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

}
