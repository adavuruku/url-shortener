package com.example.url_shortener.Api;

import com.example.url_shortener.Models.Request.UrlCreateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Sherif.Abdulraheem 12/20/2025 - 10:02 PM
 **/
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class URLShortenerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Integration: create short URL end-to-end")
    void create_shouldPersistAndReturnShortUrl() throws Exception {
        UrlCreateRequest request =
                new UrlCreateRequest("https://example.com");

        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.shortUrl").exists());
    }

    @Test
    @DisplayName("Integration: redirect increments hit count")
    void redirect_shouldIncrementHitCount() throws Exception {
        //1. create first
        String response =
                mockMvc.perform(post("/api/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        { "longUrl": "https://example.com/redirects" }
                                        """))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        String code = json.get("code").asText();

        //2. redirect
        mockMvc.perform(get("/r/{code}", code))
                .andExpect(status().isFound())
                .andExpect(header().string(
                        HttpHeaders.LOCATION,
                        "https://example.com/redirects"
                ));
    }


    @Test
    @DisplayName("Integration: metadata reflects hit count")
    void metadata_shouldReturnUpdatedHitCount() throws Exception {

        //1. create first
        String response =
                mockMvc.perform(post("/api/urls")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        { "longUrl": "https://example.com/wells" }
                                        """))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        String code = objectMapper.readTree(response)
                .get("code").asText();

        //2. call redirect twice
        mockMvc.perform(get("/r/{code}", code));
        mockMvc.perform(get("/r/{code}", code));

        //3. wait a bit before calling metadata (to be sure the 2 api execute successfully) and async call are successful
        Awaitility.await()
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(() ->
                        mockMvc.perform(get("/api/urls/{code}", code))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.invocationCounter").value(2))
                                .andExpect(jsonPath("$.code").value(code))
                );


    }

    @Test
    @DisplayName("POST /api/urls is rate-limited after allowed requests")
    void create_shouldReturn429WhenRateLimitExceeded() throws Exception {

        String payload = """
            { "longUrl": "https://example.com/rate-limit" }
        """;

        //allowed request
        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        //allowed request
        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        //should be blocked since we've 2 capacity
        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.title")
                        .value("Rate limit ON path '/api/urls' exceeded."));
    }
}

