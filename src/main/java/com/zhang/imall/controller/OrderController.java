package com.zhang.imall.controller;

import com.github.pagehelper.PageInfo;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.model.request.CreateOrderReq;
import com.zhang.imall.model.vo.OrderVO;
import com.zhang.imall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : OrderController
 * @description : [订单]
 * @createTime : [2022/11/15 18:59]
 */
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param createOrderReq 收件人、电话、地址、运费、支付方式
     * @return 订单号
     */
    @PostMapping("/order/create")
    @ApiOperation("创建订单")
    public ApiRestResponse createOrder(@RequestBody @Valid CreateOrderReq createOrderReq) {
        //创建订单后，返回订单号
        String orderNum = orderService.createOrder(createOrderReq);
        return ApiRestResponse.success(orderNum);
    }
    /**
     * 点击订单号，出来订单的完整信息:收货信息，支付信息，订单的商品列表
     * @param orderNo 订单号
     * @return 详细订单视图
      */
    @GetMapping("order/detail")
    @ApiOperation("前台订单详情")
    public ApiRestResponse detail(@RequestParam String orderNo) {
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    /**
     * 前台订单列表
     *
     * @param pageNum  第几页
     * @param pageSize 每页多少数据
     * @return
     */
    @GetMapping("order/list")
    @ApiOperation("前台订单列表")
    public ApiRestResponse list(@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isEmpty(pageNum) && StringUtils.isEmpty(pageSize)) {
            pageNum = 1;
            pageSize = 5;
        }
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 订单取消
     */
    @PostMapping("order/cancel")
    @ApiOperation("前台取消订单")
    public ApiRestResponse cancel(@RequestParam String orderNo) {
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 生成支付二维码
     */
    @GetMapping("order/qrcode")
    @ApiOperation("生成支付二维码")
    public ApiRestResponse qrcode(@RequestParam String orderNo) {
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    @GetMapping("order/pay")
    @ApiOperation("支付接口")
    public ApiRestResponse pay(@RequestParam String orderNo) {
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }
}

