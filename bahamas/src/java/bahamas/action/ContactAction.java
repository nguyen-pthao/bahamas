/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.action;

import bahamas.dao.ContactDAO;
import bahamas.entity.Contact;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import java.util.*;

/**
 *
 * @author Marcus
 */
public class ContactAction extends ActionSupport implements ModelDriven<Contact> {

    Contact contact = new Contact();
    List<Contact> contactList = new ArrayList<Contact>();
    ContactDAO contactDAO = new ContactDAO();

    public String execute() throws Exception {
        Contact c = contactDAO.getLogin(contact.getUsername(),contact.getPassword());
        if(c!= null){
            return SUCCESS;
        }
        
        return ERROR;
    }

    @Override
    public Contact getModel() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

   

}
