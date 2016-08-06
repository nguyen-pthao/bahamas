/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao.list;

import bahamas.dao.ConnectionManager;
import bahamas.entity.EventLocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Darryl Mok
 */
public class EventLocationListDAO {
    
    private ArrayList<EventLocation> eventLocationList;
    

    public EventLocationListDAO() {
    }
    
    public ArrayList<EventLocation> retrieveEventLocationList() {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        eventLocationList = new ArrayList<EventLocation>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT EVENT_LOCATION_NAME, ADDRESS, ZIPCODE FROM EVENT_LOCATION_LIST ORDER BY POSITION_NUMBER ASC");

            rs = stmt.executeQuery();
            while (rs.next()) {
                String eventLocationName = rs.getString(1);
                String address = rs.getString(2);
                String zipcode = rs.getString(3);
                if(address == null){
                    address = "";
                }
                if(zipcode == null){
                    zipcode = "";
                }
                eventLocationList.add(new EventLocation(eventLocationName,address,zipcode));
            }

        } catch (SQLException ex) {
            Logger.getLogger(EventLocationListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve Event Location list from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return eventLocationList;
    }
    
}
