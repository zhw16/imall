package com.zhang.imall.util;

import java.util.Random;

/**
 * 生成加密使用的盐值，用于md5加密
 */
public class SaltUtil {
    /**
     * 返回字符和数字的组合一共6位数
     * @return
     */
    private static String getCharAndNum() {
        //存放生成的数据，数字加字母一共6位
        StringBuilder stringBuilder = new StringBuilder();
        String charStr = "1234567890abcdefghigklmnopqrstuvwxyz";
        for (int i = 0; i <= 5; i++) {
            //随机取一个数字或者字母所在的index
            int nextInt = new Random().nextInt(charStr.length());
            //取index所在的字符
            stringBuilder.append(charStr.charAt(nextInt));
        }
        return stringBuilder.toString();
    }




    public static void main(String[] args) {
        System.out.println(SaltUtil.getCharAndNum());
    }
}
