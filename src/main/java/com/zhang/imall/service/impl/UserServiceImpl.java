package com.zhang.imall.service.impl;

import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.dao.UserMapper;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.service.EmailService;
import com.zhang.imall.service.UserService;
import com.zhang.imall.util.EmailUtil;
import com.zhang.imall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
//与service层的接口保持一致
public class UserServiceImpl implements UserService {
    //注入mapper
    @Autowired
    private UserMapper userMapper;
    //发送邮件
    @Autowired
    private EmailService emailService;

    /**
     * 通过id主键查询 用户数据
     *
     * @return User
     */
    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    /**
     * 用户注册界面，不允许出现重复用户名
     *
     * @param username 用户名
     * @param password 密码 大于8位字符
     */
    @Override
    public void register(String username, String password) {
        //先在注册前进行查询，不允许出现重复用户名
        User result = userMapper.selectByUsername(username);
        if (result != null) {
            //抛出自定义的异常类,在构造函数处将自定义的枚举类型的数据进行初始化
            throw new ImallException(ImallExceptionEnum.NAME_EXISTED);
        }
        //写到数据库，新建一个User对象
        User user = new User();
        user.setUsername(username);
        //密码进行md5加密存储
        user.setPassword(MD5Utils.getMD5String(password));
        //选择性的插入数据
        int count = userMapper.insertSelective(user);
        if (count == 0) {//插入0条数据，插入失败，可能是其他问题
            throw new ImallException(ImallExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 登录信息的验证
     *
     * @param username 用户名
     * @param password 密码
     * @return User对象
     */
    @Override
    public User login(String username, String password) {

        User result = userMapper.selectByUsername(username);
        if (result == null) { //先查询用户名是否存在
            //抛出自定义的异常类,在构造函数处将自定义的枚举类型的数据进行初始化
            throw new ImallException(ImallExceptionEnum.NAME_NOT_EXISTED);
        }
        //将传递的密码进行md5的加盐校验
        String md5Password = MD5Utils.getMD5String(password);
        //数据库的查询
        User user = userMapper.loginCheck(username, md5Password);
        if (user == null) {//匹配密码
            throw new ImallException(ImallExceptionEnum.USER_MESSAGE_ERROR);
        }
        return user;
    }

    /**
     * 更新个性签名
     *
     * @param user 传递进来的当前登陆的用户信息
     */
    @Override
    public void updateInformation(User user) {
        int update = userMapper.updateByPrimaryKeySelective(user);
        if (update > 1) {
            //更新失败，抛出异常
            throw new ImallException(ImallExceptionEnum.UPDATE_FAILED);
        }

    }

    /**
     * 管理员登录验证
     * true是管理员，false是普通用户。
     *
     * @param user 传递进来的用户信息，封装的实体
     */
    @Override
    public boolean checkAdminRole(User user) {
        //1是普通用户，2是管理员
        return user.getRole().equals(2);
    }

    /**
     * 验证用户的邮箱地址的正确性
     * 发邮件
     *
     * @param email 邮箱
     */
    @Override
    public void sentSimpleEmailMassage(String email) {
        //进行邮箱校验，合法邮箱
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(email);
        if (!validEmailAddress) {
            throw new ImallException(ImallExceptionEnum.REQUEST_PARAM_ERROR, "非法邮箱");
        }
        //检查邮箱是否被注册
        User user = userMapper.selectOneByEmailAddress(email);
        if (user != null) {
            throw new ImallException(ImallExceptionEnum.NAME_EXISTED, "该邮箱已被注册");
        }
        //生成6位数字的验证码
        String verificationCode = emailService.createVerificationCode(6);
        //发送邮件
        emailService.sentSimpleEmailMassage(email, Constant.EMAIL_SUBJECT, "您的验证码为："+verificationCode+"。");
    }

    /**
     * 发送带freemarker的模板消息
     *
     * @param emailAddress 要发送的地址
     */
    @Override
    public void sentFreemarkerEmail(String emailAddress) {
        //生成6位数字的验证码
        String verificationCode = emailService.createVerificationCode(6);
        //发送邮件
        emailService.sentFreemarkerSimpleEmailMessage(emailAddress,Constant.EMAIL_SUBJECT,verificationCode);
    }
}
