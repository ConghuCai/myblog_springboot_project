package com.gitee.conghucai.blog.web;

import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.HomeService;
import com.gitee.conghucai.blog.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HomeController {
    @Resource
    HomeService homeService;
    @Resource
    UserService userService;

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

}
