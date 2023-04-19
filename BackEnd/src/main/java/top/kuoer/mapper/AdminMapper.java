package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.Admin;
import top.kuoer.entity.FriendChain;

import java.util.List;

@Mapper
@Repository
public interface AdminMapper {

    @Select("select id from admin where name=#{name} and password=#{password}")
    Admin findIdByNameAndPassword(@Param("name") String name, @Param("password") String password);

    @Update("update admin set token=#{token} where id=#{id}")
    Integer setUserToken(Admin admin);

    @Select("select id, name, token from admin where token=#{token}")
    Admin findIdByToken(@Param("token") String token);

    @Insert("insert into admin (name, password) values (#{name}, #{password})")
    Integer addAdmin(@Param("name") String name, @Param("password") String password);

    @Select("select id from admin where name=#{name}")
    Integer checkRepeat(@Param("name") String name);

    @Select("select * from admin")
    List<Admin> adminList();

    @Delete("delete from admin where id=#{id}")
    Integer removeAdmin(@Param("id") int id);

    @Update("update admin set password=#{password} where name=#{name}")
    int modifyAdmin(@Param("name") String name, @Param("password") String password);

    @Select("select * from admin where openid=#{openid}")
    Admin findUserInfoByQqOpenId(@Param("openid") String openid);

    @Update("update admin set openid=#{openid} where token=#{token}")
    int setQqLoginOpenIdByToken(@Param("token") String token, @Param("openid") String openid);
}
