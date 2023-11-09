package top.kuoer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;
import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.ReqFindPluginData;
import top.kuoer.plugin.annotation.Route;
import top.kuoer.plugin.event.RequestEvent;
import top.kuoer.service.UserService;
import top.kuoer.shiro.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QQLoginMain extends AppPlugin {

    private QQLoginService qqLoginService;

    @Override
    public void onEnable() {
        initFile();

        pluginTools.addMapper(QQLoginMapper.class);

        this.qqLoginService = new QQLoginService(pluginTools.getMapper(QQLoginMapper.class), pluginTools.getBean(UserService.class));

        pluginTools.log("[QQLogin] 后台QQ登录插件加载成功！");
    }

    public void initFile() {
        if(!pluginTools.fileExists("loginpage.html")) {
            pluginTools.jarResourceToPluginDirectory("loginpage.html");
        }
        if(!pluginTools.fileExists("manage.html")) {
            pluginTools.jarResourceToPluginDirectory("manage.html");
        }
    }

    @Override
    public void onDisable() {
        pluginTools.log("[QQLogin] 后台QQ登录插件卸载成功！");
    }

    @ReqFindPluginData("/pages/admin/login.html")
    public String openLoginPageEvent(URL pageURL, RequestEvent requestEvent) {
        return pluginTools.resFileTransTemplate(new String[]{"loginpage.html"});
    }

    @ReqFindPluginData("/pages/admin/manage.html")
    public String openManagePageEvent(URL pageURL, RequestEvent requestEvent) {
        return pluginTools.resFileTransTemplate(new String[]{"manage.html"});
    }

    @Route("/admin/qqlogin")
    public void qqLogin(RequestEvent requestEvent) throws IOException {
        HttpServletRequest request = requestEvent.getRequest();

        String token = this.qqLoginService.qqLogin(getQQLoginData(request));
        requestEvent.getResponse().sendRedirect(fileToYML("config.yml").get("frontendaddress") + "pages/admin/login.html?token=" + token);  // 后面的地址是登录成功后回调的地址
    }

    @Route(value = "/admin/bindqq", permissions = {"qqlogin:bindqq"})
    public void bindQQ(RequestEvent requestEvent) throws IOException {
        HttpServletRequest request = requestEvent.getRequest();

        String token = request.getParameter("token");
        int userid = Integer.parseInt(Objects.requireNonNull(JwtUtil.getInfo(token, "id")));

        String result = this.qqLoginService.bindQQ(userid, getQQLoginData(request));
        requestEvent.getResponse().sendRedirect(fileToYML("config.yml").get("frontendaddress") + "pages/admin/manage.html?result=" + result);   // 后面的地址是绑定成功后回调的地址
    }

    @Route(value = "/qqlogin/saveConfig", permissions = {"qqlogin:saveconfig"})
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

    @Route("/qqlogin/getPluginConfig")
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

    public QQLogin getQQLoginData(HttpServletRequest request) {
        QQLogin login = new QQLogin();
        login.setCode(request.getParameter("code"));
        login.setMsg(request.getParameter("msg"));
        login.setMethod(request.getParameter("method"));
        login.setOpenid(request.getParameter("openid"));
        login.setName(request.getParameter("name"));
        login.setAvatar(request.getParameter("avatar"));
        login.setMd5(request.getParameter("md5"));
        return login;
    }

    public Map<String, Object> fileToYML(String fileName) {
        Yaml yaml = new Yaml();
        Object obj = yaml.load(pluginTools.readResourcetoText(fileName));
        if(obj instanceof Map) {
            return (Map<String, Object>) obj;

        }
        return null;
    }

}