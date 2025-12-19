package com.example.url_shortener.Api;

import com.example.url_shortener.Data.Entity.UrlMapping;
import com.example.url_shortener.Models.Request.UrlCreateRequest;
import com.example.url_shortener.Models.Response.UrlCreateResponse;
import com.example.url_shortener.Models.Response.UrlMetaDataResponse;
import com.example.url_shortener.Services.Interface.IUrlMappingService;
import com.example.url_shortener.openapidoc.CreateShortUrlApiDoc;
import com.example.url_shortener.openapidoc.UrlMetaDataApiDoc;
import com.example.url_shortener.openapidoc.UrlRedirectApiDoc;
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

    private final IUrlMappingService urlMappingService;

    public URLShortenerController(IUrlMappingService urlMappingService) {
        this.urlMappingService = urlMappingService;
    }

    @PostMapping("/api/urls")
    @ResponseStatus(HttpStatus.CREATED)
    @CreateShortUrlApiDoc
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
    @UrlMetaDataApiDoc
    public UrlMetaDataResponse metadata(@PathVariable String code) {
        UrlMapping m = urlMappingService.generateUrlMetaData(code);
        return new UrlMetaDataResponse(m.getCode(), m.getCreatedAt(), m.getHitCount());
    }



    @GetMapping("/r/{code}")
    @ResponseStatus(HttpStatus.FOUND)
    @UrlRedirectApiDoc
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String longUrl = urlMappingService.resolveLongUrl(code);
        urlMappingService.incrementHitCount(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
