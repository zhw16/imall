package com.zhang.imall.util;

import com.zhang.imall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密+加盐
 */
public class MD5Utils {
    /**
     * @param initialStr 初始数据
     * @return md5加密，加盐值加密，String
     */
    public static String getMD5String(String initialStr) {
        byte[] digest = null;//保存生成的byte
        try {
            MessageDigest md5= MessageDigest.getInstance("MD5");
            //调用加密算法,要求传入Byte类型,加上盐值
            digest = md5.digest((initialStr + Constant.SALT).getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //进行base64的转化,成为String类型
        return Base64.encodeBase64String(digest);
    }

    /**
     * 测试md5
     *
     * @param args
     */
    public static void main(String[] args) {
        String s = MD5Utils.getMD5String("12345444");
        System.out.println("生成的MD5代码："+s);
    }
}
