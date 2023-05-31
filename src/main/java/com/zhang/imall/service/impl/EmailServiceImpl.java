package com.zhang.imall.service.impl;

import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.service.EmailService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;


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
    private  TemplateEngine templateEngine;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

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
    public void sentFreemarkerSimpleEmailMessage(String to, String subject,String verificationCode) {
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
            model.put("verificationCode",verificationCode);
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


}
