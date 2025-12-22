package com.example.logcli;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 9:35 AM
 **/

import com.example.logcli.Models.LogEntry;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class LogAnalyzer {
    public Map<String, Long> topNEndpoints(
            List<LogEntry> entries,
            Instant since,
            int topN
    ) {
        return entries.parallelStream()
                .filter(e -> e.timestamp().isAfter(since))
                .collect(Collectors.groupingByConcurrent(
                        LogEntry::endpoint,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public int percentile(List<LogEntry> entries, int percentile) {
        List<Integer> sorted =
                entries.parallelStream()
                        .map(LogEntry::responseTime)
                        .sorted()
                        .toList();

        int index = (int) Math.ceil(percentile / 100.0 * sorted.size()) - 1;
        return sorted.get(Math.max(index, 0));
    }
}