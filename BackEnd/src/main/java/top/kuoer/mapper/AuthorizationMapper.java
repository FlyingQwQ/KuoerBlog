package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
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

    @Update("update userrole set roleid=#{roleid} where userid=#{userid}")
    int setUserRoleByUserId(@Param("userid") int userid, @Param("roleid") int roleid);

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

    @Update("update roles set name=#{name}, description=#{description} where id=#{roleId}")
    int setRoleInfoByRoleId(@Param("roleId") int roleId, @Param("name") String name, @Param("description") String description);

    @Delete("delete from rolepermission where roleid=#{roleId}")
    int clearAllRolePermissionByRoleId(int roleId);

    @Insert("insert into rolepermission (roleid, permissionid) values (#{roleId}, #{permissionId})")
    int addRolePermission(@Param("roleId") int roleId, @Param("permissionId") int permissionId);

    @Insert("insert into roles (name, description) values (#{name}, #{description})")
    int addRole(@Param("name") String name, @Param("description") String description);

    @Insert("insert into permissions (name, description) values (#{name}, #{description})")
    int addPermission(@Param("name") String name, @Param("description") String description);

    @Delete("delete from roles where id=#{id}")
    int removeRole(@Param("id") int roleId);

    @Delete("delete from permissions where id=#{id}")
    int removePermission(@Param("id") int permissionId);

}
