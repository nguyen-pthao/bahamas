package hibernate;
// Generated 14 May, 2016 6:30:30 PM by Hibernate Tools 4.3.1



/**
 * Role generated by hbm2java
 */
public class Role  implements java.io.Serializable {


     private int roleId;
     private String roleDescription;

    public Role() {
    }

	
    public Role(int roleId) {
        this.roleId = roleId;
    }
    public Role(int roleId, String roleDescription) {
       this.roleId = roleId;
       this.roleDescription = roleDescription;
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




}


