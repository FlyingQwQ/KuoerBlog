package top.kuoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuoer.common.Result;
import top.kuoer.entity.Comment;
import top.kuoer.entity.CommentResult;
import top.kuoer.entity.ReplyComment;
import top.kuoer.enums.ResultCode;
import top.kuoer.mapper.CommentMapper;
import top.kuoer.service.CommentService;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public Result findCommentByLabel(String label) {

        List<Comment> commentList = this.commentMapper.findCommentByLabel(label);
        Collections.reverse(commentList);

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

    @Override
    public Result addComment(Comment comment) {
        comment.setDate(System.currentTimeMillis());
        if(this.commentMapper.addComment(comment)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    @Override
    public Result addReplyComment(ReplyComment replyComment) {
        replyComment.setDate(System.currentTimeMillis());
        if(this.commentMapper.addReplyComment(replyComment)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

    @Override
    public Result delComment(int id) {
        if(this.commentMapper.delComment(id)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

}
