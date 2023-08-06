package top.kuoer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.PluginManager;

import java.util.List;

@SpringBootApplication
public class BlogApplication implements ApplicationListener<ApplicationEvent> {

    public static PluginManager pluginManager;

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ApplicationReadyEvent){
            pluginManager = new PluginManager(((ApplicationReadyEvent) event).getApplicationContext());
        } else if(event instanceof ContextClosedEvent) {
            List<AppPlugin> appPluginList =  BlogApplication.pluginManager.getAllPlugins();
            for(AppPlugin plugin : appPluginList) {
                plugin.onDisable();
            }
        }

    }

}
