package com.gitee.conghucai.blog.model;

import java.io.Serializable;

public class AboutInfo implements Serializable {
    private String id;

    private String intro;

    private String urlGitee;

    private String urlGithub;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUrlGitee() {
        return urlGitee;
    }

    public void setUrlGitee(String urlGitee) {
        this.urlGitee = urlGitee;
    }

    public String getUrlGithub() {
        return urlGithub;
    }

    public void setUrlGithub(String urlGithub) {
        this.urlGithub = urlGithub;
    }
}