package top.kuoer.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.Permission;
import top.kuoer.entity.Role;
import top.kuoer.entity.RolePermission;
import top.kuoer.entity.UserRole;

import java.util.List;

@Mapper
@Repository
public interface AuthorizationMapper {

    @Select("select * from userrole where userid=#{userid}")
    List<UserRole> getUserRoleByUserId(int userid);

    @Select("select * from rolepermission where roleid=#{roleid}")
    List<RolePermission> getRolePerissionByRoleId(int roleid);

    @Select("select * from roles where id=#{roleid}")
    Role getRoleByRoleId(int roleid);

    @Select("select * from permissions where id=#{permissionid}")
    Permission getPermissionByPermissionId(int permissionid);

    @Select("select * from roles")
    List<Role> getAllRole();

    @Select("select * from permissions")
    List<Permission> getAllPermission();

}
