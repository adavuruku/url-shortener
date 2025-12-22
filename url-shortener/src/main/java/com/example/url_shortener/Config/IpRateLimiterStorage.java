package com.example.url_shortener.Config;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sherif.Abdulraheem 12/20/2025 - 6:14 PM
 **/
@Component
public class IpRateLimiterStorage {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final Bucket bucketConfig;

    public IpRateLimiterStorage(Bucket bucketConfig) {
        this.bucketConfig = bucketConfig;
    }

    public Bucket resolveBucket(String ip) {
        return buckets.computeIfAbsent(ip, k -> bucketConfig);
    }
}
