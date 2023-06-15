package com.zhang.imall.service.impl;

import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.service.EmailService;
import freemarker.template.Template;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName EmailServiceImpl
 * @Author ZhangHewen
 * @Description
 * @Date 2023/5/30 13:16
 * @Version V1.0
 **/
@Service
public class EmailServiceImpl implements EmailService {
    //发送邮件
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    /**
     * 发送文本邮件
     *
     * @param to      发送给
     * @param subject 主题
     * @param text    正文
     */
    @Override
    public void sentSimpleEmailMassage(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //设置发件人
        simpleMailMessage.setFrom(Constant.EMAIL_FORM);
        //设置发送给谁
        simpleMailMessage.setTo(to);
        //设置主题
        simpleMailMessage.setSubject(subject);
        //设置正文
        simpleMailMessage.setText(text);
        //发送邮件
        javaMailSender.send(simpleMailMessage);
    }

    /**
     * 发送模板邮件
     */
    @Override
    public void sentFreemarkerSimpleEmailMessage(String to, String subject, String verificationCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 设置发件人、收件人、主题和内容
            helper.setFrom(Constant.EMAIL_FORM);
            helper.setTo(to);
            helper.setSubject(subject);
            // 生成模板内容
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("verificationCode.ftl");
            Map<String, Object> model = new HashMap<>();
            model.put("verificationCode", verificationCode);
            model.put("name", to);
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(content, true);
            // 发送邮件
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new ImallException(ImallExceptionEnum.EMAIL_SENT_ERROR, "邮件发送失败");
        }

    }

    /**
     * 生成指定位数的数字验证码
     *
     * @param num 指定生成位数
     * @return
     */
    @Override
    public String createVerificationCode(int num) {
        return String.format("%0" + num + "d", (int) (Math.random() * Math.pow(10, num)));
    }

    /**
     * 保存用户验证码到redis
     *
     * @param email            邮箱
     * @param verificationCode 验证码
     * @return 保存结果
     */
    @Override
    public Boolean saveEmailToRedis(String email, String verificationCode) {
        //创建redissonConfig,主机、端口、密码
        Config redissonConfig = new Config();
        redissonConfig.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort).setPassword(redisPassword);
        //创建 RedissonClient 对象，这是 Redisson 客户端库的入口点。
        RedissonClient redissonClient = Redisson.create(redissonConfig);
        //通过 RedissonClient 对象获取一个Bucket（键值对），这里使用用户的 email 作为键。
        RBucket<String> bucket = redissonClient.getBucket(email);
        //检查 Bucket 是否存在，即检查 Redis 中是否已经存在对应的键。
        boolean exists = bucket.isExists();
        if (!exists) {//不存在，使用用户的 email 作为键，存入中验证码，设置过期时间是60秒
            bucket.set(verificationCode, 60, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    /**
     * 校验邮箱地址和验证码的匹配
     *
     * @param email
     * @param verificationCode
     * @return
     */
    @Override
    public Boolean checkEmailAndVerificationCode(String email, String verificationCode) {
        //创建redissonConfig,主机、端口、密码
        Config redissonConfig = new Config();
        redissonConfig.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort).setPassword(redisPassword);
        //创建 RedissonClient 对象，这是 Redisson 客户端库的入口点。
        RedissonClient redissonClient = Redisson.create(redissonConfig);
        //通过 RedissonClient 对象获取一个Bucket（键值对），这里使用用户的 email 作为键。
        RBucket<String> bucket = redissonClient.getBucket(email);
        //检查 Bucket 是否存在，即检查 Redis 中是否已经存在对应的键。
        boolean exists = bucket.isExists();
        if (exists) {//不存在，使用用户的 email 作为键，存入中验证码，设置过期时间是60秒
            String redisVerificationCode = bucket.get();
            if (verificationCode.equals(redisVerificationCode)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        EmailServiceImpl emailService = new EmailServiceImpl();
        for (int i = 0; i < 100; i++) {
            String verificationCode = emailService.createVerificationCode(6);
            System.out.println(verificationCode);
        }
    }
}
