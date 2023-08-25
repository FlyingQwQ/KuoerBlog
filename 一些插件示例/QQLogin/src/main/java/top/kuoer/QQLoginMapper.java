package top.kuoer;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kuoer.entity.User;

public interface QQLoginMapper {

    @Select("select * from admin where openid=#{openid}")
    User findUserInfoByQqOpenId(@Param("openid") String openid);

    @Update("update admin set openid=#{openid} where id=#{userid}")
    int setQqLoginOpenIdByUserId(@Param("userid") int userid, @Param("openid") String openid);

}
