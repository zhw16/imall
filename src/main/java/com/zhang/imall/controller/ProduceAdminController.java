package com.zhang.imall.controller;

import com.github.pagehelper.PageInfo;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.request.AddProductReq;
import com.zhang.imall.model.request.UpdateProductReq;
import com.zhang.imall.service.ProductService;
import com.zhang.imall.util.URIUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : ProduceAdminController
 * @description : [管理员商品后台接口]
 * @createTime : [2022/10/18 12:00]
 */
@RestController
public class ProduceAdminController {
    @Autowired
    ProductService productService;

    @ApiOperation("新增商品")
    @PostMapping("/admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     * @param httpServletRequest 请求体
     * @param file               在body 里面form-data 里面file
     * @return 拼接的文件名 http://localhost:8000/images/cdb72623-f79d-4d27-bb60-6c058efab6d1.png
     * 变更将资源路径改变，进行静态资源映射
     */
    @ApiOperation("上传文件（这儿具体来说，就是图片）")
    @PostMapping("/admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        String uploadUrl = productService.upload(httpServletRequest, file);
        return ApiRestResponse.success(uploadUrl);
    }

    /**
     * 更新商品，保证不重名
     * 使用商品id进行更新
     */
    @PostMapping("/admin/product/update")
    @ApiOperation("更新商品api")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        productService.update(updateProductReq);
        return ApiRestResponse.success();
    }

    /**
     * 删除商品界面
     * 使用id进行删除
     */
    @PostMapping("/admin/product/delete")
    @ApiOperation("使用id删除商品")
    public ApiRestResponse delete(@RequestParam("id") Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    /**
     * 批量上架下架商品使用id ，多个id组成ids[]数组.
     * status 状态
     * 更改商品里面的status ：0-下架、1上架.
     */
    @PostMapping("/admin/product/batchUpdateSellStatus")
    @ApiOperation("批量上下架产品")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam("ids") Integer[] ids, @RequestParam("sellStatus") Integer sellStatus) {
        //使用id批量将商品状态变成status
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    /**
     * 分页显示所有的商品信息
     * 传入pagenum，pagesize
     */
    @ApiOperation("后台商品的列表")
    @GetMapping("/admin/product/list")
    public ApiRestResponse list(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

}
