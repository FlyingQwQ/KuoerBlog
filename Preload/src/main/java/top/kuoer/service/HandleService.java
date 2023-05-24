package top.kuoer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.kuoer.utils.PreloadWebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@Service
public class HandleService {

    @Value("${preloadWeb}")
    private String preloadWeb;
    private PreloadWebClient preloadWebClient;
    private Logger logger = Logger.getLogger("Request Record");

    @Autowired
    public HandleService(PreloadWebClient preloadWebClient) {
        this.preloadWebClient = preloadWebClient;
    }

    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();  // 获取请求路径
        String query = request.getQueryString();  // 获取请求参数

        path = this.handleRedirect(path);

        String url = this.preloadWeb + path + (query == null ? "" : "?" + query);
        String fileType = getURIFileType(path);

        if(path.startsWith("redirect:")) {
            url = path.substring(path.indexOf("redirect:") + "redirect:".length());
        }

        String htmlSource = this.preloadWebClient.getHttpResource(url);
        if(!"".equals(htmlSource)) {
            if(fileType.equals("html")) {
                // html文件开头添加 #Don't Preload 则表示，不使用后端渲染
                if(!htmlSource.startsWith("#Don't Preload")) {
                    htmlSource = this.preloadWebClient.preloadHtmlPage(url);
                }
                htmlSource = htmlSource.replace("#Don't Preload", "");
            }
        } else {
            htmlSource = this.preloadWebClient.preloadHtmlPage(this.preloadWeb + "/pages/error/404.html");
            // 打印日志
            this.logger.info("404：" + path + (query == null ? "" : "?" + query));
        }
        return htmlSource;
    }

    /**
     * 获取uri中的资源类型
     * @param uri
     * @return 资源类型
     */
    public String getURIFileType(String uri) {
        String fileType = uri.substring(uri.lastIndexOf(".") + 1);
        if(!fileType.equals("")) {
            return fileType;
        }
        return "";
    }

    /**
     * 处理资源位置的重定向
     * @param resourceLocation 资源位置
     * @return 重定向之后的位置
     */
    public String handleRedirect(String resourceLocation) {
        String target;
        switch(resourceLocation) {
            case "/":
                target = "/index.html";
                break;
            case "/sitemap.txt":
                target = "redirect:https://blogapi.kuoer.top:2333/sitemap.txt";
                break;
            default:
                target = resourceLocation;
                break;
        }
        return target;
    }

}
