package top.kuoer.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PluginTools {

    private static PluginManager pluginManager;
    private final Logger log = LoggerFactory.getLogger(PluginTools.class);
    private static SqlSession sqlSession;
    private final AppPlugin plugin;


    public PluginTools(AppPlugin appPlugin) {
        this.plugin = appPlugin;
    }

    public void log(String str) {
        log.info(str);
    }

    public void error(String str) {
        log.error(str);
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public static void setPluginManager(PluginManager pluginManager) {
        PluginTools.pluginManager = pluginManager;
    }

    /**
     * 通过模板文件合并获取html模板源代码
     * @param templateFileNames 模板文件名称列表
     * @return 合成后的模板文本
     */
    public String resFileTransTemplate(String[] templateFileNames) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String templateFileName : templateFileNames) {
            stringBuilder.append(readResourcetoText(templateFileName));
        }
        return stringBuilder.toString();
    }

    /**
     * 输出jar包里的资源文件到插件目录
     *
     * @param resourceFile 资源文件名
     */
    public void jarResourceToPluginDirectory(String resourceFile) {
        try {
            JarFile jarFile = new JarFile("plugins/" + plugin.fileName);
            JarEntry entry = jarFile.getJarEntry(resourceFile);
            if(null == entry) {
                log("[" + plugin.name +  "] 从jar包加载资源到目录，未找到" + resourceFile + ".");
                return;
            }
            if (!entry.isDirectory()) {
                InputStream inputStream = jarFile.getInputStream(entry);
                OutputStream outputStream = Files.newOutputStream(Paths.get(plugin.pluginDirectory + "/" + resourceFile));

                // 将输入流的内容输出到输出流
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // 关闭输入流和输出流
                outputStream.close();
                inputStream.close();
            }

            jarFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String readResourcetoText(String resourceFile) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(plugin.pluginDirectory + "/" + resourceFile);
            if(!file.exists()) {
                return "";
            }
            InputStream inputStream = Files.newInputStream(file.toPath());
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, bytesRead));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 写文本到文件，没有文件自动创建
     * @param resourceFile 资源文件名
     * @param text 需要保存的文本
     */
    public void writeResourcetoText(String resourceFile, String text) {
        try {
            File file = new File(plugin.pluginDirectory + "/" + resourceFile);
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            bufferedOutputStream.write(text.getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean fileExists(String resourceFile) {
        File file = new File(plugin.pluginDirectory + "/" + resourceFile);
        return file.exists();
    }

    /**
     * 通过ApplicationContext获取Bean
     * @param var1 类.class
     * @return 获取到的对象
     * @param <T> class
     */
    public <T> T getBean(Class<T> var1) {
        return pluginManager.getContext().getBean(var1);
    }

    /**
     * 向Mybatis中添加一个Mapper
     */
    public <T> void addMapper(Class<T> var1) {
        SqlSessionFactory session = this.getBean(SqlSessionFactory.class);
        session.getConfiguration().addMapper(var1);
    }

    /**
     * 从Mtbatis中获取Mapper
     */
    public <T> T getMapper(Class<T> var1) {
        SqlSessionFactory session = this.getBean(SqlSessionFactory.class);
        if(sqlSession == null) {
            sqlSession = session.openSession();
        }
        return sqlSession.getMapper(var1);
    }

    /**
     * 获取平台的所有Controller的RequestMappingInfo，不包括插件编写的Route
     */
    public List<RequestMappingInfo> getAllControllerRequestMappingInfo() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = this.getBean(RequestMappingHandlerMapping.class);
        Set<RequestMappingInfo> s = requestMappingHandlerMapping.getHandlerMethods().keySet();
        return new ArrayList<>(s);
    }

    public String jsonObjToStr(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public <T> T jsonStrToObj(String jsonText, Class<T> var1) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonText, var1);
        } catch (JsonProcessingException e) {
            return null;
        }
    }



}
