package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.Comment;
import top.kuoer.entity.ReplyComment;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    @Select("select *, (select name from comment where id=outside.replyid) as recipient from comment outside where label=#{label}")
    List<Comment> findCommentByLabel(@Param("label") String label);

    @Insert("insert into comment (name, value, label, date) values (#{name}, #{value}, #{label}, #{date})")
    boolean addComment(Comment comment);

    @Insert("insert into comment (name, value, label, date, replyid) values (#{name}, #{value}, #{label}, #{date}, #{replyid})")
    boolean addReplyComment(ReplyComment replyComment);

    @Select("select * from comment where replyid = #{replyid}")
    List<ReplyComment> findCommentByReplyId(@Param("replyid") int replyid);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    boolean delComment(@Param("id") int id);
}
