package com.gitee.conghucai.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BlogParamConfig {

    public static int ttl;

    public static TimeUnit timeUnit = TimeUnit.MINUTES;

    public static String markdown_base;

    public static int pageSize;

    public static int carouselNum;

    public static int aboutTagID;

    public static int commentPageSize;

    public static String siteAccessName;

    @Value("${myblog.site.access.name}")
    public void setSiteAccessName(String siteAccessName) {
        BlogParamConfig.siteAccessName = siteAccessName;
    }

    @Value("${myblog.web.comment.pageSize}")
    public void setCommentPageSize(int commentPageSize) {
        BlogParamConfig.commentPageSize = commentPageSize;
    }

    @Value("${myblog.mysql.aboutTagID}")
    public void setAboutTagID(int aboutTagID) {
        BlogParamConfig.aboutTagID = aboutTagID;
    }

    @Value("${myblog.web.carouselNum}")
    public void setCarouselNum(int carouselNum) {
        BlogParamConfig.carouselNum = carouselNum;
    }

    @Value("${myblog.web.pageSize}")
    public void setPageSize(int pageSize) {
        BlogParamConfig.pageSize = pageSize;
    }

    // 静态字段直接使用@Value注解无法注入值的
    // 使用@Component创建对象，并且使用set方法+@Value注解注入
    @Value("${myblog.redis.ttl}")
    public void setTtl(int ttl) {
        BlogParamConfig.ttl = ttl;
    }

    @Value("${myblog.markdown_base}")
    public void setMarkdown_base(String markdown_base) {
        BlogParamConfig.markdown_base = markdown_base;
    }
}
