package com.gitee.conghucai.blog.web;

import com.gitee.conghucai.blog.service.OauthService;
import com.gitee.conghucai.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AuthController {

    @Resource
    OauthService authService;
    @Resource
    UserService userService;

    @GetMapping("/auth/gitee")
    public Object getOauthLogin(String url){
        return authService.getGithubAuthUrl(url);
    }

    @PostMapping("/auth/login")
    public Object getLoginInfo(String code, String backUrl){
        return authService.getLoginInfo(code, backUrl);
    }

    @PostMapping("/auth/user/{login}")
    public Object getUserInfo(@PathVariable("login") String login){
        return userService.getUserInfo(login);
    }

}
