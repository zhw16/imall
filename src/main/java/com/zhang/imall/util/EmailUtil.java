package com.zhang.imall.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @ClassName EmailUtil
 * @Author ZhangHewen
 * @Description 邮件工具类
 * @Date 2023/5/30 11:06
 * @Version V1.0
 **/
public class EmailUtil {
    /**
     * 检查邮件地址是否有效
     * @param emailAddress  邮箱地址
     * @return
     */
    public static boolean isValidEmailAddress(String emailAddress) {
        Boolean flag = true;
        try {
            InternetAddress internetAddress = new InternetAddress(emailAddress);
            internetAddress.validate();
        } catch (AddressException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
