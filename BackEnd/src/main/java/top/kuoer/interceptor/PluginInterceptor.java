package top.kuoer.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.kuoer.BlogApplication;
import top.kuoer.common.Result;
import top.kuoer.enums.ResultCode;
import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.Route;
import top.kuoer.plugin.event.RequestEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class PluginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        List<AppPlugin> appPluginList = BlogApplication.pluginManager.getAllPlugins();
        for(AppPlugin plugin : appPluginList) {
            for(Method method : plugin.getClass().getMethods()) {
                if (method.isAnnotationPresent(Route.class)) {
                    Route annotation = method.getAnnotation(Route.class);
                    if(annotation.value().equals(uri)) {
                        if(!this.checkPermission(annotation.permissions())) {
                            this.sendJson(response, new Result(ResultCode.NOAUTH, "你需要拥有 " + Arrays.toString(annotation.permissions()) + " 才能访问"));
                            return false;
                        }

                        RequestEvent requestEvent = new RequestEvent(request, response);
                        method.invoke(plugin, requestEvent);
                        return false;
                    }

                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public boolean checkPermission(String[] permissions) {
        Subject subject = SecurityUtils.getSubject();
        for(String permission : permissions) {
            if(!subject.isPermitted(permission)) {
                return false;
            }
        }
        return true;
    }

    public void sendJson(HttpServletResponse response, Object obj) {
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.getWriter().print(objectMapper.writeValueAsString(obj));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
