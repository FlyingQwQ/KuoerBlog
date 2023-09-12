package top.kuoer.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import top.kuoer.entity.*;
import top.kuoer.mapper.AuthorizationMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class JwtRealm extends AuthorizingRealm {


    private final AuthorizationMapper  authorizationMapper;

    public JwtRealm(AuthorizationMapper  authorizationMapper) {
        this.authorizationMapper = authorizationMapper;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String token = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        int id = Integer.parseInt(Objects.requireNonNull(JwtUtil.getInfo(token, "id")));

        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        List<UserRole> userRoleList = this.authorizationMapper.getUserRoleByUserId(id);
        for(UserRole userRole : userRoleList) {
            Role role = this.authorizationMapper.getRoleByRoleId(userRole.getRoleid());
            roles.add(role.getName());
            List<RolePermission> permissionList = this.authorizationMapper.getRolePerissionByRoleId(userRole.getRoleid());
            for(RolePermission rolePermission : permissionList) {
                Permission permission = this.authorizationMapper.getPermissionByPermissionId(rolePermission.getPermissionid());
                if(null != permission) {
                    permissions.add(permission.getName());
                }
            }
        }

        info.setRoles(roles);
        info.setStringPermissions(permissions);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String userName = JwtUtil.getInfo(token, "name");
        if(null == userName) {
            throw new AuthenticationException("token认证失败！");
        }

        return new SimpleAuthenticationInfo(token, token, "JwtRealm");
    }


}
