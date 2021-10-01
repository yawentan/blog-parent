package com.yawen.blog.dao.pojo;

import com.yawen.blog.vo.ArticleBodyVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
public class Article {
    private static final int ARTICLE_TOP = 1;
    private static final int ARTICLE_COMMON = 0;

    private Long id;
    private String title;
    private String summary;
    private int commentCounts;
    private int viewCounts;
    /**
     * 作者id
     */
    private long authorId;
    /**
     * 内容id
     */
    private long bodyId;
    /**
     * 类别id
     */
    private long categoryId;
    /**
     * 创建时间
     */
    private Long createDate;
    /**
     * 是否置顶
     */
    private int weight = ARTICLE_COMMON;
}
