package top.kuoer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.kuoer.utils.PreloadWebClient;

import javax.servlet.http.HttpServletRequest;
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

    public String handleRequest(HttpServletRequest request) {
        String path = request.getRequestURI();  // 获取请求路径
        String query = request.getQueryString();  // 获取请求参数

        if(path.equals("/")) {
            path = "/index.html";
        }

        String url = this.preloadWeb + path + (query == null ? "" : "?" + query);
        String fileType = getURIFileType(path);

        this.logger.info(url);

        if(fileType.equals("html")) {
            String htmlSource = this.preloadWebClient.getHttpResource(url);
            if(!htmlSource.startsWith("#Don't Preload")) {
                return this.preloadWebClient.preloadHtmlPage(url);
            }
            return htmlSource.replace("#Don't Preload", "");
        }
        return this.preloadWebClient.getHttpResource(url);
    }

    public String getURIFileType(String uri) {
        String fileType = uri.substring(uri.lastIndexOf(".") + 1);
        if(!fileType.equals("")) {
            return fileType;
        }
        return "";
    }

}
