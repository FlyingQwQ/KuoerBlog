package top.kuoer.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PreloadWebClient {

    @Value("${preloadWeb}")
    private String preloadWeb;
    private ChromeDriver chromeDriver;
    private Map<String, String> replaceElementAttributeMap = new HashMap<>();


    public PreloadWebClient() {
        System.getProperties().setProperty("webdriver.chrome.driver", "chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu"); // 谷歌文档提到需要加上这个属性来规避bug
        options.addArguments("--disable-software-rasterizer"); //禁用3D软件光栅化器
        options.addArguments("blink-settings=imagesEnabled=false"); //禁止加图片,如果爬取图片的话,这个不能禁用
        options.addArguments("--disable-images");

        this.chromeDriver = new ChromeDriver(options);

        this.replaceElementAttributeMap.put("link", "href");
        this.replaceElementAttributeMap.put("img", "src");
        this.replaceElementAttributeMap.put("script", "src");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.chromeDriver.quit();
            System.out.println("已关闭Selenium");
        }));
    }

    public String preloadHtmlPage(String url) {
        this.chromeDriver.get(url);
        this.replaceHtmlSource(this.chromeDriver);
        return chromeDriver.getPageSource();
    }


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
            }
            httpClient.close();  // 关闭HTTP客户端
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public void replaceHtmlSource(ChromeDriver driver) {
        Set<String> replaceElementTagName = this.replaceElementAttributeMap.keySet();
        for(String tagName : replaceElementTagName) {
            String attributeName = this.replaceElementAttributeMap.get(tagName);

            List<WebElement> elements = chromeDriver.findElements(By.tagName(tagName));
            for(WebElement element : elements) {
                JavascriptExecutor js = (JavascriptExecutor) chromeDriver;
                String attributeValue = element.getAttribute(attributeName);
                if(!"".equals(attributeValue) && !"null".equals(attributeValue)) {
                    js.executeScript("arguments[0].setAttribute('" + attributeName + "', arguments[1]);", element, element.getAttribute(attributeName));
                }
            }

        }
    }

}
