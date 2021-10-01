package com.yawen.blog.controller;

import com.yawen.blog.service.CategoryService;
import com.yawen.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result getCategory(){
        return categoryService.getAllCategory();
    }

    @GetMapping("detail")
    public Result getCategoryDetail(){
        return categoryService.getAllCategory();
    }

    @GetMapping("detail/{id}")
    public Result getCategoryDetailId(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
}
