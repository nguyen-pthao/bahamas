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
<<<<<<< HEAD
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
=======
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMMMM-yyyy");
>>>>>>> 34a47db001db96778f330b967d9fd3e09ceca732

    public ContactDAO() {
    }

    public ArrayList<Contact> retrieveAllContact() {

<<<<<<< HEAD
        contactList = new ArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
=======
        contactList = new ArrayList<Contact>();
        Date startDate = null;
>>>>>>> 34a47db001db96778f330b967d9fd3e09ceca732

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
                if (rs.getString(5).equals("Y")) {
                    isAdmin = true;
                }
                boolean deactivated = true;
                if (rs.getString(6).equals("N")) {
                    deactivated = false;
                }
                String dateStr = rs.getString(7);
<<<<<<< HEAD
                Date dateCreated = sdf.parse(dateStr);
=======
                Date dateCreated = format.parse(dateStr);
>>>>>>> 34a47db001db96778f330b967d9fd3e09ceca732
                String createdBy = rs.getString(8);
                String name = rs.getString(9);
                String altName = rs.getString(10);
                String explainIfOther = rs.getString(11);
                String profession = rs.getString(12);
                String jobTitle = rs.getString(13);
                String nric = rs.getString(14);
                String gender = rs.getString(15);
                String nationality = rs.getString(16);
                String dateOfBirth = sdf.format(sdf.parse(rs.getString(17)));
                String profilePic = rs.getString(18);
                String remarks = rs.getString(19);
                boolean notification = true;
                if (rs.getString(20).equals("N")) {
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
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return contactList;
    }

    public Contact retrieveContactByUsername(String usernameInput) {
        contactList = new ArrayList();
<<<<<<< HEAD
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
=======
        Date startDate = null;
>>>>>>> 34a47db001db96778f330b967d9fd3e09ceca732

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
                if (rs.getString(5).equals("Y")) {
                    isAdmin = true;
                }
                boolean deactivated = true;
                if (rs.getString(6).equals("N")) {
                    deactivated = false;
                }
                String dateStr = rs.getString(7);
                Date dateCreated = sdf.parse(dateStr);
                String createdBy = rs.getString(8);
                String name = rs.getString(9);
                String altName = rs.getString(10);
                String explainIfOther = rs.getString(11);
                String profession = rs.getString(12);
                String jobTitle = rs.getString(13);
                String nric = rs.getString(14);
                String gender = rs.getString(15);
                String nationality = rs.getString(16);
                String dateOfBirth = sdf.format(sdf.parse(rs.getString(17)));
                String profilePic = rs.getString(18);
                String remarks = rs.getString(19);
                boolean notification = true;
                if (rs.getString(20).equals("N")) {
                    deactivated = false;
                }

                Contact contact = new Contact(contactId, contactType, username, password, isAdmin, deactivated, dateCreated, createdBy, name, altName, explainIfOther, profession, jobTitle, nric, gender, nationality, dateOfBirth, profilePic, remarks, notification);
                return contact;
            }
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ContactDAO.class.getName()).log(Level.SEVERE, "Unable to retrieve contact from database data", ex);
            ex.printStackTrace();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return null;
    }

    public static boolean addContact(Contact c) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;

        try {
            //get database connection
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement("INSERT INTO contact (CONTACT_TYPE,ISADMIN,"
                    + "DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,"
                    + "JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,"
                    + "REMARKS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            stmt.setString(1, c.getContactType());
            stmt.setBoolean(2, c.isIsAdmin());
            stmt.setDate(3, new java.sql.Date(c.getDateCreated().getTime()));
            stmt.setString(4, c.getCreatedBy());
            stmt.setString(5, c.getName());
            stmt.setString(6, c.getAltName());
            stmt.setString(7, c.getExplainIfOther());
            stmt.setString(8, c.getProfession());
            stmt.setString(9, c.getJobTitle());
            stmt.setString(10, c.getNric());
            stmt.setString(11, c.getGender());
            stmt.setString(12, c.getNationality());
            stmt.setString(13, c.getDateOfBirth());
            stmt.setString(14, c.getProfilePic());
            stmt.setString(15, c.getRemarks());

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
