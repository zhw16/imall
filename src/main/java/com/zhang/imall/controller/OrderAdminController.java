package com.zhang.imall.controller;

import com.github.pagehelper.PageInfo;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : OrderAdminController
 * @description : [后台订单模块]
 * @createTime : [2022/11/21 9:09]
 */
@RestController
public class OrderAdminController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/admin/order/list")
    @ApiOperation("后台订单列表")
    public ApiRestResponse listOrderForAdmin(@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isEmpty(pageNum) && StringUtils.isEmpty(pageSize)) {
            pageNum = 1;
            pageSize = 5;
        }
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
    /**
     * 后台管理员的，订单发货
     * @param orderNo
     * @return
     */
    @ApiOperation("后台管理员的，订单发货")
    @PostMapping("/admin/order/delivered")
    public ApiRestResponse delivered(@RequestParam("orderNo") String orderNo) {
        orderService.deliver(orderNo);
        return ApiRestResponse.success();
    }
    /**
     * 前后台通用：订单完结；
     * @param orderNo
     * @return
     */
    @ApiOperation("前后台通用的：完结订单")
    @PostMapping("/order/finish")
    public ApiRestResponse finish(@RequestParam("orderNo") String orderNo) {
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }
}
