package top.kuoer.config;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kuoer.mapper.AuthorizationMapper;
import top.kuoer.shiro.JwtFilter;
import top.kuoer.shiro.JwtRealm;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private AuthorizationMapper authorizationMapper;

    @Autowired
    public ShiroConfig(AuthorizationMapper authorizationMapper) {
        this.authorizationMapper = authorizationMapper;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Autowired DefaultWebSecurityManager defaultWebSecurityManager) throws Exception {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(getStringStringMap());

//        AbstractShiroFilter abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
//        PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
//        DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager();
//        System.out.println(defaultFilterChainManager.getFilterChains().get("/auth/modifyrolepermission"));

        return shiroFilterFactoryBean;
    }

    private static Map<String, String> getStringStringMap() {
        Map<String, String> filterChain = new HashMap<>();
        filterChain.put("/user/userlist", "jwt");
        filterChain.put("/user/verification", "jwt");
        filterChain.put("/user/getuserinfo", "jwt");
        filterChain.put("/user/modify", "jwt");
        filterChain.put("/user/add", "jwt");
        filterChain.put("/user/remove", "jwt");
        filterChain.put("/user/test", "jwt");

        filterChain.put("/auth/getallrole", "jwt");
        filterChain.put("/auth/getallpermission", "jwt");
        filterChain.put("/auth/getrolepermission", "jwt");
        filterChain.put("/auth/modifyrolepermission", "jwt");
        filterChain.put("/auth/addrole", "jwt");
        filterChain.put("/auth/addperission", "jwt");
        filterChain.put("/auth/removerole", "jwt");
        filterChain.put("/auth/removeperission", "jwt");

        filterChain.put("/home/modifyPosts", "jwt");
        filterChain.put("/home/addPosts", "jwt");
        filterChain.put("/home/removePosts", "jwt");

        filterChain.put("/friendchain/modifyFriendChain", "jwt");
        filterChain.put("/friendchain/removeFriendChain", "jwt");
        filterChain.put("/friendchain/addFriendChain", "jwt");

        filterChain.put("/plugin/findallplugininfo", "jwt");

        // 使用这个后所有接口都会变成匿名接口
//        filterChain.put("/**", "anon");
        return filterChain;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(new JwtRealm(authorizationMapper));

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

        manager.setSubjectDAO(subjectDAO);
        return manager;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Autowired DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
