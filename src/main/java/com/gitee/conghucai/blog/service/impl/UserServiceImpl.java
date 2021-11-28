package com.gitee.conghucai.blog.service.impl;

import com.gitee.conghucai.blog.mapper.UserMapper;
import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    RedisTemplate<Object, Object> redis;

    @Resource
    UserMapper userMapper;

    private String keyName = "user";

    @Override
    public User getUserByLogin(String login) {
        if(redis.opsForHash().hasKey(keyName, login)){
            System.out.println("getUser-redis命中");
            return (User) redis.opsForHash().get(keyName, login);
        }
        System.out.println("getUser-sql命中");
        User user = userMapper.selectByPrimaryKey(login);
        if(user != null){
            redis.opsForHash().put(keyName, login, user);
        }
        return user;
    }
}
