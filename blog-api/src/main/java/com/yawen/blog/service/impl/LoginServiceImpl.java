package com.yawen.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yawen.blog.dao.pojo.SysUser;
import com.yawen.blog.service.LoginService;
import com.yawen.blog.service.SysUserService;
import com.yawen.blog.utils.JWTUtils;
import com.yawen.blog.vo.ErrorCode;
import com.yawen.blog.vo.Result;
import com.yawen.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private static final String SALT = "mszlu!@#";

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public SysUser checkToken(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);

        if(stringObjectMap==null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    /**
     * 账号注册
     * @param loginParam
     * @return
     */
    @Override
    public Result register(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if(StringUtils.isBlank(account)
                ||StringUtils.isBlank(password)
                ||StringUtils.isBlank(nickname))
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        //查询是否已存在
        SysUser sysUser = sysUserService.findByAuthor(account);
        if(sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+SALT));
        sysUser.setAvatar("static/img/logo.b3a48c0.png");
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
        //token
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }

    /**
     * 账号登出
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    /**
     * 账号登录
     * @param loginParam
     * @return
     */
    @Override
    public Result login(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String psw = DigestUtils.md5Hex(password + SALT);
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)) {
            return Result.fail(100,"账户密码为空");
        }
        SysUser sysUser = sysUserService.findByAccountPassword(account,psw);
        if(sysUser==null) {
            return Result.fail(-1000,"没找到该用户");
        }
        //登录成功，使用JWT生成token，返回token和redis
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    public static void main(String[] args) {

    }
}
