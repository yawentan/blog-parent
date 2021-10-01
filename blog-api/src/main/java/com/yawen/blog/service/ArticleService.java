package com.yawen.blog.service;

import com.yawen.blog.vo.ArticleVo;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.params.PageParams;
import com.yawen.blog.vo.params.PublishParam;

public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticlePage(PageParams pageParams);

    Result getHotArticlesList(int limit);

    Result getNewArticlesList(int limit);

    Result getListArchives();

    Result getArticleBodyById(Long id);

    Result publish(PublishParam publishParam);
}
