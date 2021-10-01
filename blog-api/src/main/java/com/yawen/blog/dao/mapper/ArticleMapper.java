package com.yawen.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yawen.blog.dao.dos.Archives;
import com.yawen.blog.dao.pojo.Article;
import com.yawen.blog.vo.params.PageParams;

import java.util.List;

/**
 * article与数据库建立连接
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> getListArchives();

    IPage<Article> listArticlePage(Page<Article> page, Long categoryId, Long tagId, String year, String month);
}
