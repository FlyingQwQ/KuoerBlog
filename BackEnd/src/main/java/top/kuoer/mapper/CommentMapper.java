package top.kuoer.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.Comment;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    @Select("select * from comment where label = #{label}")
    List<Comment> findCommentByLabel(@Param("label") String label);

    @Insert("insert into comment (name, value, label, date) values (#{name}, #{value}, #{label}, #{date})")
    boolean addComment(Comment comment);

}
