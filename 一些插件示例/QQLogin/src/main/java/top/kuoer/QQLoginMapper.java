package top.kuoer;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.kuoer.entity.Admin;

public interface QQLoginMapper {

    @Select("select * from admin where openid=#{openid}")
    Admin findUserInfoByQqOpenId(@Param("openid") String openid);

    @Update("update admin set openid=#{openid} where token=#{token}")
    int setQqLoginOpenIdByToken(@Param("token") String token, @Param("openid") String openid);

}
