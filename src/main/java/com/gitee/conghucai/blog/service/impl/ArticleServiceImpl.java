package com.gitee.conghucai.blog.service.impl;

import com.gitee.conghucai.blog.config.BlogParamConfig;
import com.gitee.conghucai.blog.mapper.ArticleMapper;
import com.gitee.conghucai.blog.mapper.RefArticleTagMapper;
import com.gitee.conghucai.blog.model.Article;
import com.gitee.conghucai.blog.service.ArticleService;
import com.gitee.conghucai.blog.utils.FileReadUtil;
import com.gitee.conghucai.blog.utils.TypeArrayUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleServiceImpl implements ArticleService {

  private int pageSize = BlogParamConfig.pageSize;
  private int carouselNum = BlogParamConfig.carouselNum;
  private int aboutTagID = BlogParamConfig.aboutTagID;
  private String mdBasePath = BlogParamConfig.markdown_base;

  @Resource
  RedisTemplate<Object, Object> redis;

  @Resource
  ArticleMapper articleMapper;
  @Resource
  RefArticleTagMapper refArticleTagMapper;

  @Override
  public Map<String, Object> getArticleCarousel() {
    if (redis.hasKey("carousel")) {
      return (Map<String, Object>) redis.opsForValue().get("carousel");
    }
    Map<String, Object> res = new HashMap<>();
    Map<String, Object> data = new HashMap<>();

    List<Map> images = articleMapper.selectSrcByID(carouselNum);
    data.put("images", images);

    res.put("data", data);
    res.put("code", 200);
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

  private Map<String, Object> getBlogList(String target, String param, Integer pageNum) {
    // PageHelper分页设置
    PageHelper.startPage(pageNum, pageSize);
    List<Map> articleList = null;
    // 根据不同的target 选择不同的mapper方法
    switch (target) {
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
      case "recommend":
        articleList = articleMapper.selectBlogListByArticleTags(param);
        break;
      default:
        articleList = articleMapper.selectAllByLimit();
        break;
    }

    for (Map m : articleList) {
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

  @Override
  public Map<String, Object> getArticleInfo(String articleID) {
    Map articlesInfo = articleMapper.selectBlogInfoByKeyword(articleID);
    if (articlesInfo == null) {
      return returnNullObjectMsg("article");
    }

    List<String> tagList = refArticleTagMapper.selectByArticleID(articleID);
    List<String> colorList = TypeArrayUtils.getTypeList(tagList);
    articlesInfo.put("tags", tagList);
    articlesInfo.put("tagType", colorList);

    Integer clickCount = (Integer) articlesInfo.get("clickCount");

    String mdName = (String) articlesInfo.get("md");
    String category = (String) articlesInfo.get("url_name");

    String mdContent = FileReadUtil.readMDFile(mdBasePath + mdName);
    articlesInfo.put("md", mdContent);

    Map<String, Object> res = new HashMap<>();
    Map<String, Object> data = new HashMap<>();

    data.put("articlesInfo", articlesInfo);

    res.put("data", data);
    res.put("code", 200);

    // 更新表 添加一次点击量
    Article article = new Article();
    article.setId(articleID);
    article.setClickCount(clickCount + 1);
    articleMapper.updateByPrimaryKeySelective(article);
    return res;
  }

  private Map<String, Object> returnNullObjectMsg(String name) {
    Map<String, Object> res = new HashMap<>();
    res.put("error", "get " + name + " error! get null result!");
    res.put("code", 500);
    return res;
  }

  @Override
  public Map<String, Object> articleOps(String target, String articleID) {
    Map<String, Object> res = new HashMap<>();
    Map<String, Object> data = new HashMap<>();
    Article article = articleMapper.selectByPrimaryKey(articleID);
    if (article == null) {
      data.put("res", "failed. can't find article.");
      res.put("data", data);
      res.put("code", 500);
      return res;
    }

    Integer count = null;
    if (target.equals("like")) {
      count = article.getLikeCount();
    } else if (target.equals("share")) {
      count = article.getShareCount();
    }

    if (count == null) {
      data.put("res", "failed. Count is null.");
      res.put("data", data);
      res.put("code", 500);
      return res;
    }

    Article newArticle = new Article();
    newArticle.setId(articleID);

    if (target.equals("like")) {
      newArticle.setLikeCount(count + 1);
    } else if (target.equals("share")) {
      newArticle.setShareCount(count + 1);
    }

    int updateRes = 0;
    try {
      updateRes = articleMapper.updateByPrimaryKeySelective(newArticle);
    } catch (Exception e) {
      data.put("res", "failed. exception :" + e.getMessage());
      res.put("data", data);
      res.put("code", 500);
      return res;
    }

    if (updateRes == 0) {
      data.put("res", "failed. nothing changed.");
      res.put("data", data);
      res.put("code", 500);
      return res;
    }

    data.put("res", "success");
    res.put("data", data);
    res.put("code", 200);
    return res;
  }

  @Override
  public Map<String, Object> getRecommendArticleList(String articleID, Integer pageNum) {
    return getBlogList("recommend", articleID, pageNum);
  }
}
