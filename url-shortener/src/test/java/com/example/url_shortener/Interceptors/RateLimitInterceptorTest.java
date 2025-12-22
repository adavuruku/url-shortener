package com.example.url_shortener.Interceptors;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 1:01 AM
 **/
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.url_shortener.Config.IpRateLimiterStorage;
import com.example.url_shortener.Exceptions.RateLimitExceedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RateLimitInterceptorTest {

    @Mock
    private IpRateLimiterStorage rateLimiterStorage;

    @Mock
    private Bucket bucket;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private RateLimitInterceptor interceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptor = new RateLimitInterceptor(rateLimiterStorage);
    }

    @Test
    void shouldAllowNonPostRequests() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verifyNoInteractions(rateLimiterStorage);
    }

    @Test
    void shouldAllowRequestsToOtherPaths() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/other");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verifyNoInteractions(rateLimiterStorage);
    }

    @Test
    void shouldAllowRequestWhenRateLimitNotExceeded() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/urls");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        when(rateLimiterStorage.resolveBucket("127.0.0.1"))
                .thenReturn(bucket);
        when(bucket.tryConsume(1)).thenReturn(true);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(bucket).tryConsume(1);
    }

    @Test
    void shouldThrowExceptionWhenRateLimitExceeded() {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/urls");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        when(rateLimiterStorage.resolveBucket("127.0.0.1"))
                .thenReturn(bucket);
        when(bucket.tryConsume(1)).thenReturn(false);

        assertThrows(
                RateLimitExceedException.class,
                () -> interceptor.preHandle(request, response, new Object())
        );
    }
}

