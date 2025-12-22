package com.example.logcli;

import com.example.logcli.Models.CliArgs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 3:55 PM
 **/
class LogCliApplicationTest {
    @Test
    void parse_shouldParseValidArgs() {
        CliArgs args = CliArgs.parse(new String[]{
                "--file", "access.csv",
                "--topN", "10",
                "--since", "2025-01-01T00:00:00Z"
        });

        assertEquals("access.csv", args.file());
        assertEquals(10, args.topN());
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
    void parse_shouldFail_onUnknownOption() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{"--foo", "bar"})
        );
    }

    @Test
    void parse_shouldFail_whenValueMissing() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CliArgs.parse(new String[]{"--file"})
        );
    }



}
