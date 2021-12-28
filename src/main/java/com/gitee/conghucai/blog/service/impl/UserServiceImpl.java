package com.gitee.conghucai.blog.service.impl;

import com.gitee.conghucai.blog.config.BlogParamConfig;
import com.gitee.conghucai.blog.mapper.UserMapper;
import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    RedisTemplate<Object, Object> redis;

    @Resource
    UserMapper userMapper;

    private String prefix = "user_";
    private String keyName = "userMap";
    private int ttl = BlogParamConfig.ttl;
    private TimeUnit timeUnit = BlogParamConfig.timeUnit;

    //对于留言、评论区查所有的用户的功能  信息存在redis的map结构中  更新数据时顺带更新redis

    //对于授权后登录的用户 查用户信息的功能  查完信息后  将信息单独存放在redis的Value结构中，并且设置一个生存时间
    //首次授权后就在redis中填入用户信息

    //用户每发起一次查信息的请求  就重置这个生存时间

    //这样的话用户太久没登陆或使用url想直接套数据库里的用户信息就会在redis中命中不了
    //于是就可以让用户重新登录了.

    @Override
    // 评论、留言区获取用户信息。
    // 这一部分存放在redis的用户map中
    public User getUserByLogin(String login) {
        if(redis.opsForHash().hasKey(keyName, login)){
            return (User) redis.opsForHash().get(keyName, login);
        }
        User user = userMapper.selectByPrimaryKey(login);
        if(user != null){
            redis.opsForHash().put(keyName, login, user);
        }
        return user;
    }

    @Override
    // 如果存在user的信息  则进行更新
    // 不存在  则添加入数据库

    // 执行这个方法的一定是用户通过正常途径授权登录  因此一定要在redis中为用户分配value
    public int userAddOrUpdate(User user) {
        User oldUser = getUserByLogin(user.getLogin());

        if(oldUser != null){
            //更新数据库和redis
            int res = 0;
            try{
                res = userMapper.updateByPrimaryKey(user);
                // 更新redis中用户map
                redis.opsForHash().put(keyName, user.getLogin(), user);
                // redis中分配
                redis.opsForValue().set(prefix+user.getLogin(), user, ttl, timeUnit);
            } catch(Exception e){
                e.printStackTrace();
                return -1;
            }
            return res;
        }
        // 插入数据库中
        int res = 0;
        try {
            res = userMapper.insert(user);
            // 更新redis中用户map
            redis.opsForHash().put(keyName, user.getLogin(), user);
            // redis中分配
            redis.opsForValue().set(prefix+user.getLogin(), user, ttl, timeUnit);
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }

        return res;
    }

    @Override
    // 正常情况下  这个是用户授权后访问的服务  因此user_login这个键一定存在于redis中
    // 如果发现不存在  直接返回error  让用户重新授权登录
    // 不存在说明是通过url想直接套数据库的信息  或太长时间不操作  导致键值对过期了
    public Map<String, Object> getUserInfo(String login) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        if(!redis.hasKey(prefix+login)){
            data.put("error", "没有这个用户或者登录已过期");
            res.put("code", 500);
            res.put("data", data);
            return res;
        }
        // redis命中用户  更新ttl
        User user = (User) redis.opsForValue().get(prefix + login);
        redis.opsForValue().set(prefix + login, user, ttl, timeUnit);
        data.put("userInfo", user);
        res.put("code", 200);
        res.put("data", data);
        return res;
    }
}
