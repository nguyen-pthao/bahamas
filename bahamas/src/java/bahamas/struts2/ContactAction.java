/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.struts2;

import bahamas.dao.ContactDAO;
import bahamas.dao.impl.ContactDAOImpl;
import bahamas.entity.Contact;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Marcus
 */
public class ContactAction extends ActionSupport implements ModelDriven {

    Contact contact = new Contact();
    List<Contact> contactList = new ArrayList<Contact>();
    ContactDAO contactDAO = new ContactDAOImpl();

    public String execute() throws Exception {
        return SUCCESS;
    }

    public Object getModel() {
        return contact;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    //save customer
    public String addContact() throws Exception {

        //save it
        contact.setDateCreated(new Date());
        contactDAO.addContact(contact);

        //reload the customer list
        contactList = null;
        contactList = contactDAO.listContact();

        return SUCCESS;

    }

    //list all customers
    public String listContact() throws Exception {

        contactList = contactDAO.listContact();

        return SUCCESS;

    }

}
