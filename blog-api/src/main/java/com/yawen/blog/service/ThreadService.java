package com.yawen.blog.service;

import com.yawen.blog.dao.mapper.ArticleMapper;
import com.yawen.blog.dao.pojo.Article;

public interface ThreadService {

    void updateArticleViewCount(ArticleMapper articleMapper, Article article);
}
