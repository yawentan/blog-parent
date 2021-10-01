package com.yawen.blog.common.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志切面
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.yawen.blog.common.aop.LogAnnotation)")
    public void logPointCut(){

    }
    //切点
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时间
        long time = System.currentTimeMillis()-beginTime;
        //保存日志
        recordLog(point,time);
        return result;
    }

    private void recordLog(ProceedingJoinPoint point, long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================================log start======================");
        log.info("module:{}",annotation.module());
        log.info("operation:{}",annotation.operation());
        //请求者方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}",className+"."+methodName+"()");
        //请求的参数
        Object[] args = point.getArgs();
        String s = JSON.toJSONString(args[0]);
        log.info("param:{}",s);

        log.info("execute time:{}ms",time);
        log.info("=====================log end================");

    }
}
