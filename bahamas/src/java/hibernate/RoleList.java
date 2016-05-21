package hibernate;
// Generated 21 May, 2016 5:04:19 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * RoleList generated by hbm2java
 */
public class RoleList  implements java.io.Serializable {


     private int roleId;
     private String roleDescription;
     private Set<Event> events = new HashSet<Event>(0);
     private Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>(0);

    public RoleList() {
    }

	
    public RoleList(int roleId) {
        this.roleId = roleId;
    }
    public RoleList(int roleId, String roleDescription, Set<Event> events, Set<EventParticipant> eventParticipants) {
       this.roleId = roleId;
       this.roleDescription = roleDescription;
       this.events = events;
       this.eventParticipants = eventParticipants;
    }
   
    public int getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public String getRoleDescription() {
        return this.roleDescription;
    }
    
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    public Set<Event> getEvents() {
        return this.events;
    }
    
    public void setEvents(Set<Event> events) {
        this.events = events;
    }
    public Set<EventParticipant> getEventParticipants() {
        return this.eventParticipants;
    }
    
    public void setEventParticipants(Set<EventParticipant> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }




}

