package com.gitee.conghucai.blog;

import com.alibaba.fastjson.JSON;
import com.gitee.conghucai.blog.mapper.MessageMapper;
import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.HomeService;
import com.gitee.conghucai.blog.service.UserService;
import com.gitee.conghucai.blog.utils.DateTimeUtil;
import com.gitee.conghucai.blog.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
class BlogApplicationTests {

    @Resource
    RedisTemplate<Object, Object> redis;

    @Resource
    HomeService homeService;
    @Resource
    UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void homeServiceTest() {
        Map<String, Object> res = homeService.getMenu();
        System.out.println(JSON.toJSONString(res));
    }

    @Test
    void utilsTest(){
        System.out.println(UUIDUtil.getUUID());
        System.out.println(UUIDUtil.getUUID());
        System.out.println(UUIDUtil.getUUID());
        System.out.println(UUIDUtil.getUUID());
        System.out.println(UUIDUtil.getUUID());
        System.out.println(UUIDUtil.getUUID().length());

        System.out.println(DateTimeUtil.getSysDate());
        System.out.println(DateTimeUtil.getSysTime());

        System.out.println(DateTimeUtil.getSysDate().length());
        System.out.println(DateTimeUtil.getSysTime().length());
    }

    @Test
    void redisTest(){
        // redis.opsForHash().put("mykey", "A", "1");
        // redis.opsForHash().put("mykey", "B", "1");
        // redis.opsForHash().put("mykey", "C", "1");

        if(redis.opsForHash().hasKey("key", "A")){
            System.out.println("!!");
        } else{
            redis.opsForHash().put("key", "A", "1");
        }
    }

    @Test
    void userServiceTest(){
        User u = userService.getUserByLogin("conghucai");
        System.out.println(JSON.toJSONString(u));
    }

    @Resource
    MessageMapper messageMapper;
    @Test
    void mapperTest(){
        Object res = messageMapper.getMessageDetail();
        System.out.println(JSON.toJSONString(res));
    }
}
