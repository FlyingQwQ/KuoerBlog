package top.kuoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuoer.common.Result;
import top.kuoer.entity.Permission;
import top.kuoer.entity.RolePermission;
import top.kuoer.enums.ResultCode;
import top.kuoer.mapper.AuthorizationMapper;
import top.kuoer.service.AuthService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthorizationMapper authorizationMapper;

    @Autowired
    public AuthServiceImpl(AuthorizationMapper authorizationMapper) {
        this.authorizationMapper = authorizationMapper;
    }

    @Override
    public Result getAllRole() {
        return new Result(ResultCode.SUCCESS, this.authorizationMapper.getAllRole());
    }

    @Override
    public Result getAllPermission() {
        return new Result(ResultCode.SUCCESS, this.authorizationMapper.getAllPermission());
    }

    @Override
    public Result getRolePerission(int roleid) {
        List<RolePermission> rolePermissionList = this.authorizationMapper.getRolePerissionByRoleId(roleid);
        List<Permission> permissionList = new ArrayList<>();
        for(RolePermission rolePermission : rolePermissionList) {
            permissionList.add(this.authorizationMapper.getPermissionByPermissionId(rolePermission.getPermissionid()));
        }
        return new Result(ResultCode.SUCCESS, permissionList);
    }

    @Override
    public Result modifyRolePerission(int roleid, String roleName, String roleDescription, String permissionids) {
        String[] permissionidList = permissionids.split(",");
        this.authorizationMapper.setRoleInfoByRoleId(roleid, roleName, roleDescription);
        this.authorizationMapper.clearAllRolePermissionByRoleId(roleid);
        for (String permissionid : permissionidList) {
            if(!permissionid.equals("")) {
                this.authorizationMapper.addRolePermission(roleid, Integer.parseInt(permissionid));
            }
        }
        return new Result(ResultCode.SUCCESS, "修改成功！");
    }

    @Override
    public Result removeRole(int roleid) {
        if(this.authorizationMapper.removeRole(roleid) > 0) {
            this.authorizationMapper.clearAllRolePermissionByRoleId(roleid);
            return new Result(ResultCode.SUCCESS, "删除成功！");
        }
        return new Result(ResultCode.OPERATIONFAIL, "删除失败！");
    }

    @Override
    public Result removePerission(int permissionId) {
        if(this.authorizationMapper.removePermission(permissionId) > 0) {
            return new Result(ResultCode.SUCCESS, "删除成功！");
        }
        return new Result(ResultCode.OPERATIONFAIL, "删除失败！");
    }

}
