package top.kuoer.service;

import top.kuoer.common.Result;
import top.kuoer.entity.Admin;
import top.kuoer.entity.QQLogin;

import java.util.List;

public interface AdminService {

    /**
     * 登录账号
      * @return 账号ID
     */
    Admin login(String userName, String password);

    /**
     * 验证Token有效性
     * @return 账号信息
     */
    Result verification(String token);

    /**
     * 添加新管理
     * @return 影响数量
     */
    Result addAdmin(String userName, String password);

    /**
     * 管理员列表
     * @return
     */
    List<Admin> adminList();

    /**
     * 删除管理员
     * @param id 账号id
     * @return 影响数量
     */
    Result removeAdmin(int id);

    /**
     * 修改账号
     * @return 影响数量
     */
    Result modifyAdmin(String name, String password);


    /**
     * 内部使用，不公开
     * @param token
     * @return
     */
    Admin _verification(String token);

    /**
     * 使用QQ登录
     * @param login
     * @return
     */
    String qqLogin(QQLogin login);

    /**
     * 绑定QQ登录
     */
    String bindQQ(String token, QQLogin login);

}
