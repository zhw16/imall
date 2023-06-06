package com.zhang.imall.controller;

import com.alibaba.excel.EasyExcel;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.service.ExcelService;
import com.zhang.imall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
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
    @Resource
    private UserService userService;

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

    /**
     * 导出Excel
     * @return
     */
    @GetMapping("/excel/exportExcel")
    @ApiOperation("导出Excel")
    public ApiRestResponse exportExcel() {
        //指定导出的目录
        String path = "D://excel/export/";
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        String fileName = path + "User" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, com.zhang.imall.model.export.User.class)
                .sheet("用户表")
                .doWrite(userService.selectAllUser());
        return ApiRestResponse.success();
    }

}
