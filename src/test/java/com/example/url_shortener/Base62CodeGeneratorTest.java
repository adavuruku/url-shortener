package com.example.url_shortener;

/**
 * Created by Sherif.Abdulraheem 12/22/2025 - 1:16 AM
 **/
import com.example.url_shortener.Utils.Base62CodeGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class Base62CodeGeneratorTest {

    private final Base62CodeGenerator generator = new Base62CodeGenerator();

    private static final Pattern BASE62_PATTERN =
            Pattern.compile("^[0-9A-Za-z]{6}$");

    @Test
    void generatedCodeShouldHaveCorrectLengthAndCharacters() {
        String code = generator.generate();

        assertNotNull(code, "Code should not be null");
        assertEquals(6, code.length(), "Code should be 6 characters");
        assertTrue(BASE62_PATTERN.matcher(code).matches(),
                "Code should only contain 0-9, A-Z, a-z");
    }

    @Test
    void generatedCodesShouldBeUniqueInSmallSample() {
        Set<String> codes = new HashSet<>();
        int sampleSize = 1000;

        for (int i = 0; i < sampleSize; i++) {
            String code = generator.generate();
            assertTrue(codes.add(code), "Duplicate code detected: " + code);
        }
    }

    @Test
    void simulateCollisionRetry() {
        // Simple simulation: pretend first generated code is already used
        Set<String> usedCodes = new HashSet<>();
        String firstCode = generator.generate();
        usedCodes.add(firstCode);

        String newCode;
        int attempts = 0;
        do {
            newCode = generator.generate();
            attempts++;
        } while (usedCodes.contains(newCode) && attempts < 10);

        assertFalse(usedCodes.contains(newCode), "Collision not resolved");
    }
}
