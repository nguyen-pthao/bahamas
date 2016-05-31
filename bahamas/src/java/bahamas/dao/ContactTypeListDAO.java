/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.entity.ContactTypeList;
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
 * @author Darryl Mok
 */
public class ContactTypeListDAO {
    
    private ArrayList<String> contactTypeList;
    

    public ContactTypeListDAO() {
    }
    
    public ArrayList<String> retrieveAllContactTypeList() {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        contactTypeList = new ArrayList<String>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM TYPE_OF_CONTACT_LIST");

            rs = stmt.executeQuery();
            while (rs.next()) {
                contactTypeList.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ContactTypeListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact type list from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return contactTypeList;
    }
    
    
}
