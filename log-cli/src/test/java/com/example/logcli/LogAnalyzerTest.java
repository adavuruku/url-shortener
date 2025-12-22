package com.example.logcli;

import com.example.logcli.Models.LogEntry;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 9:39 AM
 **/
class LogAnalyzerTest {
    @Test
    void percentileCalculationWorks() {
        LogAnalyzer analyzer = new LogAnalyzer();

        List<LogEntry> entries = List.of(
                new LogEntry(Instant.now(), "/a", 100, 200),
                new LogEntry(Instant.now(), "/a", 200, 200),
                new LogEntry(Instant.now(), "/b", 300, 200),
                new LogEntry(Instant.now(), "/b", 400, 200)
        );

        assertEquals(400, analyzer.percentile(entries, 99));
        assertEquals(300, analyzer.percentile(entries, 75));
    }
}
