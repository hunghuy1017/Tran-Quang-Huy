package models;

public class Roles {
    private int roleID;
    private String roleName;
    private boolean status;
    private String description;

    public Roles() {}

    public Roles(int roleID, String roleName, boolean status, String description) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.status = status;
        this.description = description;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleID=" + roleID +
                ", roleName='" + roleName + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
