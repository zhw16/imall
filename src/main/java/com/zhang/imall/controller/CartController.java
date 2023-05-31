package com.zhang.imall.controller;

import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.filter.UserFilter;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.model.vo.CartVO;
import com.zhang.imall.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : CartController
 * @description : [购物车控制类]
 * @createTime : [2022/10/31 13:49]
 */
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 购物车添加商品，需要产品id和数量
     * 用户id从session获取
     * 返回购物车的列表数据
     *
     * @return
     */
    @PostMapping("/cart/add")
    public ApiRestResponse addCart(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) {
        //获取登录用户
        Integer currentUserId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.addCartInfo(currentUserId, productId, count);
        return ApiRestResponse.success(cartVOS);
    }

    /**
     * 由过滤器拿到用户信息，展示用户的购物车
     * 不对用户购物车进行分页
     *
     * @return CartVO, 承接购物车的数据
     */
    @GetMapping("/cart/list")
    @ApiOperation("显示购物车")
    public ApiRestResponse listCart() {
        //当前登录用户
        User currentUser = UserFilter.currentUser;
        //根据用户id查询用户购物车
        List<CartVO> cartVOS = cartService.list(currentUser.getId());
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("更新购物车产品数量")
    @PostMapping("/cart/update")
    public ApiRestResponse updateCart(Integer productId, Integer newCount) {
        //获取userId
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.updateCart(userId, productId, newCount);
        return ApiRestResponse.success(cartVOS);
    }

    @PostMapping("/cart/delete")
    @ApiOperation("根据商品id删除购物车里面的商品")
    public ApiRestResponse deleteCart(Integer productId) {
        //获取登录用户
        Integer currentUserId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.deleteCart(currentUserId, productId);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("选中/不选某商品")
    @PostMapping("/cart/updateStatus")
    public ApiRestResponse selectCart(Integer productId, Integer selectStatus) {
        List<CartVO> cartVOS = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selectStatus);
        return ApiRestResponse.success(cartVOS);
    }

    @ApiOperation("全部选择/取消商品")
    @PostMapping("/cart/selectAll")
    public ApiRestResponse selectAll(Integer selectStatus) {
        List<CartVO> cartVOS = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selectStatus);
        return ApiRestResponse.success(cartVOS);
    }

}
