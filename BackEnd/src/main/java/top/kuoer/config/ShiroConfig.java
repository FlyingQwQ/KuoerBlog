package top.kuoer.config;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Autowired DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        Map<String, String> filterChain = new HashMap<>();
        filterChain.put("/**", "anon");

        filterChain.put("/user/userlist", "jwt");
        filterChain.put("/user/verification", "jwt");
        filterChain.put("/user/getuserinfo", "jwt");

        filterChain.put("/home/modifyPosts", "jwt");
        filterChain.put("/home/addPosts", "jwt");

        filterChain.put("/friendchain/modifyFriendChain", "jwt");

        filterChain.put("/plugin/findallplugininfo", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChain);

        return shiroFilterFactoryBean;
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
