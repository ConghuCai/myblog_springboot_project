package com.gitee.conghucai.blog.service;


import com.gitee.conghucai.blog.model.Article;
import com.gitee.conghucai.blog.model.Comment;

import java.util.Map;

public interface CommentService {

    Map<String, Object> getCommentList(String articleID);

    Map<String, Object> addComment(String articleID, String content, String userLogin);

    void commentTransaction(Comment comment, Article article);
}
