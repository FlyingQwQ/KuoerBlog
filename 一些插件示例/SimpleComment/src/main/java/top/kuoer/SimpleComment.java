package top.kuoer;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.kuoer.plugin.AppPlugin;
import top.kuoer.plugin.annotation.ReqFindPluginData;
import top.kuoer.plugin.annotation.Route;
import top.kuoer.plugin.event.RequestEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class SimpleComment extends AppPlugin {

    private CommentService commentService;

    @Override
    public void onEnable() {
        if (!pluginTools.fileExists("SimpleComment.html")) {
            pluginTools.jarResourceToPluginDirectory("SimpleComment.html");
        }

        pluginTools.addMapper(CommentMapper.class);
        commentService = new CommentService(pluginTools.getMapper(CommentMapper.class));

        pluginTools.log("[SimpleComment] 简单评论区插件加载成功！");
    }

    @Override
    public void onDisable() {
        pluginTools.log("[SimpleComment] 简单评论区插件卸载成功！");
    }

    @ReqFindPluginData({"/pages/post.html"})
    public String postComment(URL pageURL, RequestEvent requestEvent) {
        return pluginTools.resFileTransTemplate(new String[]{"SimpleComment.html"});
    }

    @Route("/comment/findCommentByLabel")
    public void findCommentByLabel(RequestEvent requestEvent) {
        String label = requestEvent.getRequest().getParameter("label");
        this.sendJson(requestEvent.getResponse(), this.commentService.findCommentByLabel(label));
    }

    @Route("/comment/addComment")
    public void addComment(RequestEvent requestEvent) {
        HttpServletRequest request = requestEvent.getRequest();

        Comment comment = new Comment();
        comment.setName(request.getParameter("name"));
        comment.setValue(request.getParameter("value"));
        comment.setLabel(request.getParameter("label"));
        this.sendJson(requestEvent.getResponse(), this.commentService.addComment(comment));
    }

    @Route("/comment/addReplyComment")
    public void addReplyComment(RequestEvent requestEvent) {
        HttpServletRequest request = requestEvent.getRequest();

        ReplyComment replyComment = new ReplyComment();
        replyComment.setName(request.getParameter("name"));
        replyComment.setValue(request.getParameter("value"));
        replyComment.setLabel(request.getParameter("label"));
        replyComment.setReplyid(Integer.parseInt(request.getParameter("replyid")));
        this.sendJson(requestEvent.getResponse(), this.commentService.addReplyComment(replyComment));
    }

    @Route("/comment/delComment")
    public void delComment(RequestEvent requestEvent) {
        int id = Integer.parseInt(requestEvent.getRequest().getParameter("id"));
        this.sendJson(requestEvent.getResponse(), this.commentService.delComment(id));
    }

    public void sendJson(HttpServletResponse response, Object obj) {
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.getWriter().print(objectMapper.writeValueAsString(obj));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}