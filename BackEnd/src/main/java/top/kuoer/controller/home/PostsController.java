package top.kuoer.controller.home;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.common.Result;
import top.kuoer.service.PostsService;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path="/home")
public class PostsController {

    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @RequestMapping(path="/findPostsById", method = RequestMethod.GET)
    public Result findPostsById(@RequestParam("id") int id, HttpServletRequest request) {
        return postsService.findPostsById(id, request);
    }

    @RequestMapping(path="/findPostsAll", method = RequestMethod.GET)
    public Result findPostsAll() {
        return postsService.findPostsAll();
    }

    @RequestMapping(path="/addPosts", method = RequestMethod.POST)
    @RequiresPermissions("home:addPosts")
    public Result addPosts(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("labelName") String labelName) {
        return postsService.addPosts(title, content, labelName);
    }

    @RequestMapping(path="/modifyPosts", method = RequestMethod.POST)
    @RequiresPermissions("home:modifyPosts")
    public Result modifyPosts(@RequestParam("id") int id,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("labelName") String labelName) {
        return postsService.modifyPosts(id, title, content, labelName);
    }

    @RequestMapping(path="/removePosts", method = RequestMethod.GET)
    @RequiresPermissions("home:removePosts")
    public Result removePosts(@RequestParam("id") int id) {
        return postsService.removePosts(id);
    }

    @RequestMapping(path="/findPostsByLabel", method = RequestMethod.GET)
    public Result findPostsByLabel(@RequestParam("id") int id) {
        return postsService.findPostsByLabel(id);
    }

}
