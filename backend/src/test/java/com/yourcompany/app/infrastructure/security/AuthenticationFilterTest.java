package com.yourcompany.app.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private final String TEST_TOKEN = "valid-token";
    private final UUID TEST_USER_ID = UUID.randomUUID();
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testPublicPathsAllowed() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/auth/login");

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).validateToken(anyString());
    }

    @Test
    public void testValidTokenAuthentication() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/users/profile");
        request.addHeader("Authorization", "Bearer " + TEST_TOKEN);

        when(jwtUtil.validateToken(TEST_TOKEN)).thenReturn(true);
        when(jwtUtil.extractUserId(TEST_TOKEN)).thenReturn(TEST_USER_ID.toString());
        when(jwtUtil.extractEmail(TEST_TOKEN)).thenReturn(TEST_EMAIL);

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtUtil).validateToken(TEST_TOKEN);
        verify(jwtUtil).extractUserId(TEST_TOKEN);
        verify(jwtUtil).extractEmail(TEST_TOKEN);
        verify(filterChain).doFilter(request, response);
        assertEquals(TEST_USER_ID, request.getAttribute("userId"));
        assertEquals(TEST_EMAIL, request.getAttribute("email"));
    }

    @Test
    public void testMissingToken() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/users/profile");
        // No Authorization header

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    public void testInvalidToken() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/users/profile");
        request.addHeader("Authorization", "Bearer invalid-token");

        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(jwtUtil).validateToken("invalid-token");
        verify(filterChain, never()).doFilter(any(), any());
    }
}