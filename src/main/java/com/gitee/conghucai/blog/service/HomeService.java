package com.gitee.conghucai.blog.service;

import com.gitee.conghucai.blog.model.User;

import java.util.Map;

public interface HomeService {

    Map<String, Object> getMenu();

    Map<String, Object> getOwnerInfo();

    Map<String, Object> getMessage();

    Map<String, Object> addMessage(User user, String message);

    Map<String, Object> getTag();

    Map<String, Object> getLink();

    Map<String, Object> getCategoryInfo(String categoryName);

    Map<String, Object> getAboutInfo();

    void helloService();

    Map<String, Object> getGiteeLogo();
}
