package com.gitee.conghucai.blog.service;

import java.util.Map;

public interface OauthService {

    Map<String, Object> getGithubAuthUrl(String originUrl);

    Map<String, Object> getLoginInfo(String userCode, String backUrl);

}
