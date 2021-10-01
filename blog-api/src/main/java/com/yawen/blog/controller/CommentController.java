package com.yawen.blog.controller;

import com.yawen.blog.service.CommentService;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("article/{id}")
    public Result getCommentsById(@PathVariable long id){
        return commentService.getCommentsById(id);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }
}
