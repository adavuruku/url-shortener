package com.example.url_shortener.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 2:39 PM
 **/
@Configuration
@ConfigurationProperties(prefix = "short-url")
public class ShortUrlProperties {

    private long expiryMinutes;
    private long maxCreateRetry;
    private ThreadConfig threadConfig; //

    public long getMaxCreateRetry() {
        return maxCreateRetry;
    }

    public void setMaxCreateRetry(long maxCreateRetry) {
        this.maxCreateRetry  = maxCreateRetry;
    }

    public long getExpiryMinutes() {
        return expiryMinutes;
    }

    public void setExpiryMinutes(long expiryMinutes) {
        this.expiryMinutes = expiryMinutes;
    }

    public ThreadConfig getThreadConfig() {
        return threadConfig;
    }

    public void setThreadConfig(ThreadConfig threadConfig) {
        this.threadConfig = threadConfig;
    }

    public static class ThreadConfig {
        private long corePoolSize;
        private long maxPoolSize;
        private long queueCapacity;

        public long getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(long maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public long getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(long corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public long getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(long queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class CacheConfig {
        private long corePoolSize;
    }
}
