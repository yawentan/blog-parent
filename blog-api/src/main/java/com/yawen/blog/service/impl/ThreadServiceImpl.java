package com.yawen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yawen.blog.dao.mapper.ArticleMapper;
import com.yawen.blog.dao.pojo.Article;
import com.yawen.blog.service.ThreadService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThreadServiceImpl implements ThreadService {
    @Override
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        //我这样写增加了SQL更新的面积
        article.setViewCounts(article.getViewCounts()+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        //通过set操作能做到sql语句中的 update table_name set =? where eq.
        updateWrapper.set(Article::getViewCounts,article.getViewCounts());
        updateWrapper.eq(Article::getId,article.getId());
        articleMapper.update(null,updateWrapper);
        System.out.println("========================更新完成==================");


        //这种方式需要把Article参数的默认值修改为0
//        int viewCounts = article.getViewCounts();
//        Article articleUpdate = new Article();
//        articleUpdate.setViewCounts(viewCounts+1);
//        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(Article::getId,article.getId());
//        updateWrapper.eq(Article::getViewCounts,viewCounts);
//        articleMapper.update(articleUpdate,updateWrapper);
//        try {
//            Thread.sleep(5000);
//            System.out.println("更新完成");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
