package com.example.logcli.Models;

import java.time.Instant;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 9:36 AM
 **/
public record LogEntry(
        Instant timestamp,
        String endpoint,
        int responseTime,
        int status
) {}
