package top.kuoer.controller.user;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.common.Result;
import top.kuoer.entity.User;
import top.kuoer.enums.ResultCode;
import top.kuoer.service.UserService;

@RestController
@RequestMapping(path="/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public User login(@RequestParam("username") String name,
                      @RequestParam("password") String password) {
        return this.userService.login(name, password);
    }

    @RequestMapping(path = "/verification", method = RequestMethod.GET)
    public Result verification(@RequestParam("token") String token) {
        return this.userService.verification(token);
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    @RequiresPermissions("user:add")
    public Result addAdmin(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        return this.userService.add(username, password);
    }

    @RequestMapping(path = "/userlist", method = RequestMethod.GET)
    @RequiresPermissions("user:list")
    public Result userList() {
        return new Result(ResultCode.SUCCESS, this.userService.userList());
    }

    @RequestMapping(path = "/remove", method = RequestMethod.GET)
    @RequiresPermissions("user:remove")
    public Result delUser(@RequestParam("id") int id) {
        return this.userService.remove(id);
    }

    @RequestMapping(path = "/modify", method = RequestMethod.GET)
    @RequiresPermissions("user:modify")
    public Result modifyUser(@RequestParam("userid") int userid,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam("roleid") int roleid) {
        return this.userService.modify(userid, password, roleid);
    }

    @RequestMapping(path = "/getuserinfo", method = RequestMethod.GET)
    public Result getUserInfo(@RequestParam("token") String token) {
        return this.userService.getUserInfo(token);
    }

}
