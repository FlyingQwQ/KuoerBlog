package top.kuoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuoer.common.Result;
import top.kuoer.entity.*;
import top.kuoer.enums.ResultCode;
import top.kuoer.mapper.AuthorizationMapper;
import top.kuoer.mapper.UserMapper;
import top.kuoer.service.UserService;
import top.kuoer.shiro.JwtUtil;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private AuthorizationMapper authorizationMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, AuthorizationMapper authorizationMapper) {
        this.userMapper = userMapper;
        this.authorizationMapper = authorizationMapper;
    }

    @Override
    public User login(String userName, String password) {
        Integer id = this.userMapper.findIdByNameAndPassword(userName, password);
        if(null != id) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("id", String.valueOf(id));
            userInfo.put("name", userName);

            User user = new User();
            user.setId(id);
            user.setToken(JwtUtil.sign(userInfo));
            user.setName(userName);
            user.setPassword(password);
            return user;
        }
        return new User(-1, null, null, null);
    }

    @Override
    public Result verification(String token) {
        return new Result(ResultCode.SUCCESS, "验证成功.");
    }


    @Override
    public Result add(String userName, String password) {
        if(null == this.userMapper.checkRepeat(userName)) {
            if(this.userMapper.addAdmin(userName, password) > 0) {
                return new Result(ResultCode.SUCCESS, null);
            }
            return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
        }
        return new Result(ResultCode.OPERATIONFAIL, "该账号已经存在了");
    }

    @Override
    public List<User> userList() {
        List<User> userList = this.userMapper.adminList();
        for(User user : userList) {
            user.setPassword("");
            this.setUserRolePermission(user);
        }
        return userList;
    }

    @Override
    public Result remove(int id) {
        if(this.userMapper.removeAdmin(id) > 0) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    @Override
    public Result modify(int userid, String password, int roleid) {
        if(!password.isEmpty()) {
            this.userMapper.modifyUser(userid, password);
        }
        this.authorizationMapper.setUserRoleByUserId(userid, roleid);
        return new Result(ResultCode.SUCCESS, "修改成功！");
    }

    @Override
    public Result getUserInfo(String token) {
        int id = Integer.parseInt(Objects.requireNonNull(JwtUtil.getInfo(token, "id")));
        User user = this.userMapper.getUserInfoById(id);
        user.setToken(token);
        this.setUserRolePermission(user);
        return new Result(ResultCode.SUCCESS, user);
    }

    public void setUserRolePermission(User user) {
        Set<Role> roles = new HashSet<>();
        Set<Permission> permissions = new HashSet<>();

        List<UserRole> userRoleList = this.authorizationMapper.getUserRoleByUserId(user.getId());
        for(UserRole userRole : userRoleList) {
            Role role = this.authorizationMapper.getRoleByRoleId(userRole.getRoleid());
            roles.add(role);
            List<RolePermission> permissionList = this.authorizationMapper.getRolePerissionByRoleId(userRole.getRoleid());
            for(RolePermission rolePermission : permissionList) {
                Permission permission = this.authorizationMapper.getPermissionByPermissionId(rolePermission.getPermissionid());
                permissions.add(permission);
            }
        }

        user.setRoles(roles);
        user.setPermissions(permissions);
    }

}