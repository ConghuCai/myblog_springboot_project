package com.gitee.conghucai.blog.service.impl;

import com.gitee.conghucai.blog.config.BlogParamConfig;
import com.gitee.conghucai.blog.mapper.ArticleMapper;
import com.gitee.conghucai.blog.mapper.CommentMapper;
import com.gitee.conghucai.blog.model.Article;
import com.gitee.conghucai.blog.model.Comment;
import com.gitee.conghucai.blog.model.User;
import com.gitee.conghucai.blog.service.CommentService;
import com.gitee.conghucai.blog.service.UserService;
import com.gitee.conghucai.blog.utils.DateTimeUtil;
import com.gitee.conghucai.blog.utils.UUIDUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    private int pageSize = BlogParamConfig.commentPageSize;

    @Resource
    CommentMapper commentMapper;
    @Resource
    ArticleMapper articleMapper;
    @Resource
    UserService userService;

    @Override
    public Map<String, Object> getCommentList(String articleID) {
        List<Map> comments = commentMapper.selectInfoByArticleID(articleID);
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("comments", comments);
        data.put("pageSize", pageSize);

        res.put("data", data);
        res.put("code", 200);
        return res;
    }

    @Transactional
    @Override
    public Map<String, Object> addComment(String articleID, String content, String userLogin) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        Article article = articleMapper.selectByPrimaryKey(articleID);

        if(article == null){
            data.put("res", "failed. can't find article.");
            res.put("data", data);
            res.put("code", 500);
            return res;
        }

        User user = userService.getUserByLogin(userLogin);
        if(user == null){
            data.put("res", "failed. can't find user.");
            res.put("data", data);
            res.put("code", 500);
            return res;
        }

        Comment comment = new Comment();
        comment.setId(UUIDUtil.getUUID());
        comment.setArticleId(articleID);
        comment.setContent(content);
        comment.setUserLogin(userLogin);
        comment.setTime(DateTimeUtil.getSysTime());

        int commentCount = article.getCommentCount();
        Article articleUpdate = new Article();
        articleUpdate.setId(articleID);
        articleUpdate.setCommentCount(commentCount + 1);

        try{
            commentTransaction(comment, articleUpdate);
        } catch (Exception e){
            data.put("res", "insert Exception: " + e.getMessage());
            res.put("data", data);
            res.put("code", 500);
            return res;
        }

        data.put("res", "success");
        res.put("data", data);
        res.put("code", 500);
        return res;
    }


    //在这里进行事务操作  插入评论同时  文章评论字段+1
    @Transactional
    @Override
    public void commentTransaction(Comment comment, Article article) throws RuntimeException{
        int res1 = 0, res2 = 0;
        try{
            res1 = commentMapper.insert(comment);
            res2 = articleMapper.updateByPrimaryKeySelective(article);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        if(res1 == 0 || res2 == 0){
            throw new RuntimeException("更改文章或评论信息失败！");
        }
    }

}
