package top.kuoer.service;

import top.kuoer.common.Result;
import top.kuoer.entity.FriendChain;
import top.kuoer.entity.Posts;
import top.kuoer.entity.PostsInfo;
import top.kuoer.entity.PostsYear;

import java.util.List;
import java.util.Map;

/**
 * 帖子服务
 */
public interface PostsService {

    /**
     * 通过ID查询帖子
     * @param id 帖子id
     * @return 帖子实体
     */
    Result findPostsById(int id);

    /**
     * 查询所有帖子
     * @return 帖子列表
     */
    Result findPostsAll();

    /**
     * 根据标签查询帖子
     * @return 帖子列表
     */
    Result findPostsByLabel(int id);

    /**
     * 添加新帖子
     * @return 是否成功
     */
    Result addPosts(String title, String content, String labelName);

    /**
     * 修改现有的帖子
     * @return 影响数量
     */
    Result modifyPosts(int id, String title, String content, String labelName);

    /**
     * 删除帖子
     * @param id 帖子id
     * @return 影响数量
     */
    Result removePosts(int id);



}
