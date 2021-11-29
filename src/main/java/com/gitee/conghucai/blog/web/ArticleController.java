package com.gitee.conghucai.blog.web;

import com.gitee.conghucai.blog.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleController {

    @Resource
    ArticleService articleService;

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


}
