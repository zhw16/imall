package com.zhang.imall.controller;

import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.service.EmailService;
import com.zhang.imall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户的Controller类
 * 逻辑：controller->service(接口)->service实现方法->mapper
 */
@Controller
@RequestMapping("/user")
public class UserController {
    //注入UserService层的接口
    @Resource
    private UserService userService;

    //注入EmailService 进行验证码校验
    @Autowired
    private EmailService emailService;
    /**
     * 测试接口
     *
     * @return 返回用户的基本信息
     */
    @GetMapping("/user_test")
    @ResponseBody
    @ApiOperation("数据通路和流程的测试，get请求")
    public User personalPage() {
        User user = userService.getUser();
        return user;
    }


    /**
     * 用户登录界面
     *
     * @param username 用户名
     * @param password 密码
     * @param session  使用session保存用户登录信息
     * @return 状态码，和状态信息
     */
    @ApiOperation("登录接口")
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) throws ImallException {
        if (StringUtils.isEmpty(username)) {//用户名为空
            return ApiRestResponse.error(ImallExceptionEnum.NEED_USER_NAME);
        } else if (StringUtils.isEmpty(password)) {//密码不为空
            return ApiRestResponse.error(ImallExceptionEnum.NEED_USER_PASSWORD,"fhsiajhfkjkkjasjh.");
        }
        User user = userService.login(username, password);
        //返回用户信息时，不保存密码
        user.setPassword(null);
        //将用户信息，除了密码保存到session
        session.setAttribute(Constant.IMALL_USER, user);
        //返回的带上结果信息
        return ApiRestResponse.success(user);
    }

    /**
     * 更改当前登录的用户的签名信息
     *
     * @param session   当前登录的信息
     * @param signature 签名信息
     * @return
     */
    @ApiOperation("更新用户的签名信息")
    @PostMapping("/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam("signature") String signature) throws ImallException {
        //获取现在登录的用户
        User currentUser = (User) session.getAttribute(Constant.IMALL_USER);
        if (currentUser == null) {
            //需要登录
            return ApiRestResponse.error(ImallExceptionEnum.USER_NEED_LOGIN);
        } else {
            //新创建一个User，然后保留id，覆盖掉其他信息,因为mapper是使用User实体类进行数据的操作
            User user = new User();
            //放入id，只需要放入id就行，因为后面是传入参数就是User类型的参数，选择性的按照主键（id）查询数据
            user.setId(currentUser.getId());
            //更新签名，选择性的更新字段
            user.setPersonalizedSignature(signature);
            //放入临时新建的user，放入当前用户的id，后面查询是要传递User类型的参数的
            userService.updateInformation(user);
            return ApiRestResponse.success();
        }
    }

    /**
     * 登出此系统
     *
     * @param session 当前用户信息
     * @return 状态码, 不含数据
     */
    @PostMapping("/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        //设置的登录用户名
        session.removeAttribute(Constant.IMALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录系统
     * 判断role字段，为true是管理员，存储管理员信息
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) throws ImallException {
        if (StringUtils.isEmpty(username)) {//用户名为空
            return ApiRestResponse.error(ImallExceptionEnum.NEED_USER_NAME);
        } else if (StringUtils.isEmpty(password)) {//密码不为空
            return ApiRestResponse.error(ImallExceptionEnum.NEED_USER_PASSWORD);
        }
        //返回验证登录的用户的所有的信息
        User user = userService.login(username, password);
        //判断是不是管理员登录，返回true是管理员，返回false不是管理员
        if (userService.checkAdminRole(user)) {
            //返回用户信息时，不保存密码
            user.setPassword(null);
            //将用户信息，除了密码保存到session
            session.setAttribute(Constant.IMALL_USER, user);
            //返回的带上结果信息
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(ImallExceptionEnum.NEED_ADMIN_USER);
        }
    }

    /**
     * 用户注册界面
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回响应值和参数
     * @throws ImallException 自定义的异常
     */
    @ApiOperation("注册接口")
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("username") String username, @RequestParam("password") String password) throws ImallException {
        //if (username == null || password == null) //使用spring框架提供的String工具
        if (StringUtils.isEmpty(username)) {//用户名为空
            return ApiRestResponse.error(ImallExceptionEnum.NEED_USER_NAME);
        } else if (StringUtils.isEmpty(password)) {//密码不为空
            return ApiRestResponse.error(ImallExceptionEnum.NEED_USER_PASSWORD);
        } else if (password.length() < 8) {//密码大于8位
            return ApiRestResponse.error(ImallExceptionEnum.NEED_PASSWORD_LENGTH);
        } else {
            //注册成功，插入数据到数据库，进行数据的持久化
            userService.register(username, password);
            // 返回无参数的构造函数，传递定义好的OK_CODE和OK_MSG
            return ApiRestResponse.success();
        }
    }

    @ApiOperation("发送文字Email验证码")
    @PostMapping("/sentEmail")
    @ResponseBody
    public ApiRestResponse sentEmail(String emailAddress) {
        //发送邮件验证码
        userService.sentSimpleEmailMassage(emailAddress);
        return ApiRestResponse.success();
    }

    @ApiOperation("发送Freemarker模板Email的6位验证码")
    @PostMapping("/sentFreemarkerEmail")
    @ResponseBody
    public ApiRestResponse sentFreemarkerEmail(String emailAddress) {
        //发送邮件验证码
        userService.sentFreemarkerEmail(emailAddress);
        return ApiRestResponse.success();
    }


    /**
     * 发送验证码并将验证码记录在redis
     *
     * @param emailAddress email地址
     * @return
     */
    @ApiOperation("发送Freemarker模板Email的6位验证码,保存数据到redis")
    @PostMapping("/sentFreemarkerEmailAndSaveRedis")
    @ResponseBody
    public ApiRestResponse sentFreemarkerEmailAndSaveRedis(String emailAddress) {
        //发送邮件验证码
        userService.sentFreemarkerEmailAndSaveRedis(emailAddress);
        return ApiRestResponse.success();
    }



    /**
     * 验证码保存到redis,进行验证码和邮箱的校验
     * 验证Email和验证码的匹配程度
     * @param email
     * @param verificationCode
     * @return
     */
    @ApiOperation("校验邮箱和验证码")
    @PostMapping("/checkEmailAndVerificationCode")
    @ResponseBody
    public ApiRestResponse checkEmailAndVerificationCode(String email, String verificationCode) {
        Boolean checked = emailService.checkEmailAndVerificationCode(email, verificationCode);
        if (!checked) {
            return ApiRestResponse.error(ImallExceptionEnum.EMAIL_CHECKED_ERROR, "验证码错误，请检查后重试");
        }
        return ApiRestResponse.success();
    }



    /**
     * 用户登录界面,使用spring Session
     *
     * @param username 用户名
     * @param password 密码
     * @param session  使用session保存用户登录信息
     * @return 状态码，和状态信息
     */
    @ApiOperation("登录接口")
    @PostMapping("/springSessionLogin")
    @ResponseBody
    public ApiRestResponse springSessionLogin(@RequestParam("username") String username, @RequestParam("password") String password,HttpSession session)  {
        User user = userService.login(username, password);
        //返回用户信息时，不保存密码
        user.setPassword(null);
        //将用户信息，除了密码保存到session
        session.setAttribute(Constant.IMALL_USER, user);
        //过期时间60s
        session.setMaxInactiveInterval(60);
        //返回的带上结果信息
        return ApiRestResponse.success(user);
    }

    /**
     * 用户登录界面,使用cookie
     *
     * @param username 用户名
     * @param password 密码
     * @return 状态码，和状态信息
     */
    @ApiOperation("登录接口")
    @PostMapping("/springCookieLogin")
    @ResponseBody
    public ApiRestResponse springCookieLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response)  {
        User user = userService.login(username, password);
        //返回用户信息时，不保存密码
        user.setPassword(null);
        //将用户信息，除了密码保存到cookie
        Cookie cookie = new Cookie(Constant.IMALL_USER,user.getUsername());
        response.addCookie(cookie);
        //返回的带上结果信息
        return ApiRestResponse.success(user);
    }
}