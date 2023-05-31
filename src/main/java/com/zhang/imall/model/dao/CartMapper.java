package com.zhang.imall.model.dao;

import com.zhang.imall.model.pojo.Cart;
import com.zhang.imall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    //根据userid查询出来购物车数据，存放到list
    List<CartVO> selectListByUserId(@Param("userId") Integer userId);

    //查询此人购物车是否已经有这个产品
    Cart selectByUserIdAndProductId(@Param("currentUserId") Integer currentUserId, @Param("productId") Integer productId);
    //更新选中状态
    int updateSelectOrNot(@Param("userId") Integer userId, @Param("productId") Integer productId,@Param("selectOrNot") Integer selectStatus);
}