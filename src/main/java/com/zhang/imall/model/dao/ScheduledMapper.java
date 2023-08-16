package com.zhang.imall.model.dao;

import com.zhang.imall.model.pojo.Scheduled;

public interface ScheduledMapper {
    int deleteByPrimaryKey(String cronId);

    int insert(Scheduled record);

    int insertSelective(Scheduled record);

    Scheduled selectByPrimaryKey(String cronId);

    int updateByPrimaryKeySelective(Scheduled record);

    int updateByPrimaryKey(Scheduled record);
}