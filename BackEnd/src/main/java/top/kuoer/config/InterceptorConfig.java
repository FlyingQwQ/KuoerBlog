package top.kuoer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kuoer.interceptor.PluginInterceptor;
import top.kuoer.interceptor.VerifyInterceptor;
import top.kuoer.plugin.PluginManager;
import top.kuoer.service.AdminService;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private AdminService adminService;

    @Autowired
    public InterceptorConfig(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 自定义拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        VerifyInterceptor verifyInterceptor = new VerifyInterceptor();
        verifyInterceptor.setAdminService(this.adminService);
        registry.addInterceptor(verifyInterceptor)
                .addPathPatterns("/admin/adminlist")
                .addPathPatterns("/admin/removeadmin")
                .addPathPatterns("/admin/modifyadmin")
                .addPathPatterns("/admin/addadmin")
                .addPathPatterns("/admin/verification")
                .addPathPatterns("/admin/bindqq")

                .addPathPatterns("/comment/delComment")

                .addPathPatterns("/home/addPosts")
                .addPathPatterns("/home/modifyPosts");


        // 插件系统要用，可以拦截所有的url
        registry.addInterceptor(new PluginInterceptor())
                .addPathPatterns("/**"); // 拦截所有URL


    }

}
