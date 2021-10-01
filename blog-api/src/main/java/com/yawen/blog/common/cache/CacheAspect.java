package com.yawen.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.yawen.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 配置切面
 */
@Aspect
@Component
@Slf4j
public class CacheAspect {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //配置切点
    @Pointcut("@annotation(com.yawen.blog.common.cache.Cache)")
    public void pt(){}

    //环绕通知
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp){
        try{
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            //调用的方法名
            String methodName = signature.getName();


            Class[] parameterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();

            //参数
            String params="";
            for(int i=0;i<args.length;i++){
                if(args[i]!=null){
                    params+= JSON.toJSONString(args[i]);
                    parameterTypes[i]=args[i].getClass();
                }else{
                    parameterTypes[i]=null;
                }
            }

            //加密
            if(StringUtils.isNoneEmpty(params)){
                params = DigestUtils.md5Hex(params);
            }
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName,parameterTypes);
            //获得Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            //缓存过期时间
            long expire = annotation.expire();
            //缓存名称
            String name = annotation.name();
            //先从redis获取
            String redisKey = name+"::"+className+"::"+methodName+"::"+params;
            String s = redisTemplate.opsForValue().get(redisKey);
            if(StringUtils.isNotEmpty(s)){
                log.info("走了缓存~~~,{},{}",className,methodName);
                return JSON.parseObject(s,Result.class);
            }
            //运行程序
            Object proceed = pjp.proceed();
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存~~~{},{}",className,methodName);
            return proceed;
        }catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999,"系统错误");
    }
}
