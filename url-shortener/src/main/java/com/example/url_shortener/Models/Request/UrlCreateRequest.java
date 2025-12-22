package com.example.url_shortener.Models.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 12:56 PM
 **/

public record UrlCreateRequest(
        @NotBlank
        @Size(max = 2048)
        @URL
        String longUrl
) {}
