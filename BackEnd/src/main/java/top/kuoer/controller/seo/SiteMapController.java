package top.kuoer.controller.seo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.entity.PostsInfo;
import top.kuoer.mapper.PostsMapper;

import java.util.List;

@RestController
public class SiteMapController {

    private final PostsMapper postsMapper;
    @Value("${front-end}")
    private String frontEndAddress;

    @Autowired
    public SiteMapController(PostsMapper postsMapper) {
        this.postsMapper = postsMapper;
    }

    @RequestMapping(path = "/sitemap.txt", method = RequestMethod.GET)
    public String getSiteMap() {
        StringBuffer sb = new StringBuffer();

        sb.append(frontEndAddress + "\r\n");

        List<PostsInfo> postsInfoList = postsMapper.findPostsAll();
        for(PostsInfo postsInfo : postsInfoList) {
            sb.append(frontEndAddress + "pages/post.html?id=" + postsInfo.getId() + "\r\n");
        }

        return sb.toString();
    }

}
