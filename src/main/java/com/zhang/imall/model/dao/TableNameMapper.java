package com.zhang.imall.model.dao;

import com.zhang.imall.model.pojo.TableName;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableNameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TableName record);

    int insertSelective(TableName record);

    TableName selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TableName record);

    int updateByPrimaryKey(TableName record);

    List<String> selectAll();

    int updateByUserName(@Param("chineseStr") String chineseStr, @Param("username1") String username1);
}