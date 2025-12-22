package com.example.logcli.Models;

import java.time.Instant;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 3:50 PM
 **/
public record CliArgs(String file, int topN, Instant since) {

    public static CliArgs parse(String[] args) {
        String file = null;
        int topN = 5;
        Instant since = Instant.EPOCH;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--file" -> file = next(args, ++i, "--file");
                case "--topN" -> topN = Integer.parseInt(next(args, ++i, "--topN"));
                case "--since" -> since = Instant.parse(next(args, ++i, "--since"));
                default -> throw new IllegalArgumentException("Unknown option: " + args[i]);
            }
        }

        if (file == null) {
            throw new IllegalArgumentException(
                    "Usage: --file <csv> [--topN N] [--since ISO-8601]"
            );
        }

        if (topN <= 0) {
            throw new IllegalArgumentException("topN must be > 0");
        }

        return new CliArgs(file, topN, since);
    }

    private static String next(String[] args, int index, String flag) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Missing value for " + flag);
        }
        return args[index];
    }
}
