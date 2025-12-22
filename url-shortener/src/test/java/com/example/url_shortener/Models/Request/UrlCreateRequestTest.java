package com.example.url_shortener.Models.Request;

import com.example.url_shortener.Models.Response.UrlCreateResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 12:53 AM
 **/
class UrlCreateRequestTest {

    @Test
    void recordHoldsValues() {
        UrlCreateRequest r = new UrlCreateRequest( "http://example.com");
        assertEquals("http://example.com", r.longUrl());
    }

    @Test
    void shouldSupportEqualityAndHashCode() {

        UrlCreateRequest r1 = new UrlCreateRequest( "http://example.com");
        UrlCreateRequest r2 = new UrlCreateRequest( "http://example.com");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldSupportEqualityAndHashCode_NotEqual() {

        UrlCreateRequest r1 = new UrlCreateRequest( "http://example.com");
        UrlCreateRequest r2 = new UrlCreateRequest( "http://example.com/sherif");

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }
}
