package com.gitee.conghucai.blog.service;

import com.gitee.conghucai.blog.model.User;

public interface UserService {
    User getUserByLogin(String login);

}
