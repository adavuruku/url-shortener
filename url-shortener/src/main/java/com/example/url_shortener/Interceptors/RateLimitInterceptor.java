package com.example.url_shortener.Interceptors;

import com.example.url_shortener.Config.IpRateLimiterStorage;
import com.example.url_shortener.Exceptions.RateLimitExceedException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * Created by Sherif.Abdulraheem 12/20/2025 - 6:17 PM
 **/
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final IpRateLimiterStorage rateLimiterStorage;

    public RateLimitInterceptor(IpRateLimiterStorage rateLimiterStorage) {
        this.rateLimiterStorage = rateLimiterStorage;
    }
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String path = "/api/urls";
        if (!"POST".equalsIgnoreCase(request.getMethod())
                || !request.getRequestURI().equalsIgnoreCase(path)) {
            return true;
        }

        Bucket bucket = rateLimiterStorage.resolveBucket(request.getRemoteAddr());

        if (!bucket.tryConsume(1)) {
            throw new RateLimitExceedException(
                    new Object[]{request.getRequestURI()}
            );
        }

        return true;
    }
}

