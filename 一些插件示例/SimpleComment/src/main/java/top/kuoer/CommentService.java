package top.kuoer;

import top.kuoer.common.Result;
import top.kuoer.enums.ResultCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentService {

    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public Result findCommentByLabel(String label) {
        List<Comment> commentList = this.commentMapper.findCommentByLabel(label);

        for(Comment comment : commentList) {
            comment.setReplyComments(new ArrayList<>());
        }

        for(Comment Xcomment : commentList) {
            for(Comment Ycomment : commentList) {
                if(Xcomment.getId() == Ycomment.getReplyid()) {
                    Xcomment.getReplyComments().add(Ycomment);
                }
            }
        }

        List<Comment> cloneCommentList = new ArrayList<>();
        for(Comment comment : commentList) {
            if(comment.getReplyid() == -1) {
                cloneCommentList.add(comment);
            }
        }

        return new Result(ResultCode.SUCCESS, cloneCommentList);
    }

    public Result addComment(Comment comment) {
        comment.setDate(System.currentTimeMillis());
        if(this.commentMapper.addComment(comment)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    public Result addReplyComment(ReplyComment replyComment) {
        replyComment.setDate(System.currentTimeMillis());
        if(this.commentMapper.addReplyComment(replyComment)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    public Result delComment(int id) {
        if(this.commentMapper.delComment(id)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

}
