package com.gitee.conghucai.blog.mapper;

import com.gitee.conghucai.blog.model.Category;
import com.gitee.conghucai.blog.vo.CategoryListVo;

import java.util.List;
import java.util.Map;

public interface CategoryMapper {
    int deleteByPrimaryKey(String name);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<CategoryListVo> selectByMenuId(Integer menuId);

    Map selectInfoByName(String name);
}