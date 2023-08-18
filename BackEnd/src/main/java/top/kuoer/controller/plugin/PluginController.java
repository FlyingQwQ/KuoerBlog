package top.kuoer.controller.plugin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.common.Result;
import top.kuoer.plugin.event.RequestEvent;
import top.kuoer.service.PluginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/plugin")
public class PluginController {

    private PluginService pluginService;

    @Autowired
    public PluginController(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    /**
     * 请求 /getplugindata 必须要带上完成的页面url
     * @param pageURL
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "/getplugindata", method = RequestMethod.GET)
    public Result pageFindPlugin(String pageURL, HttpServletRequest request, HttpServletResponse response) {
        return this.pluginService.pageFindPlugin(pageURL, new RequestEvent(request, response));
    }

    @RequestMapping(path = "/findallplugininfo", method = RequestMethod.GET)
    @RequiresPermissions("plugin:findallplugininfo")
    public Result findAllPluginInfo() {
        return this.pluginService.findAllPluginInfo();
    }

    @RequestMapping(path = "/getpluginconfigpage", method = RequestMethod.GET)
    public Result getPluginConfigPage(String name) {
        return this.pluginService.getPluginConfigPage(name);
    }

}
