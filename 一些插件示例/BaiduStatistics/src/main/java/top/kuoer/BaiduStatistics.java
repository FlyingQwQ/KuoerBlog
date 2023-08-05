package top.kuoer;

import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.ReqFindPluginData;
import top.kuoer.plugin.event.RequestEvent;

import java.net.URL;

public class BaiduStatistics extends AppPlugin {

    @Override
    public void onEnable() {
        if(!pluginTools.fileExists("statistics.html")) {
            pluginTools.jarResourceToPluginDirectory("statistics.html");
        }
        pluginTools.log("[BaiduStatistics] 百度统计插件加载成功！");
    }

    @ReqFindPluginData({"/", "/index.html", "/pages/friend_chain.html",  "/pages/post.html", "/pages/label.html"})
    public String onPageFindPluginEvent(URL pageURL, RequestEvent requestEvent) {
        return pluginTools.resFileTransTemplate(new String[]{"statistics.html"});
    }



}