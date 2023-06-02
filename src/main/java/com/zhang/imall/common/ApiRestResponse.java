package com.zhang.imall.common;

import com.zhang.imall.exception.ImallExceptionEnum;

/**
 * 通用的返回对象
 */
public class ApiRestResponse<T> {
    /**
     * status 状态码
     * msg 返回的字符串
     * data 泛型的数据，不固定
     */
    private Integer status;

    private String msg;

    private T data;

    //与前端约定好的状态码和信息
    private static final int OK_CODE = 10000;

    private static final String OK_MSG = "SUCCESS";

    //三个参数的构造函数
    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //两个参数的构造函数
    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    //无参数的构造函数，传递定义好的OK_CODE和OK_MSG
    public ApiRestResponse() {
        this(OK_CODE, OK_MSG);
    }

    //成功后调用的带着返回值的方法,自己会调用无参数的构造方法，也就是success 的10000+success
    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<>();
    }

    //成功并且带上返回值,属于泛型的返回值 T result,调用无参构造中的success和1000
    public static <T> ApiRestResponse<T> success(T result) {
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }


    /**
     * 失败的方法，收拢常见的错误
     *失败的状态吗
     *  失败的信息
     * @param <T>  泛型
     * @return
     */
    public static <T> ApiRestResponse<T> error(Integer code, String msg) {
        return new ApiRestResponse<>(code, msg);
    }
    //使用自定义的错误枚举类
    public static <T> ApiRestResponse<T> error(ImallExceptionEnum exceptionEnum) {
        return new ApiRestResponse<>(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }
    //使用自定义的错误枚举类
    public static <T> ApiRestResponse<T> error(ImallExceptionEnum exceptionEnum,String msg) {
        return new ApiRestResponse<>(exceptionEnum.getCode(), msg);
    }
    @Override//toString()
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

