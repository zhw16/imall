package com.zhang.imall.model.dao;

import com.zhang.imall.model.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("currentUserId") Integer currentUserId, @Param("orderNo") String orderNo);

    List<Order> selectByUserId(Integer currentUserId);

    List<Order> selectAllOrderForAdmin();

    Order selectByOrderNo(String orderNo);
}