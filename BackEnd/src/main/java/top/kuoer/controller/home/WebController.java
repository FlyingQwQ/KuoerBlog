package top.kuoer.controller.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.entity.WebConfigEntity;

@RestController
@RequestMapping(path="/web")
public class WebController {

    private WebConfigEntity webConfigEntity;

    @Autowired
    public WebController(WebConfigEntity webConfigEntity) {
        this.webConfigEntity = webConfigEntity;
    }

    @RequestMapping("cx")
    public void say() {
        System.out.println(webConfigEntity.getFrontEnd());
        System.out.println(webConfigEntity.getFilingNumber());
        webConfigEntity.setFilingNumber("2333");
    }

}
