package top.kuoer.plugin;

import top.kuoer.plugin.annotation.ReqFindPluginData;
import top.kuoer.plugin.annotation.Route;
import top.kuoer.plugin.event.RequestEvent;

import java.io.*;
import java.net.URL;

/**
 * 开发插件的时候需要继承这个类
 */
public class AppPlugin {

    public String name = "";
    public String version = "";
    public String mainClass = "";
    public String fileName = "";
    public String introduce = "";
    public File pluginDirectory;

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



}
