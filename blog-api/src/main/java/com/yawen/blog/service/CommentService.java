package com.yawen.blog.service;

import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.params.CommentParam;

public interface CommentService {
    public Result getCommentsById(long id);

    public Result comment(CommentParam commentParam);
}
