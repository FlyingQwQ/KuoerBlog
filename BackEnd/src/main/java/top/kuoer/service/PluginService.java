package top.kuoer.service;

import top.kuoer.common.Result;
import top.kuoer.plugin.event.RequestEvent;

public interface PluginService {

    /**
     * 用户访问页面的时候，会根据URI获取相干插件的数据和页面模板
     * @param pageURL
     * @return
     */
    Result pageFindPlugin(String pageURL, RequestEvent requestEvent);

    /**
     * 获取所有的插件信息
     * @return
     */
    Result findAllPluginInfo();

    /**
     * 获取插件配置页面
     * @param name
     * @return
     */
    Result getPluginConfigPage(String name);

}
