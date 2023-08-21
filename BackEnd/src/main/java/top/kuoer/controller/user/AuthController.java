package top.kuoer.controller.user;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.common.Result;
import top.kuoer.service.AuthService;

@RestController
@RequestMapping(path="/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(path = "/getallrole", method = RequestMethod.GET)
    @RequiresPermissions("auth:getallrole")
    public Result getAllRole() {
        return this.authService.getAllRole();
    }

    @RequestMapping(path = "/getallpermission", method = RequestMethod.GET)
    @RequiresPermissions("auth:getallpermission")
    public Result getAllPermission() {
        return this.authService.getAllPermission();
    }

    @RequestMapping(path = "/getrolepermission", method = RequestMethod.GET)
    @RequiresPermissions("auth:getrolepermission")
    public Result getRolePerission(@RequestParam("roleid") int roleid) {
        return this.authService.getRolePerission(roleid);
    }

    @RequestMapping(path = "/modifyrolepermission", method = RequestMethod.POST)
    @RequiresPermissions("auth:modifyrolepermission")
    public Result modifyRolePerission(@RequestParam("roleid") int roleid,
                                      @RequestParam("roleName") String roleName,
                                      @RequestParam("roleDescription") String roleDescription,
                                      @RequestParam("permissionids") String permissionids) {
        return this.authService.modifyRolePerission(roleid, roleName, roleDescription, permissionids);
    }

    @RequestMapping(path = "/removerole", method = RequestMethod.POST)
    @RequiresPermissions("auth:removerole")
    public Result removeRole(@RequestParam("roleId") int roleid) {
        return this.authService.removeRole(roleid);
    }

    @RequestMapping(path = "/removeperission", method = RequestMethod.POST)
    @RequiresPermissions("auth:removeperission")
    public Result removePerission(@RequestParam("permissionId") int permissionId) {
        return this.authService.removePerission(permissionId);
    }

    @RequestMapping(path = "/addrole", method = RequestMethod.POST)
    @RequiresPermissions("auth:addrole")
    public Result addRole(@RequestParam("name") String name, @RequestParam("description") String description) {
        return this.authService.addRole(name, description);
    }
    @RequestMapping(path = "/addperission", method = RequestMethod.POST)
    @RequiresPermissions("auth:addperission")
    public Result addPerission(@RequestParam("name") String name, @RequestParam("description") String description) {
        return this.authService.addPerission(name, description);
    }

}
