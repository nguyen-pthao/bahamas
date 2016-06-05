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
 * @author Marcus
 */
public class ModeOfSendingReceiptListDAO {
    
    private ArrayList<String> modeOfSendingReceiptList;
    

    public ModeOfSendingReceiptListDAO() {
    }
    
    public ArrayList<String> retrieveMOSRList() {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        modeOfSendingReceiptList = new ArrayList<String>();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM MODE_OF_SENDING_RECEIPT_LIST");

            rs = stmt.executeQuery();
            while (rs.next()) {
                modeOfSendingReceiptList.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ModeOfSendingReceiptListDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve mode of sending receipt list from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return modeOfSendingReceiptList;
    }
    
}
