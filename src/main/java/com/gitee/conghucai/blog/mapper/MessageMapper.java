package com.gitee.conghucai.blog.mapper;

import com.gitee.conghucai.blog.model.Message;
import com.gitee.conghucai.blog.vo.MessageDetailVo;

import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(String id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<MessageDetailVo> getMessageDetail();
}