package top.kuoer.controller.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kuoer.common.Result;
import top.kuoer.entity.Comment;
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
    public Result findCommentByLabel(Comment comment) {
        return commentService.addComment(comment);
    }

}
