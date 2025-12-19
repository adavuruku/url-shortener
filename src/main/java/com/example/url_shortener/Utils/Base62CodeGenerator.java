package com.example.url_shortener.Utils;

import java.security.SecureRandom;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 12:20 PM
 **/
public class Base62CodeGenerator {
    private static final String CHARS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
