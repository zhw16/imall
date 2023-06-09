package com.zhang.imall.service;


import java.util.Map;

/**
 * 邮件
 */
public interface EmailService {
    /**
     * 发送文本邮件
     * @param to 发送给
     * @param subject 主题
     * @param text 正文
     */
    void sentSimpleEmailMassage(String to, String subject, String text);


    /**
     *
     * @param to 收件人
     * @param subject 主题
     */
    void sentFreemarkerSimpleEmailMessage(String to, String subject,String verificationCode);

    /**
     * 生成指定位数的数字验证码
     * @param num 指定生成位数
     * @return
     */
    String createVerificationCode(int num);

    /**
     * 保存邮箱对应的验证码到redis
     * @param email  邮箱
     * @param verificationCode 验证码
     * @return
     */
    Boolean saveEmailToRedis(String email, String verificationCode);

    /**
     * 校验邮箱地址和验证码的匹配
     * @param email
     * @param verificationCode
     * @return
     */
    Boolean checkEmailAndVerificationCode(String email, String verificationCode);
}
