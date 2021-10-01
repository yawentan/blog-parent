package com.yawen.blog.vo.params;

import com.yawen.blog.dao.pojo.Category;
import com.yawen.blog.vo.CategoryVo;
import com.yawen.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class PublishParam {
    private String title;
    private long id;
    private ArticleBodyParam body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;
}
