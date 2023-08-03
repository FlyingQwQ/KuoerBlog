package top.kuoer.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PluginManager {

    private ApplicationContext context;
    public List<AppPlugin> plugins = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(PluginManager.class);

    public PluginManager(ApplicationContext context) {
        // 创建存放插件的目录
        File pluginDirectoryFile = new File("plugins");
        if(!pluginDirectoryFile.exists()) {
            boolean created = pluginDirectoryFile.mkdirs();
            if(!created) {
                log.error("插件目录创建失败");
            }
        }

        this.context = context;

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
        for(AppPlugin plugin : plugins) {
            plugin.onEnable();
        }
    }


    private void registerPlugin(File jarFile) {
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});

            Yaml yaml = new Yaml();
            Object obj = yaml.load(classLoader.getResourceAsStream("plugin.yml"));
            if (obj instanceof Map) {
                Map<String, Object> pluginInfo = (Map<String, Object>) obj;

                if(pluginInfo.get("main").equals("")) {
                    return;
                }

                Class<?> loadedClass = classLoader.loadClass((String) pluginInfo.get("main"));

                Constructor<?> constructor = loadedClass.getDeclaredConstructor();
                constructor.setAccessible(true);

                // 创建插件目录
                File pluginDirectory = new File("plugins/" + (String) pluginInfo.get("name"));
                if(!pluginDirectory.exists()) {
                    boolean created = pluginDirectory.mkdirs();
                    if(!created) {
                        log.error((String) pluginInfo.get("name") + "目录创建失败！");
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


}
