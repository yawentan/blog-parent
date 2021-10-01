package com.yawen.blog.service;

import com.yawen.blog.dao.pojo.Category;
import com.yawen.blog.vo.Result;

public interface CategoryService {
    Category findCategoryById(long categoryId);

    Result getAllCategory();

    Result getCategoryById(Long id);
}
