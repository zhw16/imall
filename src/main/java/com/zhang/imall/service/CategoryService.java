package com.zhang.imall.service;

import com.github.pagehelper.PageInfo;
import com.zhang.imall.model.pojo.Category;
import com.zhang.imall.model.request.AddCategoryReq;
import com.zhang.imall.model.vo.CategoryVO;

import java.util.List;

//商品分类的接口层
public interface CategoryService {
    //添加商品分类
    public void addCategory(AddCategoryReq addCategoryReq);

    //更新商品分类,controller传递来的封装的Category实体对象，进行判断更新即可
    void update(Category updateCategory);

    //删除分类，通过id
    void delete(Integer id);
    //分页显示分类的产品
    PageInfo listCategoryForAdmin(Integer pageNum, Integer pageSize);
    //前台获取分类
    List<CategoryVO> listCategoryForCustomer(Integer id);
}
