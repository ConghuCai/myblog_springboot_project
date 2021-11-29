package com.gitee.conghucai.blog.mapper;

import com.gitee.conghucai.blog.model.RefArticleTag;

import java.util.List;

public interface RefArticleTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefArticleTag record);

    int insertSelective(RefArticleTag record);

    RefArticleTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefArticleTag record);

    int updateByPrimaryKey(RefArticleTag record);

    List<String> selectByArticleID(String articleID);
}