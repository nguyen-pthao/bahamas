/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

/**
 *
 * @author tan.si.hao
 */
public class AppNotification {
    int notification_id;
    int contact_id;
    Integer event_id;
    String state;
    String message;

    public AppNotification(int notification_id, int contact_id, Integer event_id, String state, String message) {
        this.notification_id = notification_id;
        this.contact_id = contact_id;
        this.event_id = event_id;
        this.state = state;
        this.message = message;
    }
    
    public AppNotification(int contact_id, Integer event_id, String state, String message) {
        this.contact_id = contact_id;
        this.event_id = event_id;
        this.state = state;
        this.message = message;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }    
    
    
}
