package com.example.url_shortener.Models.Response;

import com.example.url_shortener.Data.Entity.UrlMapping;

import java.time.Instant;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 12:56 PM
 **/

public record UrlMetaDataResponse(
        String code,
        Instant creationTime,
        long invocationCounter
) { }
