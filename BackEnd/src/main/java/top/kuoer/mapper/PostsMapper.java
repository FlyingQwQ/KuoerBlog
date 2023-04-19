package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.Posts;
import top.kuoer.entity.PostsInfo;
import top.kuoer.entity.PostsYear;

import java.util.List;

@Mapper
@Repository
public interface PostsMapper {

    @Select("select * from posts where id=#{id}")
    Posts findPostsById(@Param("id") int id);

    @Select("select *, strftime('%Y',datetime(date / 1000, 'unixepoch', 'localtime')) as year from posts")
    List<PostsInfo> findPostsAll();

    @Select("select *, strftime('%Y',datetime(date / 1000, 'unixepoch', 'localtime')) as year from posts where label=#{id}")
    List<PostsInfo> findPostsByLabel(int id);

    @Select("select distinct strftime('%Y',datetime(date / 1000, 'unixepoch', 'localtime')) as year from posts")
    List<PostsYear> findAllYear();

    @Insert("insert into posts (title, content, date, label) values (#{title}, #{content}, #{date}, #{label})")
    boolean addPosts(Posts posts);

    @Update("update posts set title=#{title}, content=#{content}, label=#{label} where id=#{id}")
    int modifyPosts(@Param("id") int id, @Param("title") String title, @Param("content") String content, @Param("label") int label);

    @Delete("delete from posts where id=#{id}")
    int removePosts(@Param("id") int id);

}
