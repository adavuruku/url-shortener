package com.example.url_shortener.Models.Response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 12:53 AM
 **/
class UrlCreateResponseTest {

    @Test
    void recordHoldsValues() {
        UrlCreateResponse r = new UrlCreateResponse("c1", "http://example.com");

        assertEquals("c1", r.code());
        assertEquals("http://example.com", r.shortUrl());
    }

    @Test
    void shouldSupportEqualityAndHashCode() {

        UrlCreateResponse r1 =
                new UrlCreateResponse("xyz", "http://example.com");
        UrlCreateResponse r2 =
                new UrlCreateResponse("xyz", "http://example.com");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldSupportEqualityAndHashCode_NotEqual() {

        UrlCreateResponse r1 =
                new UrlCreateResponse("sherif", "http://example.com");
        UrlCreateResponse r2 =
                new UrlCreateResponse("xyz", "http://example.com");

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }
}
