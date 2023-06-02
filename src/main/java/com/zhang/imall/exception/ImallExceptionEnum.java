package com.zhang.imall.exception;

/**
 * 错误的枚举类，使用","进行枚举类型的增加
 */
public enum ImallExceptionEnum {
    //对外统一展示错误代码，不对外展示详细错误信息,
    SYSTEM_ERROR(20000, "系统内部异常"),
    //业务错误10000-19999
    //user异常
    NEED_USER_NAME(10001, "用户名不能为空"),
    NEED_USER_PASSWORD(10002, "密码不能为空"),
    NEED_PASSWORD_LENGTH(10003, "密码不能少于8位"),
    NAME_EXISTED(10004, "不允许重名，操作失败"),
    INSERT_FAILED(10005, "插入失败，请重试"),
    NAME_NOT_EXISTED(10006, "用户名不存在，请检查输入或者重新注册"),
    USER_MESSAGE_ERROR(10007, "用户名密码不匹配，请检查后重新输入"),
    USER_NEED_LOGIN(10008, "用户未登录"),
    UPDATE_FAILED(10009, "更新失败"),
    NEED_ADMIN_USER(10010, "您不是管理员账户"),
    //category异常
    CATEGORY_PARA_NEED_NOT_NULL(10011, "添加目录的参数不能为空"),
    CATEGORY_NAME_EXISTED(10012, "添加目录的参数不能为空"),
    CATEGORY_CREATE_FAILED(10013, "添加目录失败"),
    REQUEST_PARAM_ERROR(10014, "参数错误"),
    DELETE_ERROR(10015, "删除失败"),
    PARENT_ID_NOT_EXISTED(10015, "删除失败"),
    //product异常
    MKDIR_FAILED(10020, "文件夹创建异常"),
    UPLOAD_FAILED(10021, "文件上传失败"),
    PRODUCT_NOT_EXISTED(10022, "产品不存在"),
    NOT_SALE(10023, "产品暂不支持下单"),
    NOT_ENOUGH(10023, "产品库存不足"),
    //cart异常
    CART_SELECT_EMPTY(10030, "未选择商品"),

    NO_ENUM(10040, "枚举不存在"),
    ORDER_CREATE_FAILED(10050, "订单创建失败"),
    NO_ORDER(10051, "没有此订单"),
    CANCEL_ORDER_FAILED(10052, "订单取消失败"),
    ORDER_STATUS_ERROR(10053, "订单状态异常"),
    NOT_YOUR_ORDER(10054, "此订单不是你的订单"),
    EMAIL_SENT_ERROR(10060, "邮件发送失败"),
    READ_ERROR(10070, "读操作失败");

    //异常码
    Integer code;
    //异常信息
    String msg;

    ImallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
