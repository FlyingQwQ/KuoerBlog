package top.kuoer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        return this.adminService.login(name, password);
    }

    @RequestMapping(path = "/qqlogin", method = RequestMethod.POST)
    public void qqLogin(QQLogin login, HttpServletResponse response) throws IOException {
        String token = this.adminService.qqLogin(login);
        response.sendRedirect(this.frontEndAddress + "pages/admin/index.html?token=" + token);
    }

    @RequestMapping(path = "/bindqq", method = RequestMethod.POST)
    public void bindQQ(QQLogin login,
                       @RequestParam("token") String token,
                       HttpServletResponse response) throws IOException {
        String result = this.adminService.bindQQ(token, login);
        response.sendRedirect(this.frontEndAddress + "pages/admin/manage.html?result=" + result);
    }

    @RequestMapping(path = "/verification", method = RequestMethod.GET)
    public Result verification(@RequestParam("token") String token) {
        return this.adminService.verification(token);
    }

    @RequestMapping(path = "/addadmin", method = RequestMethod.GET)
    public Result addAdmin(@RequestParam("token") String token,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password) {
        return this.adminService.addAdmin(username, password);
    }

    @RequestMapping(path = "/adminlist", method = RequestMethod.GET)
    public Result adminList(@RequestParam("token") String token) {
        return new Result(ResultCode.SUCCESS, this.adminService.adminList());
    }

    @RequestMapping(path = "/removeadmin", method = RequestMethod.GET)
    public Result delAdmin(
            @RequestParam("token") String token,
            @RequestParam("id") int id) {
        return this.adminService.removeAdmin(id);
    }

    @RequestMapping(path = "/modifyadmin", method = RequestMethod.GET)
    public Result modifyAdmin(@RequestParam("token") String token,
                           @RequestParam("username") String userName,
                           @RequestParam("password") String password) {
        return this.adminService.modifyAdmin(userName, password);
    }
}
