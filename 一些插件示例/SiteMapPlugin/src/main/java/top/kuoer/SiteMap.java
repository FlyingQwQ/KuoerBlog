package top.kuoer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;
import top.kuoer.entity.PostsInfo;
import top.kuoer.mapper.PostsMapper;
import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.Route;
import top.kuoer.plugin.event.RequestEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteMap extends AppPlugin {

    private PostsMapper postsMapper;

    @Override
    public void onEnable() {

        this.postsMapper = pluginTools.getBean(PostsMapper.class);

        pluginTools.log("[SiteMap] 自动生成网站地图插件加载成功！");
    }

    @Override
    public void onDisable() {
        pluginTools.log("[SiteMap] 插件成功卸载");
    }

    @Route("/sitemap.txt")
    public void requestSiteMap(RequestEvent requestEvent) {
        String target = "Generate SiteMap Failed！";

        Yaml yaml = new Yaml();
        Object obj = yaml.load(pluginTools.readResourcetoText("config.yml"));
        if(obj instanceof Map) {
            Map<String, Object> siteMapConfig = (Map<String, Object>) obj;
            StringBuffer sb = new StringBuffer();

            List<PostsInfo> postsInfoList = postsMapper.findPostsAll();
            for(PostsInfo postsInfo : postsInfoList) {
                sb.append(siteMapConfig.get("frontendaddress") + "pages/post.html?id=" + postsInfo.getId() + "\r\n");
            }

            target = sb.toString();
        }

        try {
            requestEvent.getResponse().getWriter().print(target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Route("/sitemap/saveConfig")
    public void saveConfig(RequestEvent requestEvent) {
        String target = "no";

        String frontEndAddress = requestEvent.getRequest().getParameter("frontendaddress");
        if(null != frontEndAddress) {
            Map<String, Object> data = new HashMap<>();
            data.put("frontendaddress", frontEndAddress);

            Yaml yaml = new Yaml();
            pluginTools.writeResourcetoText("config.yml", yaml.dump(data));
            target = "yes";
        }

        try {
            requestEvent.getResponse().getWriter().print(target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Route("/sitemap/getPluginConfig")
    public void getPluginConfig(RequestEvent requestEvent) {
        String pluginConfig = pluginTools.readResourcetoText("config.yml");
        try {
            Yaml yaml = new Yaml();
            Object obj = yaml.load(pluginConfig);
            if(obj instanceof Map) {
                Map<String, Object> siteMapConfig = (Map<String, Object>) obj;
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = objectMapper.writeValueAsString(siteMapConfig);
                requestEvent.getResponse().getWriter().print(jsonString);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}