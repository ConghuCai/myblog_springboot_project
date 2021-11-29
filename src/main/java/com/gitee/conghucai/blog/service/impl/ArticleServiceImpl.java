package com.gitee.conghucai.blog.service.impl;

import com.gitee.conghucai.blog.config.BlogParamConfig;
import com.gitee.conghucai.blog.mapper.ArticleMapper;
import com.gitee.conghucai.blog.mapper.RefArticleTagMapper;
import com.gitee.conghucai.blog.service.ArticleService;
import com.gitee.conghucai.blog.utils.TypeArrayUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleServiceImpl implements ArticleService {

    private int pageSize = BlogParamConfig.pageSize;
    private int carouselNum = BlogParamConfig.carouselNum;
    private int aboutTagID = BlogParamConfig.aboutTagID;

    @Resource
    RedisTemplate<Object, Object> redis;

    @Resource
    ArticleMapper articleMapper;
    @Resource
    RefArticleTagMapper refArticleTagMapper;

    @Override
    public Map<String, Object> getArticleCarousel() {
        if(redis.hasKey("carousel")){
            System.out.println("carousel-redis命中");
            return (Map<String, Object>) redis.opsForValue().get("carousel");
        }
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        List<Map> images = articleMapper.selectSrcByID(carouselNum);
        data.put("images", images);

        res.put("data", data);
        res.put("code", 200);
        System.out.println("carousel-sql命中");
        redis.opsForValue().set("carousel", res, 60, TimeUnit.MINUTES);

        return res;
    }

    @Override
    public Map<String, Object> getAllArticleList(Integer pageNum) {
        return getBlogList("all", null, pageNum);
    }

    @Override
    public Map<String, Object> getTagArticleList(String tagName, Integer pageNum) {
        return getBlogList("tag", tagName, pageNum);
    }

    @Override
    public Map<String, Object> getCategoryArticleList(String categoryName, Integer pageNum) {
        return getBlogList("category", categoryName, pageNum);
    }

    @Override
    public Map<String, Object> getSearchArticleList(String keyword, Integer pageNum) {
        return getBlogList("search", keyword, pageNum);
    }

    @Override
    public Map<String, Object> getAboutArticleList(Integer pageNum) {
        return getBlogList("about", null, pageNum);
    }

    private Map<String, Object> getBlogList(String target, String param, Integer pageNum){
        // PageHelper分页设置
        PageHelper.startPage(pageNum, pageSize);
        List<Map> articleList = null;
        //根据不同的target  选择不同的mapper方法
        switch(target){
            case "tag":
                articleList = articleMapper.selectBlogListByTag(param);
                break;
            case "category":
                articleList = articleMapper.selectAllByCategory(param);
                break;
            case "search":
                articleList = articleMapper.selectBlogListByKeyword(param);
                break;
            case "about":
                articleList = articleMapper.selectBlogListByAbout(aboutTagID);
                break;
            default:
                articleList  = articleMapper.selectAllByLimit();
                break;
        }

        for(Map m : articleList){
            String id = (String) m.get("id");
            List<String> tagList = refArticleTagMapper.selectByArticleID(id);
            List<String> colorList = TypeArrayUtils.getTypeList(tagList);

            m.put("tag", tagList);
            m.put("tagType", colorList);
        }
        PageInfo<Map> articles = new PageInfo<>(articleList);

        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("articles", articles.getList());
        data.put("totalItem", articles.getTotal());
        data.put("pageSize", pageSize);

        res.put("data", data);
        res.put("code", 200);

        return res;
    }
}
