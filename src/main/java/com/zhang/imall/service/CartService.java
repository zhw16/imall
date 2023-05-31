package com.zhang.imall.service;

import com.zhang.imall.model.vo.CartVO;

import java.util.List;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : CartService
 * @description : [描述说明该类的功能]
 * @createTime : [2022/10/31 13:50]
 */
public interface CartService {
    /**
     * 根据id取数据
     * @param  userId 用户id
     * @return 数据
     */
    List<CartVO> list(Integer userId);

    /**
     *
     * @param currentUserId 登录用户id
     * @param productId 产品id
     * @param count 数量
     * @return  返回购物车信息对象
     */
    List<CartVO> addCartInfo(Integer currentUserId, Integer productId, Integer count);

    /**
     *
     * @param userId 用户id
     * @param productId 产品id
     * @param newCount 数量
     * @return
     */
    List<CartVO> updateCart(Integer userId, Integer productId, Integer newCount);

    /**
     *
     * @param currentUserId 用户id
     * @param productId 产品id
     * @return 购物车对象集合
     */
    List<CartVO> deleteCart(Integer currentUserId, Integer productId);

    /**
     * 选中或者取消选中
     * @param userId 用户id
     * @param productId 产品id
     * @param selectStatus 状态
     * @return 购物车
     */
    List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selectStatus);

    /**
     * 获得购物车列表，将状态改为全选和全不选
     * @param userId 用户id
     * @param selectStatus 全选/全不选
     * @return 购物车
     */
    List<CartVO> selectAllOrNot(Integer userId, Integer selectStatus);
}
