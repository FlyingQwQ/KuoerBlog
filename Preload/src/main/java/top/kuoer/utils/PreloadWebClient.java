package top.kuoer.utils;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

@Component
public class PreloadWebClient {

    @Value("${preloadWeb}")
    private String preloadWeb;
    private ChromeDriver chromeDriver;


    public PreloadWebClient() {
        System.getProperties().setProperty("webdriver.chrome.driver", "chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu"); // 谷歌文档提到需要加上这个属性来规避bug
        options.addArguments("--disable-software-rasterizer"); //禁用3D软件光栅化器
        options.addArguments("blink-settings=imagesEnabled=false"); //禁止加图片,如果爬取图片的话,这个不能禁用
        options.addArguments("--disable-images");

        this.chromeDriver = new ChromeDriver(options);
    }

    public String preloadHtmlPage(String url) {
        chromeDriver.get(url);
        return chromeDriver.getPageSource();
    }

//    public String preloadHtmlPage(String url) {
//        try {
//            WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
//            webClient.getOptions().setUseInsecureSSL(true);
//            webClient.getOptions().setJavaScriptEnabled(true);
//            webClient.getOptions().setCssEnabled(false);
//            webClient.getOptions().setThrowExceptionOnScriptError(false);
//            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//
//            WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);
//            request.setCharset(Charset.forName("UTF-8"));
//            HtmlPage page = webClient.getPage(request);
//            while(!"complete".equalsIgnoreCase(page.getReadyState())) {
//
//            }
//            webClient.waitForBackgroundJavaScriptStartingBefore(1000);
//
//            this.replaceHtmlSource(page);
//
//
//            webClient.close();
//            return page.asXml();
//        } catch (ScriptException e) {
//            System.out.println(e.getMessage());
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return "";
//    }


    public String getHttpResource(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();  // 获取服务器响应码
            if (statusCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {  // 逐行读取服务器响应数据
                    stringBuffer.append(line + "\n");
                }
                reader.close();
                return stringBuffer.toString();
            } else {
                System.out.println("服务器返回非正常状态： " + statusCode);
            }
            httpClient.close();  // 关闭HTTP客户端
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public void replaceHtmlSource(HtmlPage page) {
        DomNodeList<DomElement> linkElements = page.getElementsByTagName("link");
        for(DomElement linkElement : linkElements) {
            linkElement.setAttribute("href", linkElement.getAttribute("href").replaceFirst("^\\.", preloadWeb + "/"));
        }

        DomNodeList<DomElement> scriptElements = page.getElementsByTagName("script");
        for(DomElement scriptElement : scriptElements) {
            if(!scriptElement.getAttribute("src").equals("")) {
                scriptElement.setAttribute("src", scriptElement.getAttribute("src").replaceFirst("^\\.", preloadWeb + "/"));
            }
        }

        DomNodeList<DomElement> imgElements = page.getElementsByTagName("img");
        for(DomElement imgElement : imgElements) {
            imgElement.setAttribute("src", imgElement.getAttribute("src").replaceFirst("^\\.", preloadWeb + "/"));
        }


    }

}
