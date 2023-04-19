package top.kuoer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.annotations.Authentication;
import top.kuoer.common.Result;
import top.kuoer.entity.Admin;
import top.kuoer.entity.QQLogin;
import top.kuoer.enums.ResultCode;
import top.kuoer.service.AdminService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(path="/admin")
public class AdminController {

    private final AdminService adminService;
    @Value("${front-end}")
    private String frontEndAddress;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public Admin login(@RequestParam("username") String name,
                       @RequestParam("password") String password) {
        return adminService.login(name, password);
    }

    @RequestMapping(path = "/qqlogin", method = RequestMethod.POST)
    public void qqLogin(QQLogin login, HttpServletResponse response) throws IOException {
        String token = adminService.qqLogin(login);
        response.sendRedirect(frontEndAddress + "pages/admin/index.html?token=" + token);
    }

    @RequestMapping(path = "/bindqq", method = RequestMethod.POST)
    @Authentication
    public void bindQQ(QQLogin login,
                       @RequestParam("token") String token,
                       HttpServletResponse response) throws IOException {
        String result = adminService.bindQQ(token, login);
        response.sendRedirect(frontEndAddress + "pages/admin/manage.html?result=" + result);
    }

    @RequestMapping(path = "/verification", method = RequestMethod.GET)
    @Authentication
    public Result verification(@RequestParam("token") String token) {
        return adminService.verification(token);
    }

    @RequestMapping(path = "/addadmin", method = RequestMethod.GET)
    @Authentication
    public Result addAdmin(@RequestParam("token") String token,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password) {
        return adminService.addAdmin(username, password);
    }

    @RequestMapping(path = "/adminlist", method = RequestMethod.GET)
    @Authentication
    public Result adminList(@RequestParam("token") String token) {
        return new Result(ResultCode.SUCCESS, adminService.adminList());
    }

    @RequestMapping(path = "/removeadmin", method = RequestMethod.GET)
    @Authentication
    public Result delAdmin(
            @RequestParam("token") String token,
            @RequestParam("id") int id) {
        return adminService.removeAdmin(id);
    }

    @RequestMapping(path = "/modifyadmin", method = RequestMethod.GET)
    @Authentication
    public Result modifyAdmin(@RequestParam("token") String token,
                           @RequestParam("username") String userName,
                           @RequestParam("password") String password) {
        return adminService.modifyAdmin(userName, password);
    }
}
