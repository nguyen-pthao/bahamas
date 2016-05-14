/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao.impl;

import bahamas.dao.ContactDAO;
import bahamas.entity.Contact;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Marcus
 */
public class ContactDAOImpl implements ContactDAO{

    @SessionTarget
    Session session;

    @TransactionTarget
    Transaction transaction;

    public void addContact(Contact contact) {
        session.save(contact);
    }

    @Override
    public List<Contact> listContact() {
        return session.createQuery("from Contact").list();
    }
}
