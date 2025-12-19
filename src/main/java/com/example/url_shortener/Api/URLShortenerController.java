package com.example.url_shortener.Api;

import com.example.url_shortener.Data.Entity.UrlMapping;
import com.example.url_shortener.Models.Request.UrlCreateRequest;
import com.example.url_shortener.Models.Response.UrlCreateResponse;
import com.example.url_shortener.Models.Response.UrlMetaDataResponse;
import com.example.url_shortener.Services.Interface.IUrlMappingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 12:04 PM
 **/
@RestController
public class URLShortenerController {
//    MIGRATION_DB="Host=localhost;Port=5432;Database=rise_mig;Username=postgres;Password=sherif"
//    REDIS="127.0.0.1:6379,ssl=False,abortConnect=False,connectRetry=5,connectTimeout=5000"
//    REDIS_KEY_PREFIX=indtech:EMR:


    private final IUrlMappingService urlMappingService;

    public URLShortenerController(IUrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    @PostMapping("/api/urls")
    @ResponseStatus(HttpStatus.CREATED)
    public UrlCreateResponse create(@Valid @RequestBody UrlCreateRequest req) {
        UrlMapping m = urlMappingService.create(req.longUrl());
        String shortUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/r/{code}")
                .buildAndExpand(m.getCode())
                .toUriString();

        return new UrlCreateResponse(
                m.getCode(),
                shortUrl
        );
    }

    @GetMapping("/api/urls/{code}")
    @ResponseStatus(HttpStatus.OK)
    public UrlMetaDataResponse metadata(@PathVariable String code) {
        UrlMapping m = urlMappingService.generateUrlMetaData(code);
        return new UrlMetaDataResponse(m.getCode(), m.getCreatedAt(), m.getHitCount());
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String longUrl = urlMappingService.resolveLongUrl(code);
        urlMappingService.incrementHitCount(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
