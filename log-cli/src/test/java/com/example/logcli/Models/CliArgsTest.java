package com.example.logcli.Models;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 4:05 PM
 **/
public class CliArgsTest {
    @Test
    void parse_shouldParseAllArguments() {
        CliArgs args = CliArgs.parse(new String[]{
                "--file", "access.csv",
                "--topN", "10",
                "--since", "2025-01-01T00:00:00Z"
        });

        assertEquals("access.csv", args.file());
        assertEquals(10, args.topN());
        assertEquals(Instant.parse("2025-01-01T00:00:00Z"), args.since());
    }

    @Test
    void parse_shouldApplyDefaults_whenOptionalArgsMissing() {
        CliArgs args = CliArgs.parse(new String[]{
                "--file", "access.csv"
        });

        assertEquals("access.csv", args.file());
        assertEquals(5, args.topN());
        assertEquals(Instant.EPOCH, args.since());
    }

    @Test
    void parse_shouldFail_whenFileMissing() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{"--topN", "5"})
        );

        assertTrue(ex.getMessage().contains("Usage"));
    }

    @Test
    void parse_shouldFail_whenTopNZeroOrNegative() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{
                        "--file", "access.csv",
                        "--topN", "0"
                })
        );

        assertEquals("topN must be > 0", ex.getMessage());
    }


    @Test
    void parse_shouldFail_onUnknownOption() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{
                        "--file", "access.csv",
                        "--unknown", "value"
                })
        );

        assertEquals("Unknown option: --unknown", ex.getMessage());
    }

    @Test
    void parse_shouldFail_whenMissingValueForFile() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{"--file"})
        );

        assertEquals("Missing value for --file", ex.getMessage());
    }

    @Test
    void parse_shouldFail_whenMissingValueForTopN() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{
                        "--file", "access.csv",
                        "--topN"
                })
        );

        assertEquals("Missing value for --topN", ex.getMessage());
    }

    @Test
    void parse_shouldFail_whenMissingValueForSince() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{
                        "--file", "access.csv",
                        "--since"
                })
        );

        assertEquals("Missing value for --since", ex.getMessage());
    }

    @Test
    void parse_shouldFail_whenTopNNotANumber() {
        assertThrows(
                NumberFormatException.class,
                () -> CliArgs.parse(new String[]{
                        "--file", "access.csv",
                        "--topN", "abc"
                })
        );
    }

    @Test
    void parse_shouldFail_whenSinceNotIsoInstant() {
        assertThrows(
                Exception.class,
                () -> CliArgs.parse(new String[]{
                        "--file", "access.csv",
                        "--since", "yesterday"
                })
        );
    }
}
