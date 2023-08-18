package top.kuoer.entity;

public class UserRole {

    private int id;
    private int userid;
    private int roleid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", userid=" + userid +
                ", roleid=" + roleid +
                '}';
    }

}
