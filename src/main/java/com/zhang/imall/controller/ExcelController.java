package com.zhang.imall.controller;

import com.alibaba.excel.EasyExcel;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.service.ExcelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName ExcelController
 * @Author ZhangHewen
 * @Description 本接口用于解析excel
 * @Date 2023/6/1 17:56
 * @Version V1.0
 **/
@RestController
public class ExcelController {

    @Autowired
    private ExcelService excelService;


    /**
     * 解析上传的Excel,返回文件里面的批量的用户信息
     * 可以自动转化大小写
     * @param multipartFile 上传的Excel文件
     * @return  将Excel文件映射成User.java文件的用户属性，保证表头和类属性一致
     */
    @ApiOperation("解析上传的excel")
    @PostMapping("/excel/analysisUploadFile")
    public ApiRestResponse<List<User>> analysisUploadFile(@RequestParam("file") MultipartFile multipartFile) {
       List<User> batchUserList= excelService.analysisUploadFile(multipartFile);
        return ApiRestResponse.success(batchUserList);
    }

    /**
     * 解析指定路径上的Excel
     *
     * @return 用户的列表
     */
    @PostMapping("/excel/analysisPathFile")
    @ApiOperation("解析指定路径的Excel")
    public ApiRestResponse analysisPathFile() {
        //获取相对路径
        ClassPathResource classPathResource = new ClassPathResource("user-excel.xlsx");
        String path= classPathResource.getPath();
        System.out.println("相对路径"+path);
        List<User> users = excelService.analysisPathFile(path);
        return ApiRestResponse.success(users);
    }

}
