package com.yawen.blog.controller;

import com.yawen.blog.common.aop.LogAnnotation;
import com.yawen.blog.common.cache.Cache;
import com.yawen.blog.service.ArticleService;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.params.PageParams;
import com.yawen.blog.vo.params.PublishParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据交互
@RestController
//与对应的地址进行交互
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    //@Cache(expire = 5*60*1000,name="hot_article")
    @LogAnnotation(module = "文章",operation="获取文章列表")
    public Result listArticle(@RequestBody PageParams pageParams){
        Result result = articleService.listArticlePage(pageParams);
        return result;
    }

    /**
     * 热门文章
     * @return
     */
    @PostMapping("/hot")
    public Result hot(){
        int limit =  5;
        Result result = articleService.getHotArticlesList(limit);
        return result;
    }
    /**
     * 最新文章
     * @return
     */
    @PostMapping("/new")
    public Result getNewArticles(){
        int limit =  5;
        Result result = articleService.getNewArticlesList(limit);
        return result;
    }
    @PostMapping("listArchives")
    public Result getListArchives(){
        return articleService.getListArchives();
    }


    @PostMapping("view/{id}")
    public Result getArticleBodyById(@PathVariable("id") Long id){
        return articleService.getArticleBodyById(id);
    }
    @PostMapping("publish")
    public Result publish(@RequestBody PublishParam publishParam){
        return articleService.publish(publishParam);
    }
}
