package top.kuoer.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.kuoer.BlogApplication;
import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.Route;
import top.kuoer.plugin.event.RequestEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.lang.reflect.Method;
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
}
