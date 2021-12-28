package com.gitee.conghucai.blog.web;

import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.ArticleService;
import com.gitee.conghucai.blog.service.HomeService;
import com.gitee.conghucai.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HomeController {
    @Resource
    HomeService homeService;
    @Resource
    UserService userService;
    @Resource
    ArticleService articleService;

    @GetMapping("/home/hello")
    public void helloBlog() {
        homeService.helloService();
    }

    @GetMapping("/home/gitee")
    public Object getGiteeLogo(){
        return homeService.getGiteeLogo();
    }

    @GetMapping("/home/menu")
    public Object getMenu(){
        return homeService.getMenu();
    }

    @GetMapping("/home/owner")
    public Object getOwnerInfo(){
        return homeService.getOwnerInfo();
    }

    @GetMapping("/home/message/list")
    public Object getMessage(){
        return homeService.getMessage();
    }

    @PostMapping("/home/message/add")
    public Object addMessage(String login, String msg){
        User user = userService.getUserByLogin(login);
        return homeService.addMessage(user, msg);
    }

    @GetMapping("/home/tag")
    public Object getTag(){
        return homeService.getTag();
    }

    @GetMapping("/home/link")
    public Object getLink(){
        return homeService.getLink();
    }

    @GetMapping("/home/carousel")
    public Object getCarousel(){
        return articleService.getArticleCarousel();
    }

    @GetMapping("/category/info/{categoryName}")
    public Object getCategoryInfo(@PathVariable("categoryName") String categoryName) {
        return homeService.getCategoryInfo(categoryName);
    }

    @GetMapping("/about/info")
    public Object getAboutInfo(){
        return homeService.getAboutInfo();
    }
}
