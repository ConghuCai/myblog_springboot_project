package com.gitee.conghucai.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.OauthService;
import com.gitee.conghucai.blog.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class OauthServiceImpl implements OauthService {
    @Value("${myblog.client_id}")
    private String clientID;
    @Value("${myblog.client_secret}")
    private String clientSecret;

    @Resource
    UserService userService;

    @Override
    public Map<String, Object> getGithubAuthUrl(String originUrl) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        String authorizeUrl = "https://gitee.com/oauth/authorize?client_id=" +
                clientID + "&response_type=code&state=1&redirect_uri=" + originUrl;
        data.put("authorizeUrl", authorizeUrl);

        res.put("data", data);
        res.put("code", 200);
        return res;
    }

    @Override
    public Map<String, Object> getLoginInfo(String userCode, String backUrl) {
        String httpUrl = "https://gitee.com/oauth/token?grant_type=authorization_code&code="+userCode +
                "&client_id="+clientID+"&redirect_uri="+backUrl+
                "&client_secret="+clientSecret;

        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        JSONObject loginInfo = null;

        try{
            //向gitee发起请求，获取用户token码
            JSONObject token = JSON.parseObject(getResponse(httpUrl, true));

            httpUrl = "https://gitee.com/api/v5/user?access_token=" + token.get("access_token");

            //根据用户token码获取用户的个人信息
            loginInfo = JSON.parseObject(getResponse(httpUrl, false));
        } catch(Exception e) {
            data.put("error", e.getMessage());
            res.put("data", data);
            res.put("code", 500);
            return res;
        }

        //将用户信息注册到数据库中
        User user = new User();
        user.setLogin((String) loginInfo.get("login"));
        user.setName((String) loginInfo.get("name"));
        user.setAvatarUrl((String) loginInfo.get("avatar_url"));
        user.setBio((String) loginInfo.get("bio"));
        user.setCreatedAt((String) loginInfo.get("created_at"));
        user.setEmail((String) loginInfo.get("email"));
        user.setHtmlUrl((String) loginInfo.get("html_url"));
        user.setFollowers((Integer) loginInfo.get("followers"));
        user.setStared((Integer) loginInfo.get("stared"));
        user.setWatched((Integer) loginInfo.get("watched"));

        int userServeRes = userService.userAddOrUpdate(user);
        if(userServeRes == -1){
            data.put("error", "t_user数据添加/更新失败");
            res.put("data", data);
            res.put("code", 500);
            return res;
        }

        data.put("loginInfo", loginInfo);
        res.put("data", data);
        res.put("code", 200);
        return res;
    }

    private String getResponse(String httpUrl, boolean usePost){
        RestTemplate restTemplate = new RestTemplate();
        // 请求方式
        ResponseEntity<String> response;
        if(usePost){
            // 构建一个Http报文
            HttpHeaders requestHeaders = new HttpHeaders();
            // 指定响应返回json格式
            requestHeaders.add("accept", "application/json");
            // 构建请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
            response = restTemplate.postForEntity(httpUrl, requestEntity, String.class);
        } else{
            response = restTemplate.getForEntity(httpUrl, String.class);
        }

        String responseStr = response.getBody();

        return responseStr;
    }
}
