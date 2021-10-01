package com.yawen.blog.service;

import com.yawen.blog.dao.pojo.SysUser;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.params.LoginParam;

public interface LoginService {

    public Result login(LoginParam loginParam);

    public Result logout(String token);

    public Result register(LoginParam loginParam);

    public SysUser checkToken(String token);
}
