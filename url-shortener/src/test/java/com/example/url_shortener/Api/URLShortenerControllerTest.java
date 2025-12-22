package com.example.url_shortener.Api;

import com.example.url_shortener.Data.Entity.UrlMapping;
import com.example.url_shortener.Models.Request.UrlCreateRequest;
import com.example.url_shortener.Models.Response.UrlMetaDataResponse;
import com.example.url_shortener.Services.Interface.IUrlMappingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Sherif.Abdulraheem 12/20/2025 - 9:26 PM
 **/
@ExtendWith(MockitoExtension.class)
class URLShortenerControllerTest {


    @Mock
    private IUrlMappingService urlMappingService;
    @InjectMocks
    private URLShortenerController urlShortenerController;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("POST /api/urls returns short code and generated redirect URL")
    void create_shouldReturnCreatedShortUrl() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setScheme("http");
        servletRequest.setServerName("localhost");
        servletRequest.setServerPort(80);

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(servletRequest)
        );

        UrlMapping mapping = new UrlMapping();
        mapping.setCode("abc123");

        when(urlMappingService.create("https://example.com"))
                .thenReturn(mapping);

        UrlCreateRequest request =
                new UrlCreateRequest("https://example.com");

        var res =
                urlShortenerController.create(request);

        assertEquals("abc123", res.code());
        assertNotNull(res.shortUrl());
        assertTrue(res.shortUrl().endsWith("/r/abc123"));
    }


    @Test
    @DisplayName("GET /api/urls/{code} returns URL metadata")
    void metadata_shouldReturnMetadata() {
        Instant now = Instant.now();

        UrlMapping mapping = new UrlMapping();
        mapping.setCode("abc");
        mapping.setCreatedAt(now);
        mapping.setHitCount(5L);

        when(urlMappingService.generateUrlMetaData("abc"))
                .thenReturn(mapping);

        UrlMetaDataResponse res =
                urlShortenerController.metadata("abc");

        assertEquals("abc", res.code());
        assertEquals(mapping.getCreatedAt(), res.creationTime());
        assertEquals(mapping.getHitCount(), res.invocationCounter());
        assertEquals(mapping.getCode(), res.code());
    }


    @Test
    @DisplayName("GET /r/{code} returns 302 redirect with Location header")
    void redirect_shouldReturnRedirectResponse() {
        when(urlMappingService.resolveLongUrl("abc"))
                .thenReturn("https://example.com");

        ResponseEntity<Void> response =
                urlShortenerController.redirect("abc");

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(
                URI.create("https://example.com"),
                response.getHeaders().getLocation()
        );

        verify(urlMappingService).incrementHitCount("abc");
    }
}
