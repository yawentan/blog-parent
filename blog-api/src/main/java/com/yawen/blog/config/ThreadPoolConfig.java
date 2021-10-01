package com.yawen.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("taskExecutor")
    public Executor asyncServiceExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(5);
        //设置最大线程数目
        executor.setMaxPoolSize(20);
        //设置缓存队列大小
        executor.setQueueCapacity(Integer.MAX_VALUE);
        //设置线程活跃时间
        executor.setKeepAliveSeconds(60);
        //设置线程名称
        executor.setThreadNamePrefix("yawen blog");
        //等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.initialize();
        return executor;

    }
}
