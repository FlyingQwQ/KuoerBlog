package top.kuoer;

import top.kuoer.entity.User;
import top.kuoer.service.UserService;

public class QQLoginService {

    private QQLoginMapper qqLoginMapper;
    private UserService userService;

    public QQLoginService(QQLoginMapper qqLoginMapper, UserService userService) {
        this.qqLoginMapper = qqLoginMapper;
        this.userService = userService;
    }

    public String qqLogin(QQLogin login) {
        User currAdmin = this.qqLoginMapper.findUserInfoByQqOpenId(login.getOpenid());
        if(null != currAdmin) {
            User loginAdmin = this.userService.login(currAdmin.getName(), currAdmin.getPassword());
            return loginAdmin.getToken();
        }
        return null;
    }

    public String bindQQ(int userid, QQLogin login) {
        if(this.qqLoginMapper.setQqLoginOpenIdByUserId(userid, login.getOpenid()) > 0) {
            return "qqsuccess";
        }
        return "qqerror";
    }

}
