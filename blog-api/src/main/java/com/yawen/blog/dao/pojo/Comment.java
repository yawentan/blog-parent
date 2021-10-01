package com.yawen.blog.dao.pojo;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private String content;
    private Long createDate;
    private Long articleId;
    private Long level;
    private Long authorId;
    private Long parentId;
    private Long toUid;
}
