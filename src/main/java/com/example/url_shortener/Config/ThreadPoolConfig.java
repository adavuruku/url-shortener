package com.example.url_shortener.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 4:53 PM
 **/
public class ThreadPoolConfig {

    private final ShortUrlProperties shortUrlProperties;

    public ThreadPoolConfig(ShortUrlProperties shortUrlProperties) {
        this.shortUrlProperties = shortUrlProperties;
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize((int)shortUrlProperties.getThreadConfig().getCorePoolSize());
        executor.setMaxPoolSize((int) shortUrlProperties.getThreadConfig().getMaxPoolSize());
        executor.setQueueCapacity((int)shortUrlProperties.getThreadConfig().getQueCapacity());
        executor.setThreadNamePrefix("async-delete-");
        executor.initialize();
        return executor;
    }
}
