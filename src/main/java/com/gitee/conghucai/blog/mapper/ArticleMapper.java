package com.gitee.conghucai.blog.mapper;

import com.gitee.conghucai.blog.model.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Map> selectAllByLimit();

    List<Map> selectSrcByID(@Param("carouselNum") Integer carouselNum);

    List<Map> selectBlogListByTag(String tagName);

    List<Map> selectBlogListByKeyword(String keyword);

    List<Map> selectAllByCategory(String categoryName);

    List<Map> selectBlogListByAbout(Integer aboutTagID);

    Map selectBlogInfoByKeyword(String articleID);

    List<Map> selectBlogListByArticleTags(String articleID);
}