package com.zhang.imall.service;

import com.zhang.imall.model.pojo.User;

import java.util.Collection;
import java.util.List;


/**
 * service的接口层,在接口实现层 加上@Service（接口名称）
 */

public interface UserService {
    /**
     * 测试用户的方法接口
     *
     * @return User实体类
     */
    public User getUser();

    /**
     * 用户注册的用户名，密码
     */
    public void register(String username, String password);

    /**
     * 用户的登录、校验、和session的保存
     */
    public User login(String username, String password);

    /***
     * 更新用户的签名信息,
     * 参数：当前登录的用户信息
     */
    public void updateInformation(User user);

    /**
     * 管理员登录验证
     */
    public boolean checkAdminRole(User user);

    /**
     * 用户注册验证码
     */
    public void sentSimpleEmailMassage(String email);

    /**
     * 发送带freemarker的模板消息
     * @param emailAddress 要发送的地址
     */
    void sentFreemarkerEmail(String emailAddress);

    List<User> selectAllUser();
}
