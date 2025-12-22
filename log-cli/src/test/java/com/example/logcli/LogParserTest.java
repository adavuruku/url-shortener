package com.example.logcli;

import com.example.logcli.Models.LogEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 3:30 PM
 **/
class LogParserTest {
    @Test
    void parse_shouldParseValidCsv(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("access.csv");

        Files.writeString(file, """
          timestamp,endpoint,responseTime,status
          2025-12-20T08:15:30Z,/login,120,200
          2025-12-20T08:16:05Z,/home,50,200
          2025-12-20T08:17:10Z,/api/data,200,500
          2025-12-20T08:17:45Z,/login,110,200
          2025-12-20T08:18:00Z,/profile,80,200
          2025-12-20T08:18:30Z,/api/data,190,200
        """);

        LogParser parser = new LogParser();
        List<LogEntry> result = parser.parse(file);

        assertEquals(6, result.size());

        LogEntry first = result.get(0);
        assertEquals(Instant.parse("2025-12-20T08:15:30Z"), first.timestamp());
        assertEquals("/login", first.endpoint());
        assertEquals(200, first.status());
        assertEquals(120, first.responseTime());
    }

    @Test
    void parse_shouldReturnEmptyList_whenOnlyHeader(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.csv");

        Files.writeString(file, "timestamp,endpoint,responseTime,status\n");

        LogParser parser = new LogParser();
        List<LogEntry> result = parser.parse(file);

        assertTrue(result.isEmpty());
    }

    @Test
    void parse_shouldFail_whenTimestampInvalid(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("bad.csv");

        Files.writeString(file, """
        timestamp,endpoint,responseTime,status
        not-a-timestamp,/login,120,200
        """);

        LogParser parser = new LogParser();

        assertThrows(Exception.class, () -> parser.parse(file));
    }

    @Test
    void parse_shouldFail_whenStatusNotNumber(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("bad.csv");

        Files.writeString(file, """
        timestamp,endpoint,responseTime,status
        2025-12-20T08:15:30Z,/login,200,sherif
        """);

        LogParser parser = new LogParser();

        assertThrows(NumberFormatException.class, () -> parser.parse(file));
    }


    @Test
    void parse_shouldThrowIllegalArgumentException_whenCsvLineHasLessThanFourColumns(
            @TempDir Path tempDir
    ) throws IOException {

        Path file = tempDir.resolve("invalid.csv");

        Files.writeString(file, """
        timestamp,endpoint,responseTime
        2025-12-20T08:15:30Z,/login,200
        """);

        LogParser parser = new LogParser();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(file)
        );

        assertEquals("Invalid CSV line", ex.getMessage());
    }

    @Test
    void parse_shouldThrowIOException_whenFileMissing() {
        LogParser parser = new LogParser();

        assertThrows(IOException.class,
                () -> parser.parse(Path.of("does-not-exist.csv")));
    }

}
