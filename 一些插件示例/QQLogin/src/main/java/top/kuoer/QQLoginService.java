package top.kuoer;

import top.kuoer.entity.Admin;
import top.kuoer.service.AdminService;

public class QQLoginService {

    private QQLoginMapper qqLoginMapper;
    private AdminService adminService;

    public QQLoginService(QQLoginMapper qqLoginMapper, AdminService adminService) {
        this.qqLoginMapper = qqLoginMapper;
        this.adminService = adminService;
    }

    public String qqLogin(QQLogin login) {
        Admin currAdmin = this.qqLoginMapper.findUserInfoByQqOpenId(login.getOpenid());
        if(null != currAdmin) {
            Admin loginAdmin = this.adminService.login(currAdmin.getName(), currAdmin.getPassword());
            return loginAdmin.getToken();
        }
        return null;
    }

    public String bindQQ(String token, QQLogin login) {
        if(this.qqLoginMapper.setQqLoginOpenIdByToken(token, login.getOpenid()) > 0) {
            return "qqsuccess";
        }
        return "qqerror";
    }

}
