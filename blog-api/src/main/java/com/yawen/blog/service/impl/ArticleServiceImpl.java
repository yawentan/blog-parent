package com.yawen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yawen.blog.dao.dos.Archives;
import com.yawen.blog.dao.mapper.ArticleBodyMapper;
import com.yawen.blog.dao.mapper.ArticleMapper;
import com.yawen.blog.dao.mapper.ArticleTagMapper;
import com.yawen.blog.dao.pojo.*;
import com.yawen.blog.service.*;
import com.yawen.blog.utils.UserThreadLocal;
import com.yawen.blog.vo.*;
import com.yawen.blog.vo.params.PageParams;
import com.yawen.blog.vo.params.PublishParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ThreadService threadService;

    @Override
    public Result getListArchives() {
        List<Archives> archivesList = articleMapper.getListArchives();
        return Result.success(archivesList);
    }

    @Override
    @Transactional
    public Result publish(PublishParam publishParam) {
        SysUser sysUser = UserThreadLocal.get();
        CategoryVo categoryVo = publishParam.getCategory();
        //更新ArticleBody表
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(publishParam.getBody().getContent());
        articleBody.setContentHtml(publishParam.getBody().getContentHtml());
        articleBody.setArticleId(publishParam.getId());
        articleBodyMapper.insert(articleBody);
        //获取bodyId
        Long bodyId = articleBody.getId();
        //更新Article表
        Article article = new Article();
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(publishParam.getSummary());
        article.setTitle(publishParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(0);
        article.setAuthorId(sysUser.getId());
        article.setBodyId(bodyId);
        article.setCategoryId(categoryVo.getId());
        articleMapper.insert(article);
        //更新ArticleTag表
        List<TagVo> tagsVo = publishParam.getTags();
        for(TagVo tagVo:tagsVo){
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tagVo.getId());
            articleTagMapper.insert(articleTag);
        }

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));

        return Result.success(articleVo);
    }

    /**
     * 通过id获取文章内容，标签，分类
     * @param id
     * @return
     */
    @Override
    public Result getArticleBodyById(Long id) {
        //1.通过id获得Article
        Article article = articleMapper.selectById(id);
        //3.返回ArticleBodyVo
        ArticleVo articleVo = copy(article, true, true,true,true);
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    /**
     * 获取最新的文章列表
     * @param limit
     * @return
     */
    @Override
    public Result getNewArticlesList(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //按照时间排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        List<ArticleVo> articleVos = copyList(articles,false,false);
        return Result.success(articleVos);
    }

    /**
     * 查询热门文章，并排序
     * @param limit
     * @return
     */
    @Override
    public Result getHotArticlesList(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //按照view_Count排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> article = articleMapper.selectList(queryWrapper);
        List<ArticleVo> articleVos = copyList(article, false, false);
        return Result.success(articleVos);
    }

//    @Override
//    public Result listArticlePage(PageParams pageParams) {
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        IPage<Article> articleIPage = articleMapper.listArticlePage(
//                         page
//                        ,pageParams.getCategoryId()
//                        ,pageParams.getTagId()
//                        ,pageParams.getYear()
//                        ,pageParams.getMonth());
//        return Result.success(copyList(articleIPage.getRecords(),true,true));
//    }
    /**
     * 根据页码查询文章，返回文章列表
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticlePage(PageParams pageParams) {
        /**
         * 1.分页查询article数据库表
         */
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        Long categoryId = pageParams.getCategoryId();
//        if(categoryId!=null){
//            queryWrapper.eq(Article::getCategoryId,categoryId);
//        }
//        Long tagId = pageParams.getTagId();
//        if(tagId!=null){
//            LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
//            articleTagQuery.select(ArticleTag::getArticleId);
//            articleTagQuery.eq(ArticleTag::getTagId,tagId);
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagQuery);
//            List<Long> articleIdList = new LinkedList<>();
//            for(ArticleTag articleTag:articleTags){
//                articleIdList.add(articleTag.getArticleId());
//            }
//            queryWrapper.in(Article::getId,articleIdList);
//        }

        //是否置顶进行排序和时间排序
        //order by create_date desc
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        //根据条件构造器查询
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        /**
         * 2.将从数据库中查询到的数据转换成页面中显示数据
         */
        List<ArticleVo> articleVoList = copyList(articlePage.getRecords(),true,true);

        return Result.success(articleVoList);
    }

    /**
     * 通过Id找ArticleBody文章内容
     * @param bodyId
     * @return
     */
    private ArticleBody findArticleById(long bodyId) {
        //2.通过ArticleId获得ArticleBody
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getId,bodyId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        return articleBody;
    }
    /****************************************************私有方法********************************************************/
    /****************************************************私有方法********************************************************/
    /****************************************************私有方法********************************************************/
    /****************************************************私有方法********************************************************/
    /**
     * 将数据库中的Article列表转换成ArticleVo
     * @param record
     * @return
     */
    private List<ArticleVo> copyList(List<Article> record,boolean isAuthor,boolean isTag) {
        List<ArticleVo> articleVo = new LinkedList<>();
        for(Article article:record){
            articleVo.add(copy(article,isAuthor,isTag));
        }
        return articleVo;
    }

    private ArticleVo copy(Article article,boolean isAuthor,boolean isTag) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        if(isAuthor){
            long authorId = article.getAuthorId();
            SysUser sysUserServiceByAuthorId = sysUserService.findByAuthorId(authorId);
            articleVo.setAuthor(sysUserServiceByAuthorId.getNickname());
        }
        if(isTag){
            long authorId = article.getAuthorId();
            List<TagVo> tags = tagService.findTagsByArticleId(authorId);
            articleVo.setTags(tags);
        }

        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVo;
    }

    private ArticleVo copy(Article article,boolean isAuthor,boolean isTag,boolean isArticleBody,boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        if(isAuthor){
            long authorId = article.getAuthorId();
            SysUser sysUserServiceByAuthorId = sysUserService.findByAuthorId(authorId);
            articleVo.setAuthor(sysUserServiceByAuthorId.getNickname());
        }
        if(isTag){
            long authorId = article.getAuthorId();
            List<TagVo> tags = tagService.findTagsByArticleId(authorId);
            articleVo.setTags(tags);
        }
        if(isArticleBody){
            ArticleBody articleBody = findArticleById(article.getBodyId());
            ArticleBodyVo articleBodyVo = new ArticleBodyVo();
            articleBodyVo.setContent(articleBody.getContent());
            articleVo.setBody(articleBodyVo);
        }
        if(isCategory){
            Category category = categoryService.findCategoryById(article.getCategoryId());
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category,categoryVo);
            articleVo.setCategory(categoryVo);
        }

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVo;
    }

    private Category findCategory(int categoryId) {
        return categoryService.findCategoryById(categoryId);
    }
}
