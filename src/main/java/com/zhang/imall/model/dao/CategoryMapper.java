package com.zhang.imall.model.dao;

import com.zhang.imall.model.pojo.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
    //通过名字查询商品目录
    Category selectByName(String name);
    //查询商品分类的所有的数据
    List<Category> selectList();
    //根据parent_id，查询出该级别的数据；
    List<Category> selectCategoriesByParentId(Integer parentId);
}