package com.yawen.blog.controller;

import com.yawen.blog.dao.pojo.SysUser;
import com.yawen.blog.utils.UserThreadLocal;
import com.yawen.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
