package com.zsheep.ai.config.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadConfig {
    private static final Integer corePoolSize = 20;
    private static final Integer maxSize = 200;
    private static final Integer keepAliveTime = 10;
    public static final int BLOCKING_QUEUE_CAPACITY = 100000;
    
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(corePoolSize,
                maxSize,
                keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(BLOCKING_QUEUE_CAPACITY),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
    
    
    @Bean(name = "singleThreadPoolExecutor")
    public ExecutorService singleThreadPoolExecutor(LinkedBlockingQueue<Runnable> taskQueue) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, taskQueue);
    }
}
