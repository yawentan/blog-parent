package com.yawen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yawen.blog.dao.mapper.TagMapper;
import com.yawen.blog.dao.pojo.Tag;
import com.yawen.blog.service.TagService;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Result getAllTagDetailById(Long id) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId,id);
        Tag tag = tagMapper.selectOne(queryWrapper);
        TagVo tagVo = copy(tag);
        return Result.success(tagVo);
    }

    @Override
    public Result getAllTag() {
        List<Tag> tagList = tagMapper.selectList(null);
        List<TagVo> tagVoList = copyList(tagList);
        return Result.success(tagVoList);
    }

    @Override
    public List<TagVo> getHotTags(int limit) {
        List<Long> tagsId = tagMapper.getHotTagsId(limit);
        List<Tag> tags = tagMapper.getHotTags(tagsId);
        return copyList(tags);
    }

    /**
     * 通过文章Id找到对应的标签Tags
     * @param articleId:文章Id
     * @return TagVo列表
     */
    @Override
    public List<TagVo> findTagsByArticleId(long articleId) {
        List<Tag> tags =  tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    private List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagsVo = new LinkedList<TagVo>();
        for(Tag tag:tags){
            TagVo tagVo = copy(tag);
            tagsVo.add(tagVo);
        }
        return tagsVo;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
