package com.zhang.imall.controller;
import com.github.pagehelper.PageInfo;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.model.pojo.Product;
import com.zhang.imall.model.request.ProductListReq;
import com.zhang.imall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : ProductController
 * @description : [商品管理模块]
 * @createTime : [2022/10/24 14:11]
 */
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 根据商品id，查看商品详情
     * 返回商品的详情信息
     */
    @PostMapping("product/detail")
    @ApiOperation("根据商品id查询商品详情")
    public ApiRestResponse detail(@RequestParam("id") Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    /**
     * 前台展示商品
     * 前台商品列表，可以传入数据进行排序和模糊搜索
     * 查询一个编号子目录下的所有产品。支持模糊查询。按照价格排序
     */
    @GetMapping("/product/list")
    public ApiRestResponse list( ProductListReq productListReq) {
        PageInfo list = productService.pageInfoList(productListReq);
        return ApiRestResponse.success(list);
    }

}
