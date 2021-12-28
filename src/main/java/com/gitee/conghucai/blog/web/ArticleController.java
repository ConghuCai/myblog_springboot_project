package com.gitee.conghucai.blog.web;

import com.gitee.conghucai.blog.service.ArticleService;
import com.gitee.conghucai.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleController {

    @Resource
    ArticleService articleService;
    @Resource
    CommentService commentService;

    @GetMapping("/article/list/all")
    public Object getAllArticleList(Integer page){
        return articleService.getAllArticleList(page);
    }

    @GetMapping("/article/list/tag/{tagName}")
    public Object getTagArticleList(@PathVariable("tagName") String tagName, Integer page){
        return articleService.getTagArticleList(tagName, page);
    }

    @GetMapping("/article/list/category/{categoryName}")
    public Object getCategoryArticleList(@PathVariable("categoryName") String categoryName, Integer page){
        return articleService.getCategoryArticleList(categoryName, page);
    }

    @GetMapping("/article/list/search/{keyword}")
    public Object getSearchArticleList(@PathVariable("keyword") String keyword, Integer page){
        return articleService.getSearchArticleList(keyword, page);
    }

    @GetMapping("/article/list/about/site")
    public Object getAboutArticleList(Integer page){
        return articleService.getAboutArticleList(page);
    }

    @GetMapping("/article/content/{articleID}")
    public Object getArticleContent(@PathVariable("articleID") String articleID){
        return articleService.getArticleInfo(articleID);
    }

    @GetMapping("/article/like/{articleID}")
    public Object articleLike(@PathVariable("articleID") String articleID){
        return articleService.articleOps("like", articleID);
    }

    @GetMapping("/article/share/{articleID}")
    public Object articleShare(@PathVariable("articleID") String articleID){
        return articleService.articleOps("share", articleID);
    }

    @GetMapping("/article/comment/list/{articleID}")
    public Object getCommentList(@PathVariable("articleID") String articleID){
        return commentService.getCommentList(articleID);
    }

    @PostMapping("/article/comment/add")
    public Object addComment(String articleID, String comm, String user){
        return commentService.addComment(articleID, comm, user);
    }

    @GetMapping("/article/list/recommend/article/{articleID}")
    public Object getRecommendList(@PathVariable("articleID") String articleID, Integer page) {
        return articleService.getRecommendArticleList(articleID, page);
    }

}
