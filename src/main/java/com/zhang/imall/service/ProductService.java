package com.zhang.imall.service;

import com.github.pagehelper.PageInfo;
import com.zhang.imall.model.pojo.Product;
import com.zhang.imall.model.request.AddProductReq;
import com.zhang.imall.model.request.ProductListReq;
import com.zhang.imall.model.request.UpdateProductReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : ProduceService
 * @description : [后台新增产品]
 * @createTime : [2022/10/18 12:07]
 */
public interface ProductService {
    //添加商品
    void add(AddProductReq addProductReq);
    //上传商品图片
    String upload(HttpServletRequest httpServletRequest, MultipartFile file);
    //更新商品
    void update(UpdateProductReq updateProductReq);
    //删除商品
    void delete(Integer id);
    //批量更改商品状态为上架下架
    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    /**
     * 根据商品id查询商品信息
     * @param id 传来的id
     */
    Product detail(Integer id);

    /**
     * 根据页号和页数据量，展示页面
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return
     */
    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    /**
     * 根据传来的数据进行列表展示
     * @param productListReq
     * @return
     */
    PageInfo pageInfoList(ProductListReq productListReq);
}
