package com.example.url_shortener.Services.Interface;

import com.example.url_shortener.Data.Entity.UrlMapping;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 2:23 PM
 **/
public interface IUrlMappingService {
    UrlMapping create(String longUrl);
    String resolveLongUrl(String code);
    void incrementHitCount(String code);
    UrlMapping generateUrlMetaData(String code);
}
