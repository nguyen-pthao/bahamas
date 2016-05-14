/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.dao;

import bahamas.entity.Contact;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Marcus
 */
public interface ContactDAO {

    void addContact(Contact contact);
	
	List<Contact> listContact();
}
