package com.gitee.conghucai.blog.service;

import java.util.Map;

public interface ArticleService {
    Map<String, Object> getArticleCarousel();

    Map<String, Object> getAllArticleList(Integer pageNum);

    Map<String, Object> getTagArticleList(String tagName, Integer pageNum);

    Map<String, Object> getCategoryArticleList(String categoryName, Integer pageNum);

    Map<String, Object> getSearchArticleList(String keyword, Integer pageNum);

    Map<String, Object> getAboutArticleList(Integer pageNum);

    Map<String, Object> getArticleInfo(String articleID);

    Map<String, Object> articleOps(String target, String articleID);

    Map<String, Object> getRecommendArticleList(String articleID, Integer pageNum);


}
