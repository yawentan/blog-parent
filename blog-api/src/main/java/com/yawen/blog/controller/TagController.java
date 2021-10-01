package com.yawen.blog.controller;

import com.yawen.blog.service.TagService;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result hot(){
        int limit = 5;
        List<TagVo> tagVo = tagService.getHotTags(limit);
        return Result.success(tagVo);
    }
    @GetMapping
    public Result getAllTag(){
        return tagService.getAllTag();
    }
    @GetMapping("detail")
    public Result getAllTagDetail(){
        return tagService.getAllTag();
    }
    @GetMapping("detail/{id}")
    public Result getAllTagDetailById(@PathVariable Long id){
        return tagService.getAllTagDetailById(id);
    }
}
