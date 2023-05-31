package com.zhang.imall.model.dao;

import com.zhang.imall.model.pojo.Product;
import com.zhang.imall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//在mapper接口上使用@Respository注解，这样idea就认为Mapper接口是个资源，在service impl注入资源时就不会报错
@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    //通过名称查询
    Product selectByName(@Param("name") String name);
    //批量上架下架产品,多个参数时候，是必须要使用@Param注解的
    void batchUpdateSellStatus(@Param("ids") Integer[] ids, @Param("sellStatus") Integer sellStatus);
    //查询商品列表,根据更新时间倒叙排列
    List<Product> selectForAdmin();
    //根据关键字和商品id查询
    List<Product> selectList(@Param("query") ProductListQuery query);
}