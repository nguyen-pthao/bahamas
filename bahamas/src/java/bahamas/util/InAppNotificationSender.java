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
import com.google.gson.JsonArray;
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

    public static void createEventNotification(HashMap<Integer, String> cidNamePairHM, JsonArray eventIdJsonArray, int eventId, String eventTitle, Contact contact) {
        ContactDAO contactDAO = new ContactDAO();
        for (int tempContactId : cidNamePairHM.keySet()) {
            if (eventIdJsonArray.size() == 1) {
                if (contactDAO.retrieveContactById(tempContactId).getUsername() != null && tempContactId != contact.getContactId()) {
                    AppNotification appNotification = new AppNotification(tempContactId, eventId, ".viewIndivEvent", "\"" + eventTitle + "\" event has been created. Click to view event.");
                    AppNotificationDAO.addAppNotification(appNotification);
                }
            } else {
                if (contactDAO.retrieveContactById(tempContactId).getUsername() != null && tempContactId != contact.getContactId()) {
                    AppNotification appNotification = new AppNotification(tempContactId, null, ".viewUpcomingEvents", "\"" + eventTitle + "\" events have been created. Click to view events.");
                    AppNotificationDAO.addAppNotification(appNotification);
                }
            }
        }
    }

}
