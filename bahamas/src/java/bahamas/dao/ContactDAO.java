/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Marcus
 */
public class ContactDAO {

    @SessionTarget
    Session session;

    @TransactionTarget
    Transaction transaction;

    public void addContact(Contact contact) {
        session.save(contact);
    }

    public List<Contact> listContact() {
        return session.createQuery("from Contact").list();
    }

    public Contact retrieveContact(String username) {
        return (Contact) session.load(Contact.class, username);
    }

}
