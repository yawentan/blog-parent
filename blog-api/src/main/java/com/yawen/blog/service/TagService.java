package com.yawen.blog.service;

import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(long authorId);

    List<TagVo> getHotTags(int limit);

    Result getAllTag();

    Result getAllTagDetailById(Long id);
}
