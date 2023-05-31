package com.zhang.imall.service.impl;

import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.dao.CartMapper;
import com.zhang.imall.model.dao.ProductMapper;
import com.zhang.imall.model.pojo.Cart;
import com.zhang.imall.model.pojo.Product;
import com.zhang.imall.model.vo.CartVO;
import com.zhang.imall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : CartServiceImpl
 * @description : [描述说明该类的功能]
 * @createTime : [2022/10/31 13:51]
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 根据id取数据
     *
     * @param userId 用户id
     * @return 个人购物车数据
     */
    @Override
    public List<CartVO> list(Integer userId) {
        //cart表左连接product表.查询用户id对应下的每个商品的信息。组成list
        //所以购物车对应的数据是一个产品信息 对应一个id。
        //查询出来的是一个用户的多个产品的购物车情况
        List<CartVO> currentCartVO = cartMapper.selectListByUserId(userId);
        for (CartVO cartVO : currentCartVO) {
            //设置总价 单价*数量
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return currentCartVO;
    }

    /**
     * @param currentUserId 登录用户id
     * @param productId     产品id
     * @param count         数量
     * @return 返回购物车信息对象
     */
    @Override
    public List<CartVO> addCartInfo(Integer currentUserId, Integer productId, Integer count) {
        //1.查询是否有这个商品\没有此产品或者是下架\查询库存
        checkProduct(productId,count);
        //2.查询用户购物车是否包含这个产品
        Cart currentProduct = cartMapper.selectByUserIdAndProductId(currentUserId, productId);
        if (currentProduct == null) {//购物车没有这个商品
            //新建一个购物车商品对象
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(currentUserId);
            cart.setQuantity(count);
            cart.setSelected(Constant.CartIsSelected.CHECKED);//默认选中
            int i = cartMapper.insertSelective(cart);
            if (i != 1) {
                throw new ImallException(ImallExceptionEnum.UPDATE_FAILED);
            }
        }else {//购物车有这个商品
            //创建一个新的对象，选择性更新数据：更新商品数量和商品是否被选择。
            Cart tempCart = new Cart();
            tempCart.setId(currentProduct.getId());
            tempCart.setQuantity(count + currentProduct.getQuantity());
            tempCart.setSelected(Constant.CartIsSelected.CHECKED);
            tempCart.setUserId(currentProduct.getUserId());
            tempCart.setProductId(currentProduct.getProductId());
            cartMapper.updateByPrimaryKeySelective(tempCart);
        }

        return this.list(currentUserId);
    }

    /**
     * @param userId    用户id
     * @param productId 产品id
     * @param newCount  数量
     * @return
     */
    @Override
    public List<CartVO> updateCart(Integer userId, Integer productId, Integer newCount) {
        //校验商品 上架、库存
        checkProduct(productId,newCount);
        //根据用户id获得购物车
        Cart currentCart = cartMapper.selectByUserIdAndProductId(userId, productId);
        //购物车没有此产品
        if (currentCart == null) {
            throw new ImallException(ImallExceptionEnum.UPDATE_FAILED);
        }
        //更新商品数量
        Cart cart = new Cart();
        cart.setId(currentCart.getId());
        cart.setSelected(Constant.CartIsSelected.CHECKED);
        cart.setQuantity(newCount);
        cartMapper.updateByPrimaryKeySelective(cart);
        return list(userId);
    }

    /**
     * @param currentUserId 用户id
     * @param productId     产品id
     * @return 购物车对象集合
     */
    @Override
    public List<CartVO> deleteCart(Integer currentUserId, Integer productId) {
        //检查是否有此购物车
        Cart cart = cartMapper.selectByUserIdAndProductId(currentUserId, productId);
        if (cart == null) {
            throw new ImallException(ImallExceptionEnum.DELETE_ERROR);
        }
        //删除产品
        cartMapper.deleteByPrimaryKey(cart.getId());
        return this.list(currentUserId);
    }

    /**
     * 选中或者取消选中
     *
     * @param userId       用户id
     * @param productId    产品id
     * @param selectStatus 状态
     * @return 购物车
     */
    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selectStatus) {
        //根据用户id和商品id查找购物车
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new ImallException(ImallExceptionEnum.PRODUCT_NOT_EXISTED);
        }
        cartMapper.updateSelectOrNot(userId, productId, selectStatus);
        return this.list(userId);
    }

    /**
     * 获得购物车列表，将状态改为全选和全不选
     *
     * @param userId       用户id
     * @param selectStatus 全选/全不选
     * @return 购物车
     */
    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selectStatus) {
        //根据用户id，获得购物车
        List<CartVO> cartVOS = cartMapper.selectListByUserId(userId);
        //设置状态
        for (CartVO cartVO : cartVOS) {
            cartVO.setSelected(selectStatus);
        }
        return this.list(userId);
    }

    /**
     * 抽离出来的工具方法
     * 校验商品是否存在，是否上架，受否库存够
     * @param productId 产品id
     * @param count 数量
     */
    private void checkProduct(Integer productId, Integer count) {
        //1.查询是否有这个商品
        Product product = productMapper.selectByPrimaryKey(productId);
        //没有此产品或者是下架
        if (product == null || !(product.getStatus()).equals( Constant.SaleStatus.SALE)) {
            throw new ImallException(ImallExceptionEnum.NOT_SALE);
        }
        //查询库存
        if (product.getStock() < count) {
            throw new ImallException(ImallExceptionEnum.NOT_ENOUGH);
        }
    }
}
