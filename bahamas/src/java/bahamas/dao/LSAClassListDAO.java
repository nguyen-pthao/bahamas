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
 * @author Darryl Mok
 */
public class LSAClassListDAO {
    
    private ArrayList<String> LSAClassList;
    

    public LSAClassListDAO() {
    }
    
    public ArrayList<String> retrieveLSAClassList() {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        lsaClassList = new ArrayList<String>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM LSA_CLASS_LIST");

            rs = stmt.executeQuery();
            while (rs.next()) {
                lsaClassList.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(LSAClassListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve LSA Class list from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return lsaClassList;
    }
    
}
