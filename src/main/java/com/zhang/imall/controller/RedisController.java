package com.zhang.imall.controller;

import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis保存用户的验证码
 * @ClassName RedisController
 * @Author ZhangHewen
 * @Description
 * @Date 2023/6/13 17:48
 * @Version V1.0
 **/
@RestController
public class RedisController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码并将验证码记录在redis
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

}
