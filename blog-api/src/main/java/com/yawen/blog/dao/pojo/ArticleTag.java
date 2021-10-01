package com.yawen.blog.dao.pojo;

import lombok.Data;

@Data
public class ArticleTag {
    private Long id;
    private long articleId;
    private long tagId;
}
