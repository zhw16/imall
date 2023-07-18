package com.zhang.imall.service;

import com.zhang.imall.model.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 解析excel
 */
public interface ExcelService {
    /**
     * 解析上传的Excel
     * @param multipartFile 上传的excel
     * @return
     */
    List<User> analysisUploadFile(MultipartFile multipartFile);

    /**
     * 解析resources目录下的excel
     * @param pathName 文件路径
     * @return
     */
    List<User> analysisPathFile(String pathName);

    /**
     * 给中文名称创建英文首字母
     */
    void createUserName();
}
