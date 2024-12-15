package com.ela.ccvoice.common.common.config;

import com.ela.ccvoice.common.common.factory.MyThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {
    /**
     * 项目共用线程池
     */
    public static final String CCVOICE_EXECUTOR = "ccvoiceExecutor";
    /**
     * websocket通信线程池
     */
    public static final String WS_EXECUTOR = "websocketExecutor";

    @Override
    public Executor getAsyncExecutor() {
        return ccvoiceExecutor();
    }

    @Bean(CCVOICE_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor ccvoiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setWaitForTasksToCompleteOnShutdown(true); // 等待所有任务都完成再关闭线程池 优雅停机 默认值是false
        executor.setCorePoolSize(10); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(200);// 队列最大长度
        executor.setThreadNamePrefix("ccvoice-executor-"); // 线程前缀名
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//满了调用线程执行，认为重要任务
        executor.setThreadFactory(new MyThreadFactory(executor));
        executor.initialize();
        return executor;
    }
    //在项目中使用这个线程池的方法
//    @Autowired
//    @Qualifier(ThreadPoolConfig.CCVOICE_EXECUTOR)
//    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
}