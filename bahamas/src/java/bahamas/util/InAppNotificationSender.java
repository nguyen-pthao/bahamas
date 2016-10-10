/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.util;

import bahamas.dao.AppNotificationDAO;
import bahamas.dao.ContactDAO;
import bahamas.entity.AppNotification;
import bahamas.entity.Contact;
import java.util.HashMap;

/**
 *
 * @author tan.si.hao
 */
public class InAppNotificationSender {

    public static void updateDetailsNotification(HashMap<Integer, String> cidNamePairHM, int eventId, String eventTitle, Contact contact) {
        ContactDAO contactDAO = new ContactDAO();
        for (int tempContactId : cidNamePairHM.keySet()) {
            if (contactDAO.retrieveContactById(tempContactId).getUsername() != null && tempContactId != contact.getContactId()) {
                AppNotification appNotification = new AppNotification(tempContactId, eventId, ".viewIndivEvent", "Event \"" + eventTitle + "\" has been updated. Click to view event.");
                AppNotificationDAO.addAppNotification(appNotification);
            }
        }
    }

}
