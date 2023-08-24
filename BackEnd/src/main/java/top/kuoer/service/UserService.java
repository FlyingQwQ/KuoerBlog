package top.kuoer.service;

import org.springframework.web.bind.annotation.RequestParam;
import top.kuoer.common.Result;
import top.kuoer.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 登录账号
      * @return 账号ID
     */
    User login(String userName, String password);

    /**
     * 验证Token有效性
     * @return 账号信息
     */
    Result verification(String token);

    /**
     * 添加新管理
     * @return 影响数量
     */
    Result add(String name, String password, int roleid);

    /**
     * 管理员列表
     * @return
     */
    List<User> userList();

    /**
     * 删除管理员
     * @param id 账号id
     * @return 影响数量
     */
    Result remove(int id);

    /**
     * 修改账号
     * @return 影响数量
     */
    Result modify(int userid, String password, int roleid);

    /**
     * 通过令牌获取该用户的所有信息
     * @param token
     * @return 用户信息
     */
    Result getUserInfo(String token);

}
