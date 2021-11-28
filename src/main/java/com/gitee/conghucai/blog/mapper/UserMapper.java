package com.gitee.conghucai.blog.mapper;

import com.gitee.conghucai.blog.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String login);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String login);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}