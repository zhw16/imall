package com.zhang.imall.model.request;

import javax.validation.constraints.NotNull;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : CreateOrderReq
 * @description : [封装创建订单的请求参数]
 * @createTime : [2022/11/15 22:23]
 */
public class CreateOrderReq {
    @NotNull
    private String receiverName;//收货人
    @NotNull
    private String receiverMobile;//收获手机号
    @NotNull
    private String receiverAddress;//收货地址
    private Integer carriage = 0;//运费,默认包邮
    private Integer paymentType = 1;//支付方式，默认在线支付

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getCarriage() {
        return carriage;
    }

    public void setCarriage(Integer carriage) {
        this.carriage = carriage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "CreateOrderReq{" +
                "receiverName='" + receiverName + '\'' +
                ", receiverMobile='" + receiverMobile + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", carriage=" + carriage +
                ", paymentType=" + paymentType +
                '}';
    }
}
