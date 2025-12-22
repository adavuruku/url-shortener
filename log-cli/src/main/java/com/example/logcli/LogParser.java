package com.example.logcli;

import com.example.logcli.Models.LogEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 9:35 AM
 **/
public class LogParser {
    public List<LogEntry> parse(Path file) throws IOException {
        return Files.lines(file)
                .skip(1)
                .map(line -> line.split(","))
                .map(p -> {
                    if (p.length < 4) {
                        throw new IllegalArgumentException("Invalid CSV line");
                    }
                    return new LogEntry(
                            Instant.parse(p[0].trim()),
                            p[1].trim(),
                            Integer.parseInt(p[2].trim()),
                            Integer.parseInt(p[3].trim()));
                })
                .toList();
    }
}
