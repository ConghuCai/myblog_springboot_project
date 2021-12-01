package com.gitee.conghucai.blog;

import com.alibaba.fastjson.JSON;
import com.gitee.conghucai.blog.config.BlogParamConfig;
import com.gitee.conghucai.blog.mapper.ArticleMapper;
import com.gitee.conghucai.blog.mapper.MessageMapper;
import com.gitee.conghucai.blog.mapper.RefArticleTagMapper;
import com.gitee.conghucai.blog.mapper.TagMapper;
import com.gitee.conghucai.blog.model.Article;
import com.gitee.conghucai.blog.model.Comment;
import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.ArticleService;
import com.gitee.conghucai.blog.service.CommentService;
import com.gitee.conghucai.blog.service.HomeService;
import com.gitee.conghucai.blog.service.UserService;
import com.gitee.conghucai.blog.utils.DateTimeUtil;
import com.gitee.conghucai.blog.utils.FileReadUtil;
import com.gitee.conghucai.blog.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

@SpringBootTest
class BlogApplicationTests {



    @Test
    void contextLoads() {
    }

    // @Resource
    // RedisTemplate<Object, Object> redis;
    //
    // @Resource
    // HomeService homeService;
    // @Resource
    // UserService userService;
    // @Test
    // void homeServiceTest() {
    //     // Map<String, Object> res = homeService.getCarousel();
    //     // System.out.println(JSON.toJSONString(res));
    //
    // }
    //
    // @Test
    // void utilsTest(){
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID());
    //     System.out.println(UUIDUtil.getUUID().length());
    //
    //     System.out.println(DateTimeUtil.getSysDate());
    //     System.out.println(DateTimeUtil.getSysTime());
    //
    //     System.out.println(DateTimeUtil.getSysDate().length());
    //     System.out.println(DateTimeUtil.getSysTime().length());
    // }
    //
    // @Test
    // void redisTest(){
    //     // redis.opsForHash().put("mykey", "A", "1");
    //     // redis.opsForHash().put("mykey", "B", "1");
    //     // redis.opsForHash().put("mykey", "C", "1");
    //
    //     if(redis.opsForHash().hasKey("key", "A")){
    //         System.out.println("!!");
    //     } else{
    //         redis.opsForHash().put("key", "A", "1");
    //     }
    //
    //     User user = (User) redis.opsForHash().get("user", "conghucai");
    //     System.out.println(user.getStared());
    // }
    //
    // @Test
    // void userServiceTest(){
    //     User u = userService.getUserByLogin("conghucai");
    //     System.out.println(JSON.toJSONString(u));
    // }
    //
    // @Resource
    // MessageMapper messageMapper;
    // @Resource
    // RefArticleTagMapper refArticleTagMapper;
    //
    // @Test
    // void mapperTest(){
    //     Object res = refArticleTagMapper.selectByArticleID("f2ab12575c774a778fd5f3063e6d5219");
    //     System.out.println(JSON.toJSONString(res));
    //
    //     // String id = (String) res.get(0).get("id");
    //     // System.out.println(id);
    // }
    //
    // @Value("${myblog.client_id}")
    // private String clientID;
    // @Value("${myblog.client_secret}")
    // private String clientSecret;
    // @Value("${myblog.redis.ttl}")
    // private int ttl;
    //
    // @Test
    // void propertiesReadTest(){
    //     System.out.println(clientID);
    //     System.out.println(clientSecret);
    //     System.out.println(BlogParamConfig.ttl);
    //     System.out.println(BlogParamConfig.markdown_base);
    // }
    //
    // @Resource
    // ArticleService articleService;
    //
    // @Test
    // void articleServiceTest(){
    //     Object res = articleService.getAllArticleList(2);
    //     System.out.println(JSON.toJSONString(res));
    //
    //
    // }
    //
    // @Resource
    // TagMapper tagMapper;
    // @Test
    // void tagMapperTest(){
    //     Object res = tagMapper.selectAll();
    //     System.out.println(JSON.toJSONString(res));
    // }
    //
    // @Resource
    // ArticleMapper articleMapper;
    // @Test
    // void articleMapperTest(){
    //     Object res = articleMapper.selectBlogListByAbout(2);
    //     System.out.println(JSON.toJSONString(res));
    // }
    //
    // private String mdBasePath = "F:\\markdown_base\\";
    // private int pageSize = BlogParamConfig.pageSize;
    // @Test
    // void fileReaderTest(){
    //     System.out.println(pageSize);
    //     String mdName = "53d15432772e4a39af1e21e71765b212.md";
    //     String category = "java";
    //
    //     String name = mdBasePath + category + File.separator + mdName;
    //     String mdContent = FileReadUtil.readMDFile(name);
    //     System.out.println(name);
    //     System.out.println(mdContent);
    // }
    //
    // @Resource
    // CommentService commentService;
    // @Test
    // void commentTransactionTest(){
    //     Comment comment = new Comment();
    //     comment.setId("8ebbea533c8641dfb16d0b59b496f852");
    //     comment.setArticleId("21ae2eb9fa484e84ae37a812700d2fa7");
    //     comment.setContent("transaction test");
    //     comment.setUserLogin("zhangyugededizhi");
    //     comment.setTime(DateTimeUtil.getSysDate());
    //
    //     Article article = new Article();
    //     article.setId("21ae2eb9fa484easa84ae37a812700d2fa7");
    //     article.setCommentCount(7);
    //
    //     try{
    //         commentService.commentTransaction(comment, article);
    //     } catch (Exception e){
    //         System.out.println(e.getMessage());
    //     }
    //
    // }

}
