package top.kuoer.service;

import top.kuoer.common.Result;
import top.kuoer.entity.Comment;
import top.kuoer.entity.ReplyComment;

import java.util.List;

public interface CommentService {

    /**
     * 根据标签查询指定的评论
     * @param label 标签
     * @return 评论列表
     */
    Result findCommentByLabel(String label);

    /**
     * 添加新评论
     * @param comment 评论信息
     * @return 是否成功
     */
    Result addComment(Comment comment);

    /**
     * 添加回复评论
     * @param replyComment 回复评论信息
     * @return 是否成功
     */
    Result addReplyComment(ReplyComment replyComment);

}
