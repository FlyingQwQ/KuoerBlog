package top.kuoer.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginTools {

    private static PluginManager pluginManager;
    private static final Logger log = LoggerFactory.getLogger(PluginTools.class);

    public static void log(String str) {
        log.info(str);
    }

    public static void error(String str) {
        log.error(str);
    }

    public static PluginManager getPluginManager() {
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
    public static String resFileTransTemplate(AppPlugin plugin, String[] templateFileNames) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String templateFileName : templateFileNames) {
            stringBuilder.append(readResourcetoText(plugin, templateFileName));
        }
        return stringBuilder.toString();
    }

    /**
     * 输出jar包里的资源文件到插件目录
     * @param resourceFile
     */
    public static boolean jarResourceToPluginDirectory(AppPlugin plugin, String resourceFile) {
        try {
            JarFile jarFile = new JarFile("plugins/" + plugin.fileName);
            JarEntry entry = jarFile.getJarEntry(resourceFile);
            if(null == entry) {
                log("[" + plugin.name +  "] 从jar包加载资源到目录，未找到" + resourceFile + ".");
                return false;
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

        return true;
    }

    public static String readResourcetoText(AppPlugin plugin, String resourceFile) {
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
     * @param plugin
     * @param resourceFile
     * @param text
     */
    public static void writeResourcetoText(AppPlugin plugin, String resourceFile, String text) {
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

    public static boolean fileExists(AppPlugin plugin, String resourceFile) {
        File file = new File(plugin.pluginDirectory + "/" + resourceFile);
        if(file.exists()) {
            return true;
        }
        return false;
    }



}
