package top.kuoer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kuoer.common.Result;
import top.kuoer.entity.Comment;
import top.kuoer.enums.ResultCode;
import top.kuoer.mapper.CommentMapper;
import top.kuoer.service.CommentService;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public Result findCommentByLabel(String label) {
        return new Result(ResultCode.SUCCESS, this.commentMapper.findCommentByLabel(label));
    }

    @Override
    public Result addComment(Comment comment) {
        comment.setDate(System.currentTimeMillis());
        if(this.commentMapper.addComment(comment)) {
            return new Result(ResultCode.SUCCESS, null);
        }
        return new Result(ResultCode.OPERATIONFAIL, "请检查数据库是否有问题");
    }

}
