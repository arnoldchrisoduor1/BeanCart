package com.yourcompany.app.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String TEST_SECRET = "testsecrettestsecrettestsecrettestsecrettestsecret";
    private final Long TEST_EXPIRATION = 3600000L; // 1 hour

    @BeforeEach
    public void setup() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", TEST_EXPIRATION);
    }

    @Test
    public void testGenerateAndValidateToken() {
        // Given
        String id = UUID.randomUUID().toString();
        String email = "test@example.com";

        // When
        String token = jwtUtil.generateToken(id, email);

        // Then
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(email, jwtUtil.extractEmail(token));
        assertEquals(id, jwtUtil.extractUserId(token));
    }

    @Test
    public void testInvalidToken() {
        // Given
        String invalidToken = "invalid.token.string";

        // When & Then
        assertFalse(jwtUtil.validateToken(invalidToken));
    }
}