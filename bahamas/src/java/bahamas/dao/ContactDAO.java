/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HUXLEY
 */
public class ContactDAO {
    
    private ArrayList<Contact> contactList;
    
    public ContactDAO(){
        
        contactList = new ArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("Select CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,ISADMIN,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "from contact");

            rs = stmt.executeQuery();
            while (rs.next()) {
                
                int contactId = Integer.parseInt(rs.getString(1));
                String contactType = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                boolean isAdmin = false;
                if (rs.getString(5).equals("Y")){
                    isAdmin = true;
                }
                boolean deactivated = true;
                if (rs.getString(6).equals("N")){
                    deactivated = false;
                }
                String dateStr = rs.getString(7);
                Date dateCreated = (Date)formatter.parse(dateStr);
                String createdBy = rs.getString(8);
                String name = rs.getString(9);
                String altName = rs.getString(10);
                String explainIfOther = rs.getString(11);
                String profession = rs.getString(12);
                String jobTitle = rs.getString(13);
                String nric = rs.getString(14);
                String gender = rs.getString(15);
                String nationality = rs.getString(16);
                String dateOfBirth = rs.getString(17);
                String profilePic = rs.getString(18);
                String remarks = rs.getString(19);
                boolean notification = true;
                if (rs.getString(20).equals("N")){
                    deactivated = false;
                }
                
                Contact contact = new Contact(contactId, contactType, username, password, isAdmin, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                if (contact != null) {
                    contactList.add(contact);
                }
            }
        } catch (ParseException e) {
		e.printStackTrace();
	} catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to create student from database data", ex);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
    }
    
    public Contact retrieveStudentByUsername(String usernameInput){
        
        contactList = new ArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("SELECT CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,ISADMIN,"
                    + "DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION "
                    + "FROM contact WHERE username = (?)");
            stmt.setString(1, usernameInput);

            rs = stmt.executeQuery();
            while (rs.next()) {
                
                int contactId = Integer.parseInt(rs.getString(1));
                String contactType = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                boolean isAdmin = false;
                if (rs.getString(5).equals("Y")){
                    isAdmin = true;
                }
                boolean deactivated = true;
                if (rs.getString(6).equals("N")){
                    deactivated = false;
                }
                String dateStr = rs.getString(7);
                Date dateCreated = format.parse(dateStr);
                String createdBy = rs.getString(8);
                String name = rs.getString(9);
                String altName = rs.getString(10);
                String explainIfOther = rs.getString(11);
                String profession = rs.getString(12);
                String jobTitle = rs.getString(13);
                String nric = rs.getString(14);
                String gender = rs.getString(15);
                String nationality = rs.getString(16);
                String dateOfBirth = rs.getString(17);
                String profilePic = rs.getString(18);
                String remarks = rs.getString(19);
                boolean notification = true;
                if (rs.getString(20).equals("N")){
                    deactivated = false;
                }
                
                Contact contact = new Contact(contactId, contactType, username, password, isAdmin, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                return contact;
            }
        } catch (ParseException e) {
		e.printStackTrace();
	} catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to create student from database data", ex);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }
    
    
    /**
     *retrieve contact list
     * @return ArrayList of Students
     */
    public ArrayList<Contact> retrieveContactList() {
        return contactList;
    }
    
}
