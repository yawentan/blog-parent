package com.yawen.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yawen.blog.dao.pojo.Tag;

import java.util.List;

/**
 * Tag与数据库建立连接
 */
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findTagsByArticleId(long articleId);

    List<Long> getHotTagsId(int limit);

    List<Tag> getHotTags(List<Long> tagsId);
}
