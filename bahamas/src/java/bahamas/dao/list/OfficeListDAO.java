/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao.list;

import bahamas.dao.ConnectionManager;
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
public class OfficeListDAO {
    
    private ArrayList<String> officeList;
    

    public OfficeListDAO() {
    }
    
    public ArrayList<String> retrieveOfficeList() {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        officeList = new ArrayList<String>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM OFFICE_LIST");

            rs = stmt.executeQuery();
            while (rs.next()) {
                officeList.add(rs.getString(2));
            }

        } catch (SQLException ex) {
            Logger.getLogger(OfficeListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve Office list from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return officeList;
    }
    
}
