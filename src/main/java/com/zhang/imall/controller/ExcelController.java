package com.zhang.imall.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.service.ExcelService;
import com.zhang.imall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String fileName = path + "用户信息" + date + ".xlsx";
        EasyExcel.write(fileName, com.zhang.imall.model.export.User.class)
                .sheet("用户表")
                .doWrite(userService.selectAllUser());
        return ApiRestResponse.success();
    }

    @GetMapping("/excel/downloadExcel")
    @ApiOperation("下载Excel")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        String fileName = "User" + LocalDate.now() + ".xlsx"; // 根据当前日期生成文件名

        // 设置响应的内容类型为Excel文件的MIME类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置响应头，告诉浏览器该文件需要下载而不是直接打开
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 创建Excel写入器
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), User.class).build();

        // 写入数据到Excel
        WriteSheet writeSheet = EasyExcel.writerSheet("用户表").build();
        List<User> userList = userService.selectAllUser(); // 假设这是要写入的用户数据列表

        excelWriter.write(userList, writeSheet);

        // 关闭Excel写入器
        excelWriter.finish();
    }


}
