package com.yawen.blog.service;

import com.yawen.blog.dao.pojo.SysUser;
import com.yawen.blog.vo.Result;

public interface SysUserService {
    public SysUser findByAuthorId(long authorId);

    public SysUser findByAccountPassword(String account, String psw);

    public Result findUserInfoByToken(String token);

    public SysUser findByAuthor(String account);

    public void save(SysUser sysUser);
}
