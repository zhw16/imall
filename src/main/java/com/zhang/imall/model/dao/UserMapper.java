package com.zhang.imall.model.dao;

import com.zhang.imall.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //通过用户名查询数据，不允许出现重复的用户名
    User selectByUsername(String username);

    //登录用户名和密码的验证,两个参数，需要使用@Param（）给参数进行命名，到mapper.xml就使用给参数命名的 @Param("password")来获取参数值
    User loginCheck(@Param("username") String username, @Param("password") String md5Password);

    //查询用户的邮箱地址
    User selectOneByEmailAddress(String emailAddress);

}