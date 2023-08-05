package top.kuoer.plugin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import top.kuoer.plugin.annotation.ActionHook;
import top.kuoer.plugin.event.ActionHookEvent;

import java.io.*;

/**
 * 开发插件的时候需要继承这个类
 */
public class AppPlugin {

    public String name = "";
    public String version = "";
    public String mainClass = "";
    public String fileName = "";
    public String introduce = "";
    public File pluginDirectory = null;
    @JsonIgnore
    public PluginTools pluginTools = new PluginTools(this);

    /**
     * 插件卸载事件
     */
    public void onDisable() {

    }

    /**
     * 插件加载完成事件
     */
    public void onEnable() {

    }

    /**
     * 用户请求页面获取该插件的插件数据和页面模板
     * @param pageURL 页面URL
     * @param requestEvent ServletAPI
     * @return 页面模板
     */
//    @ReqFindPluginData({"/pages/post.html"})
//    public String onPageFindPluginEvent(URL pageURL, RequestEvent requestEvent) {
//        return "";
//    }

    /**
     * 自定义插件API接口，可以实现自己想要的后端功能
     * @param requestEvent ServletAPI
     */
//    @Route("/test")
//    public void requestHandle(RequestEvent requestEvent) {
//
//    }


    /**
     * 如果添加了@ActionHook接收不到事件，有可能被其他的插件使用该url的事件
     * stopAction设置为true可以拦截事件，排在本插件后面的插件则接收不到事件
     */
    @ActionHook("/hook")
    public void actionHook(ActionHookEvent actionHookEvent) {

    }


}
