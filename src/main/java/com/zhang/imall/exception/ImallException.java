package com.zhang.imall.exception;

/**
 * 自定义的异常类，统一异常处理
 */
public class ImallException extends RuntimeException {

    private final Integer code;
    //一定要是message，程序在执行的过程中会自动调用getMessage（），但是如果定义字段为msg，就会调用自带的getMessage（），所以会返回null。
    private final String message;

    //在构造函数中位final修饰的属性赋值
    public ImallException(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ImallException(ImallExceptionEnum imallExceptionEnum) {
        //使用关键字this调用上面的构造函数,
        this(imallExceptionEnum.getCode(), imallExceptionEnum.getMsg());
    }
    public ImallException(ImallExceptionEnum imallExceptionEnum,String msg) {
        //使用关键字this调用上面的构造函数,
        this(imallExceptionEnum.getCode(), msg);
    }

    //生成getter
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
