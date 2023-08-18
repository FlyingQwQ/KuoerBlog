package top.kuoer.entity;

public class RolePermission {

    private int id;
    private int roleid;
    private int permissionid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(int permissionid) {
        this.permissionid = permissionid;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "id=" + id +
                ", roleid=" + roleid +
                ", permissionid=" + permissionid +
                '}';
    }

}
