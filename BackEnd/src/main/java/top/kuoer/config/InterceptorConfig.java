package top.kuoer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kuoer.interceptor.PluginInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 插件系统要用，可以拦截所有的url
        registry.addInterceptor(new PluginInterceptor())
                .addPathPatterns("/**"); // 拦截所有URL


    }

}
