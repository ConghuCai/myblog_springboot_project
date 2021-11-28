package com.gitee.conghucai.blog.mapper;

import com.gitee.conghucai.blog.model.OwnerInfo;

public interface OwnerInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(OwnerInfo record);

    int insertSelective(OwnerInfo record);

    OwnerInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OwnerInfo record);

    int updateByPrimaryKey(OwnerInfo record);
}