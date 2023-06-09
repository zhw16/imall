//package com.zhang.imall.model.export;
//
//import com.alibaba.excel.annotation.ExcelIgnore;
//import com.alibaba.excel.annotation.ExcelProperty;
//import com.alibaba.excel.annotation.format.DateTimeFormat;
//import com.alibaba.excel.annotation.write.style.ColumnWidth;
//
//import java.util.Date;
//
///**
// * @ExcelProperty：核心注解，value属性可用来设置表头名称，converter属性可以用来设置类型转换器；
// * @ColumnWidth：用于设置表格列的宽度；
// * @DateTimeFormat：用于设置日期转换格式；
// * @NumberFormat：用于设置数字转换格式。
// */
//public class User {
//    @ColumnWidth(20)
//    @ExcelProperty("用户id")
//    private Integer id;
//
//    @ColumnWidth(20)
//    @ExcelProperty("姓名")
//    private String username;
//
//    @ExcelIgnore
//    private String password;
//
//    @ColumnWidth(30)
//    @ExcelProperty("签名")
//    private String personalizedSignature;
//
//    @ColumnWidth(10)
//    @ExcelIgnore
//    private Integer role;
//
//    @ColumnWidth(20)
//    @DateTimeFormat("yyyy-MM-dd hh-mm-ss")
//    @ExcelProperty("创建时间")
//    private Date createTime;
//
//    @ColumnWidth(20)
//    @ExcelProperty("更新时间")
//    @DateTimeFormat("yyyy-MM-dd")
//    private Date updateTime;
//
//    @ColumnWidth(20)
//    @ExcelProperty("邮箱")
//    private String email;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username == null ? null : username.trim();
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password == null ? null : password.trim();
//    }
//
//    public String getPersonalizedSignature() {
//        return personalizedSignature;
//    }
//
//    public void setPersonalizedSignature(String personalizedSignature) {
//        this.personalizedSignature = personalizedSignature == null ? null : personalizedSignature.trim();
//    }
//
//    public Integer getRole() {
//        return role;
//    }
//
//    public void setRole(Integer role) {
//        this.role = role;
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email == null ? null : email.trim();
//    }
//}