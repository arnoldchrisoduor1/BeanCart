package com.yourcompany.app.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    @Test
    public void testEncodeAndMatch() {
        // Given
        String rawPassword = "securePassword123";

        // When
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Then
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    public void testDifferentPasswordsProduceDifferentEncodings() {
        // Given
        String password1 = "password123";
        String password2 = "password123";

        // When
        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);

        // Then
        assertNotEquals(encoded1, encoded2);
        assertTrue(passwordEncoder.matches(password1, encoded1));
        assertTrue(passwordEncoder.matches(password2, encoded2));
    }
}