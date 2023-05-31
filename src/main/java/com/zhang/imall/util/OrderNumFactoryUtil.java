package com.zhang.imall.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : OrderCodeFactoryUtil
 * @description : [生成订单号，拼接进去二进制的用户id]
 * @createTime : [2022/11/16 14:51]
 */
public class OrderNumFactoryUtil {
    /**
     * 为了防止重复，使用时间+随机数20221116215320+random
     * 订单号和userId相关
     */
    public static String createOrderCode(Integer userid) {
        //获得当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = dateFormat.format(new Date());
        //随机数5位
        int randomNum = new Random().nextInt(9999)+10000;
        //userId的八进制取四位
        String userIdSub = toBinary(userid, 4);
        return currentDate + userIdSub + randomNum;
    }

    /**
     *
     * @param num 要化为二进制的数据源
     * @param digits 保留多少位
     * @return 生成的digits位二进制
     */
    public static String toBinary(int num, int digits) {
        //将无符号数1，向左移动digits位，低位补零。
        // “|”：0|1=1；1|1=1；0|0=0.
        int value = (1 << digits) | num;
        String bs = Integer.toBinaryString(value);
        return  bs.substring(1);
    }

    public static void main(String[] args) {
        System.out.println("订单号："+createOrderCode(1));
    }
}
