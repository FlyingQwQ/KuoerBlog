package top.kuoer.plugin;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.yaml.snakeyaml.Yaml;
import top.kuoer.BlogApplication;
import top.kuoer.plugin.annotation.ActionHook;
import top.kuoer.plugin.annotation.Route;
import top.kuoer.plugin.event.ActionHookEvent;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginManager {
    private ApplicationContext context;
    public List<AppPlugin> plugins = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(PluginManager.class);
    private final ShiroFilterFactoryBean shiroFilterFactoryBean;



    public PluginManager(ApplicationContext context) {

        log.info("插件系统开始加载插件.");

        // 创建存放插件的目录
        File pluginDirectoryFile = new File("plugins");
        if(!pluginDirectoryFile.exists()) {
            boolean created = pluginDirectoryFile.mkdirs();
            if(!created) {
                log.error("插件主目录创建失败");
            }
        }

        this.context = context;
        this.shiroFilterFactoryBean = context.getBean(ShiroFilterFactoryBean.class);

        PluginTools.setPluginManager(this);

        this.loadPluginFiles();
        this.initPlugin();
    }

    private void loadPluginFiles() {
        File pluginDirectory = new File("plugins/");
        if(pluginDirectory.isDirectory()) {
            File[] jarFiles = pluginDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
            if(null != jarFiles) {
                for(File jarFile : jarFiles) {
                    if(jarFile.isFile()) {
                        this.registerPlugin(jarFile);
                    }
                }
            }
        }
    }

    private void initPlugin() {
        this.initPluginShiroFilterChain(plugins);
        for(AppPlugin plugin : plugins) {
            plugin.onEnable();
        }
    }

    @SuppressWarnings("unchecked")
    private void registerPlugin(File jarFile) {
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, this.getClass().getClassLoader());

            Yaml yaml = new Yaml();
            Object obj = yaml.load(classLoader.getResourceAsStream("plugin.yml"));
            if (obj instanceof Map) {
                Map<String, Object> pluginInfo = (Map<String, Object>) obj;

                if(pluginInfo.get("main").equals("")) {
                    log.error("[" + pluginInfo.get("name") +  "] 没有找到可运行的主类：main");
                    return;
                }

                Class<?> loadedClass = classLoader.loadClass((String) pluginInfo.get("main"));
                Constructor<?> constructor = loadedClass.getDeclaredConstructor();
                constructor.setAccessible(true);

                // 创建插件目录
                File pluginDirectory = new File("plugins/" + pluginInfo.get("name"));
                if(!pluginDirectory.exists()) {
                    boolean created = pluginDirectory.mkdirs();
                    if(!created) {
                        log.error("[" + pluginInfo.get("name") +  "] 目录创建失败！");
                    }
                }

                // 写入插件信息
                AppPlugin appPlugin = (AppPlugin) constructor.newInstance();
                appPlugin.name = (String) pluginInfo.get("name");
                appPlugin.version = (String) pluginInfo.get("version");
                appPlugin.mainClass = (String) pluginInfo.get("main");
                appPlugin.introduce = (String) pluginInfo.get("introduce");
                appPlugin.fileName = jarFile.getName();
                appPlugin.pluginDirectory = pluginDirectory;
                this.plugins.add(appPlugin);


                log.info("[" + appPlugin.name +  "] 当前版本：" + appPlugin.version);
            }
        } catch (MalformedURLException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplicationContext getContext() {
        return this.context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public List<AppPlugin> getAllPlugins() {
        return this.plugins;
    }

    /**
     * 监听平台自带的Controller接口
     * 添加动作钩子，有动作的时候就会通知所有加了 @ActionHook 注解的方法
     */
    public ActionHookEvent triggerActionHook(ProceedingJoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping requestMapping = signature.getMethod().getAnnotation(RequestMapping.class);

        ActionHookEvent newActionHookEvent = new ActionHookEvent();
        newActionHookEvent.setJoinPoint(joinPoint);
        List<AppPlugin> appPluginList = BlogApplication.pluginManager.getAllPlugins();
        for(AppPlugin plugin : appPluginList) {
            for(Method method : plugin.getClass().getMethods()) {
                if (method.isAnnotationPresent(ActionHook.class)) {
                    ActionHook actionHookAnnotation = method.getAnnotation(ActionHook.class);
                    if(hasCommonStrings(actionHookAnnotation.value(), requestMapping.path())) {
                        method.invoke(plugin, newActionHookEvent);
                        if(newActionHookEvent.isStopAction()) {
                            return newActionHookEvent;
                        }
                    }
                }
            }
        }

        return newActionHookEvent;
    }

    /**
     * 初始化检测插件的事件注解中有没有需要权限的，有的话添加进Shiro过滤链中
     */
    public void initPluginShiroFilterChain(List<AppPlugin> appPluginList) {
        log.info("正在添加插件事件注解中的URI到Shiro过滤链.");
        try {
            AbstractShiroFilter abstractShiroFilter = this.shiroFilterFactoryBean.getObject();
            if(null == abstractShiroFilter) {
                log.error("动态添加Shiro过滤链时出现异常，AbstractShiroFilter值为null");
                return;
            }
            PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
            DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager();

            Map<String, String> chains = new HashMap<>(shiroFilterFactoryBean.getFilterChainDefinitionMap());

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            defaultFilterChainManager.getFilterChains().clear();

            for(AppPlugin plugin : appPluginList) {
                for(Method method : plugin.getClass().getMethods()) {
                    if (method.isAnnotationPresent(Route.class)) {
                        Route annotation = method.getAnnotation(Route.class);
                        if(annotation.permissions().length > 0) {
                            log.info("[" + plugin.name +  "] 检测到'" + annotation.value() + "'需要权限，已添加到Shiro过滤链中.");
                            chains.put(annotation.value(), "jwt");
                        }
                    }
                }
            }

            for (Map.Entry<String, String> entry : chains.entrySet()) {
                defaultFilterChainManager.createChain(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            log.error("动态添加Shiro过滤链时出现异常.");
        }

    }



    /**
     * 检擦两个字符串数据有没有相同的字符串
     * @param array1 数组1
     * @param array2 数组2
     * @return 返回比对结果
     */
    private boolean hasCommonStrings(String[] array1, String[] array2) {
        for (String str1 : array1) {
            for (String str2 : array2) {
                if (str1.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

}
