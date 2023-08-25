package top.kuoer.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import top.kuoer.BlogApplication;
import top.kuoer.common.Result;
import top.kuoer.entity.MatchPluginDataEntity;
import top.kuoer.enums.ResultCode;
import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.ReqFindPluginData;
import top.kuoer.plugin.event.RequestEvent;
import top.kuoer.service.PluginService;
import top.kuoer.shiro.JwtToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PluginServiceImpl implements PluginService {

    @Override
    public Result pageFindPlugin(String pageURL, RequestEvent requestEvent) {
        String token = requestEvent.getRequest().getParameter("token");
        if(null != token && !token.isEmpty()) {
            try {
                SecurityUtils.getSubject().login(new JwtToken(token));
            } catch (Exception e) {
                return new Result(ResultCode.AUTHERROR, "带Token请求，验证时出现错误，请检查token是否有效");
            }
        }


        List<MatchPluginDataEntity> matchPluginDataList = new ArrayList<>();
        try {
            List<AppPlugin> appPluginList =  BlogApplication.pluginManager.getAllPlugins();
            for(AppPlugin plugin : appPluginList) {
                for(Method method : plugin.getClass().getMethods()) {
                    if (method.isAnnotationPresent(ReqFindPluginData.class)) {
                        ReqFindPluginData annotation = method.getAnnotation(ReqFindPluginData.class);
                        if(!this.checkPermission(annotation.permissions())) {
                            break;
                        }
                        URL handlePageUrl = new URL(pageURL);
                        if(Arrays.asList(annotation.value()).contains(handlePageUrl.getPath())) {
                            matchPluginDataList.add(new MatchPluginDataEntity(plugin.name, plugin.version, (String) method.invoke(plugin, handlePageUrl, requestEvent)));
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if(!matchPluginDataList.isEmpty()) {
            return new Result(ResultCode.SUCCESS, matchPluginDataList);
        } else {
            return new Result(ResultCode.OPERATIONFAIL, "没有匹配到合适的插件数据");
        }

    }

    @Override
    public Result findAllPluginInfo() {
        List<AppPlugin> appPluginList =  BlogApplication.pluginManager.getAllPlugins();
        if(!appPluginList.isEmpty()) {
            return new Result(ResultCode.SUCCESS, appPluginList);
        }
        return new Result(ResultCode.NOTFOUND, "没有安装插件");
    }

    @Override
    public Result getPluginConfigPage(String name) {
        List<AppPlugin> appPluginList =  BlogApplication.pluginManager.getAllPlugins();
        for(AppPlugin plugin : appPluginList) {
            if(name.equals(plugin.name)) {
                if(!plugin.pluginTools.fileExists("config.html")) {
                    plugin.pluginTools.jarResourceToPluginDirectory("config.html");
                }
                return new Result(ResultCode.SUCCESS, plugin.pluginTools.readResourcetoText("config.html"));
            }
        }
        return new Result(ResultCode.NOTFOUND, "没有配置页面");
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

}
