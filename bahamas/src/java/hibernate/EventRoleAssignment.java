package hibernate;
// Generated 14 May, 2016 6:30:30 PM by Hibernate Tools 4.3.1



/**
 * EventRoleAssignment generated by hbm2java
 */
public class EventRoleAssignment  implements java.io.Serializable {


     private int roleId;
     private int eventId;

    public EventRoleAssignment() {
    }

    public EventRoleAssignment(int roleId, int eventId) {
       this.roleId = roleId;
       this.eventId = eventId;
    }
   
    public int getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public int getEventId() {
        return this.eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }




}


