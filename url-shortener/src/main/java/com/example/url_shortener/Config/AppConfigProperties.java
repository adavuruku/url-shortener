package com.example.url_shortener.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 2:39 PM
 **/
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class AppConfigProperties {

    private ShortUrl shortUrl;
    private ThreadConfig threadConfig;
    private CacheConfig cacheConfig;
    private RateLimitConfig rateLimitConfig;

    public RateLimitConfig getRateLimitConfig() {
        return rateLimitConfig;
    }

    public void setRateLimitConfig(RateLimitConfig rateLimitConfig) {
        this.rateLimitConfig = rateLimitConfig;
    }

    public ShortUrl getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(ShortUrl shortUrl) {
        this.shortUrl = shortUrl;
    }


    public ThreadConfig getThreadConfig() {
        return threadConfig;
    }

    public void setThreadConfig(ThreadConfig threadConfig) {
        this.threadConfig = threadConfig;
    }

    public CacheConfig getCacheConfig() {
        return cacheConfig;
    }

    public void setCacheConfig(CacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public static class ShortUrl {
        private long expiryMinutes;
        private long maxCreateRetry;

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
        private long ttlMinutes;

        public long getTtlMinutes() {
            return ttlMinutes;
        }

        public void setTtlMinutes(long ttlMinutes) {
            this.ttlMinutes = ttlMinutes;
        }
    }

    public static class RateLimitConfig {

        private long limitPeriodMinutes;
        private long limitToken;
        private long limitCapacity;

        public long getLimitPeriodMinutes() {
            return limitPeriodMinutes;
        }

        public void setLimitPeriodMinutes(long limitPeriodMinutes) {
            this.limitPeriodMinutes = limitPeriodMinutes;
        }

        public long getLimitToken() {
            return limitToken;
        }

        public void setLimitToken(long limitToken) {
            this.limitToken = limitToken;
        }

        public long getLimitCapacity() {
            return limitCapacity;
        }

        public void setLimitCapacity(long limitCapacity) {
            this.limitCapacity = limitCapacity;
        }




    }
}
