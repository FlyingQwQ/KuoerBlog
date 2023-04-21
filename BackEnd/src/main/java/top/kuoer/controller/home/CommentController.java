package top.kuoer.controller.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.annotations.Authentication;
import top.kuoer.common.Result;
import top.kuoer.entity.Comment;
import top.kuoer.entity.ReplyComment;
import top.kuoer.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(path = "/findCommentByLabel", method = RequestMethod.GET)
    public Result findCommentByLabel(@RequestParam("label") String label) {
        return commentService.findCommentByLabel(label);
    }

    @RequestMapping(path = "/addComment", method = RequestMethod.GET)
    public Result addComment(Comment comment) {
        return commentService.addComment(comment);
    }

    @RequestMapping(path = "/addReplyComment", method = RequestMethod.GET)
    public Result addReplyComment(ReplyComment replyComment) {
        return commentService.addReplyComment(replyComment);
    }

    @RequestMapping(path = "/delComment", method = RequestMethod.GET)
    @Authentication
    public Result delComment(@RequestParam("token") String token,
                             @RequestParam("id") int id) {
        return commentService.delComment(id);
    }
}
