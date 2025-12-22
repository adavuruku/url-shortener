package com.example.url_shortener.Models.Response;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 12:53 AM
 **/
class UrlMetaDataResponseTest {

    @Test
    void recordHoldsValues() {
        Instant now = Instant.now();
        UrlMetaDataResponse r = new UrlMetaDataResponse("c1", now, 5L);

        assertEquals("c1", r.code());
        assertEquals(now, r.creationTime());
        assertEquals(5L, r.invocationCounter());
    }

    @Test
    void shouldSupportEqualityAndHashCode() {
        Instant time = Instant.parse("2025-01-01T00:00:00Z");

        UrlMetaDataResponse r1 =
                new UrlMetaDataResponse("xyz", time, 10L);
        UrlMetaDataResponse r2 =
                new UrlMetaDataResponse("xyz", time, 10L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
