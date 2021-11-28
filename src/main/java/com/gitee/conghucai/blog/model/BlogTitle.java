package com.gitee.conghucai.blog.model;

import java.io.Serializable;

public class BlogTitle implements Serializable {
    private String id;

    private String blogname;

    private String shortname;

    private String blogavatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlogname() {
        return blogname;
    }

    public void setBlogname(String blogname) {
        this.blogname = blogname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getBlogavatar() {
        return blogavatar;
    }

    public void setBlogavatar(String blogavatar) {
        this.blogavatar = blogavatar;
    }
}