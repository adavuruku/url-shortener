package com.example.logcli.Models;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 3:56 PM
 **/
class LogEntryTest {
    @Test
    void recordHoldsValues() {
        Instant now = Instant.now();
        LogEntry r = new LogEntry(now, "/login", 23, 200 );
        assertEquals(now, r.timestamp());
        assertEquals("/login", r.endpoint());
        assertEquals(23, r.responseTime());
        assertEquals(200, r.status());
    }

    @Test
    void shouldSupportEqualityAndHashCode() {

        Instant now = Instant.now();
        LogEntry r1 = new LogEntry(now, "/login", 23, 200 );
        LogEntry r2 = new LogEntry(now, "/login", 23, 200 );

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldSupportEqualityAndHashCode_NotEqual() {

        Instant now = Instant.now();
        LogEntry r1 = new LogEntry(now, "/login", 23, 200 );
        LogEntry r2 = new LogEntry(now, "/login/password", 23, 200 );

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }
}
