package top.kuoer.service;

import top.kuoer.common.Result;
import top.kuoer.entity.FriendChain;

import java.util.List;

public interface FriendChainService {

    /**
     * 添加新的友链
     * @param friendChain 友链信息
     * @return 是否添加成功
     */
    Result addFriendChain(FriendChain friendChain);

    /**
     * 查询所有友链
     * @return 友链列表
     */
    Result findFriendChainAll();

    /**
     * 修改现有的友链
     * @return 影响数量
     */
    Result modifyFriendChain(int id, String title, String subTitle, String url, String icon);

    /**
     * 删除指定的友链
     * @param id 友链id
     * @return 影响数量
     */
    Result removeFriendChain(int id);

}
