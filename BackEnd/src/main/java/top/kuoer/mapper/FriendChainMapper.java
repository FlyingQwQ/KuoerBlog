package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kuoer.entity.FriendChain;

import java.util.List;

@Mapper
@Repository
public interface FriendChainMapper {

    @Select("select * from friend_chain")
    List<FriendChain> findFriendChainAll();

    @Update("update friend_chain set title=#{title}, subtitle=#{subtitle}, url=#{url}, icon=#{icon} where id=#{id}")
    int modifyFriendChain(@Param("id") int id, @Param("title") String title, @Param("subtitle") String subtitle, @Param("url") String url, @Param("icon") String icon);

    @Insert("insert into friend_chain (title, subtitle, url, icon) values (#{title}, #{subtitle}, #{url}, #{icon})")
    boolean addFriendChain(FriendChain friendChain);

    @Delete("delete from friend_chain where id=#{id}")
    int removeFriendChain(@Param("id") int id);

}
