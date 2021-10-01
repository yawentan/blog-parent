package com.yawen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yawen.blog.dao.mapper.CategoryMapper;
import com.yawen.blog.dao.pojo.ArticleBody;
import com.yawen.blog.dao.pojo.Category;
import com.yawen.blog.service.CategoryService;
import com.yawen.blog.vo.CategoryVo;
import com.yawen.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result getCategoryById(Long id) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId,id);

        Category category = categoryMapper.selectOne(queryWrapper);
        CategoryVo categoryVo = copy(category);
        return Result.success(categoryVo);
    }

    @Override
    public Result getAllCategory() {
        List<Category> categoryList = categoryMapper.selectList(null);
        List<CategoryVo> categoryVoList = copyList(categoryList);
        return Result.success(categoryVoList);
    }

    @Override
    public Category findCategoryById(long categoryId) {
        //2.通过categoryId获得category
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId,categoryId);
        Category category = categoryMapper.selectOne(queryWrapper);
        return category;
    }

    private List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> categoryVoList = new LinkedList<>();
        for(Category category:categoryList){
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    private CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
