package com.zhang.imall.service;

import com.github.pagehelper.PageInfo;
import com.zhang.imall.model.request.CreateOrderReq;
import com.zhang.imall.model.vo.OrderVO;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : OrderService
 * @description : [描述说明该类的功能]
 * @createTime : [2022/11/15 22:32]
 */
public interface OrderService {
    /**
     * 创建一个订单
     * @param createOrderReq 收件人、手机号、地址、运费、支付方式
     * @return
     */
    String createOrder(CreateOrderReq createOrderReq);

    /**
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    OrderVO detail(String orderNo);

    /**
     * 分页显示数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    /**
     * 根据orderNo 删除记录
     * @param orderNo
     */
    void cancel(String orderNo);

    /**
     * 生成订单支付二维码
     * @param orderNo 订单号
     * @return 二维码链接
     */
    String qrcode(String orderNo);

    /**
     * 支付
     * @param orderNo
     */
    void pay(String orderNo);

    /**
     *
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return 分页
     */
    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void deliver(String orderNo);

    void finish(String orderNo);
}
