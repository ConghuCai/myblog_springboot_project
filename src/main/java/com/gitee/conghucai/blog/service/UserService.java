package com.gitee.conghucai.blog.service;

import com.gitee.conghucai.blog.model.User;

import java.util.Map;

public interface UserService {
    User getUserByLogin(String login);

    int userAddOrUpdate(User user);

    Map<String, Object> getUserInfo(String login);

}
