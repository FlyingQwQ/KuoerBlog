package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.User;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    @Select("select id from admin where name=#{name} and password=#{password}")
    Integer findIdByNameAndPassword(@Param("name") String name, @Param("password") String password);

    @Select("select * from admin where id=#{id}")
    User getUserInfoById(@Param("id") int id);

    @Insert("insert into admin (name, password) values (#{name}, #{password})")
    Integer addAdmin(@Param("name") String name, @Param("password") String password);

    @Select("select id from admin where name=#{name}")
    Integer checkRepeat(@Param("name") String name);

    @Select("select * from admin")
    List<User> adminList();

    @Delete("delete from admin where id=#{id}")
    Integer removeAdmin(@Param("id") int id);

    @Update("update admin set password=#{password} where id=#{userid}")
    int modifyUser(@Param("userid") int userid, @Param("password") String password);

}
