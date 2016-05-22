/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;
import bahamas.util.HibernateUtil;

import java.util.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import org.hibernate.Query;

/**
 *
 * @author Marcus
 */
public class ContactDAO {

    private Session session = HibernateUtil.getSession();

    public Contact getLogin(String username, String password) {

        String sql = "from Contact where username=:username and password=:password";
        Query query = session.createQuery(sql);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<Contact> list = query.list();
        
        if (list.size() > 0) {
            session.close();
            return list.get(0);
        }
        session.close();
        return null;
    }
}
