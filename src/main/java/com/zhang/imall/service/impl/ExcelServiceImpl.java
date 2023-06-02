package com.zhang.imall.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.service.ExcelService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * @ClassName ExcelServiceImpl
 * @Author ZhangHewen
 * @Description
 * @Date 2023/6/2 14:43
 * @Version V1.0
 **/
@Service
public class ExcelServiceImpl implements ExcelService {


    /**
     * 解析上传的Excel
     * 同步解析，解析完进行下一步操作
     * @param multipartFile 上传的excel
     * @return
     */
    @Override
    public List<User> analysisUploadFile(MultipartFile multipartFile) {
        try {
            //获得文件输入流
            InputStream inputStream = multipartFile.getInputStream();
            //读取表格,对应成User.class；使用sheet1表；同步解析解析完成所有行进行下一步。
            List<User> sheet1 = EasyExcel.read(inputStream)
                    .head(User.class)//保证表头名称和类属性保持一致
                    .sheet("Sheet1")
                    .doReadSync();
            return sheet1;
        } catch (IOException e) {
            throw new ImallException(ImallExceptionEnum.READ_ERROR, "读文件失败");
        }
    }

    /**
     * 解析resources目录下的excel
     *
     * @param pathName 文件路径
     * @return
     */
    @Override
    public List<User> analysisPathFile(String pathName) {
        List<User> userList = EasyExcel.read(pathName)
                //指定文件类型
                .excelType(ExcelTypeEnum.XLSX)
                .head(User.class)
                .sheet(1)
                .doReadSync();
        return userList;
    }
}
