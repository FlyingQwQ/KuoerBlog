package top.kuoer.service;

import top.kuoer.common.Result;

public interface AuthService {

    /**
     * 获取全部Role
     * @return Roles
     */
    Result getAllRole();

    /**
     * 获取全部Permission
     * @return Permission
     */
    Result getAllPermission();

    /**
     * 通过RoleId获取它的所有的权限
     * @param roleid 角色id
     * @return Permission列表
     */
    Result getRolePerission(int roleid);

    /**
     * 修改角色
     * @param roleid 角色id
     * @param roleName 角色名
     * @param roleDescription 角色介绍
     * @param permissionids 需要保留的权限
     * @return 结果
     */
    Result modifyRolePerission(int roleid, String roleName, String roleDescription, String permissionids);

    /**
     * 删除角色
     * @param roleid 角色id
     * @return 结果
     */
    Result removeRole(int roleid);

    /**
     * 删除权限
     * @param permissionId 权限id
     * @return 结果
     */
    Result removePerission(int permissionId);

}
