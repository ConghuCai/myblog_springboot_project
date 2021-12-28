package com.gitee.conghucai.blog.mapper;

import com.gitee.conghucai.blog.model.BlogTitle;

public interface BlogTitleMapper {
    int deleteByPrimaryKey(String id);

    int insert(BlogTitle record);

    int insertSelective(BlogTitle record);

    BlogTitle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BlogTitle record);

    int updateByPrimaryKey(BlogTitle record);

    String selectGiteeLogoByPrimaryKey(String id);
}