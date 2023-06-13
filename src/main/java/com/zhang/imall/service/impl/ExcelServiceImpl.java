package com.zhang.imall.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
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
import java.util.ArrayList;
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
//        List<User> userList = EasyExcel.read("C:/Users/27850/Desktop/user-excel.xlsx")
        List<User> userList = EasyExcel.read("src/main/resources/user-excel.xlsx")
                //指定文件类型
                .excelType(ExcelTypeEnum.XLSX)
                .head(User.class)
                .sheet("Sheet1")
                .doReadSync();
        return userList;
    }



    /**
     * 解析上传的Excel
     * 解析一行处理一行数据
     * @param multipartFile 上传的excel
     * @return 解析后的用户信息列表
     */
    public List<User> analysisUploadFile1(MultipartFile multipartFile) {
        List<User> userList = new ArrayList<>();
        try {
            InputStream inputStream = multipartFile.getInputStream();
            ExcelReader excelReader = EasyExcel.read(inputStream).build();
            ReadSheet readSheet = EasyExcel.readSheet(0)
                    .head(User.class)
                    .registerReadListener(new AnalysisEventListener<User>() {
                        @Override
                        public void invoke(User user, AnalysisContext context) {
                            // 处理每一行数据
                            userList.add(user);
                        }
                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            // 解析完成后的回调方法，可以在这里进行后续操作
                        }
                    }).build();
            excelReader.read(readSheet);
            excelReader.finish();
        } catch (IOException e) {
            throw new ImallException(ImallExceptionEnum.READ_ERROR, "读文件失败");
        }
        return userList;
    }





}
