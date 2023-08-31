package top.kuoer;

import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.ReqFindPluginData;
import top.kuoer.plugin.event.RequestEvent;

import java.net.URL;

public class DarkMode extends AppPlugin {

    @Override
    public void onEnable() {
        if (!pluginTools.fileExists("DarkMode.html")) {
            pluginTools.jarResourceToPluginDirectory("DarkMode.html");
        }
        pluginTools.log("[DarkMode] 夜间模式插件加载成功！");
    }

    @Override
    public void onDisable() {
        pluginTools.log("[DarkMode] 夜间模式插件卸载成功！");
    }

    @ReqFindPluginData(value = {
            "/", "/index.html", "/pages/post.html",
            "/pages/friend_chain.html", "/pages/label.html"
    })
    public String postComment(URL pageURL, RequestEvent requestEvent) {
        return pluginTools.resFileTransTemplate(new String[]{"DarkMode.html"});
    }

}