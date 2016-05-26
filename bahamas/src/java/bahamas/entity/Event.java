/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.entity;

import java.util.*;

/**
 *
 * @author huxley.goh
 */
public class Event {
    
    private long eventId;
    private Date dateCreated;
    private Date eventStart;
    private Date eventEnd;
    private String eventTitle;
    private String explainIfOthers;
    private String eventDescription;
    private int minimumParticipation;
    private boolean sendReminder;
    private String explainIfOtherLocation;
    
    private String eventClassName;
    private String eventLocationName;
    
    private ArrayList<Contact> participants;
    
    private ArrayList<Role> roleList;
    
    private ArrayList<Team> teamAffliated;
    private boolean workerRelated;
    private String remarks;
    
    private ArrayList<Notice> noticeList;
    
    
    
    
    
    
}
