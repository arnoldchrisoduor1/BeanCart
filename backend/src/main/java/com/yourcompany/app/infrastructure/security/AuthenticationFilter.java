package com.yourcompany.app.infrastructure.security;

import com.yourcompany.app.common.exception.AuthenticationException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // List of paths that don't require authentication
    private final List<String> publicPaths = Arrays.asList(
            "/api/auth/register",
            "/api/auth/login",
            "/error");

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        try {
            // Check if path is public
            if (isPublicPath(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            // Get token from header
            String token = extractToken(request);
            if (token == null) {
                throw new AuthenticationException("No authentication token provided");
            }

            // Validate token
            if (!jwtUtil.validateToken(token)) {
                throw new AuthenticationException("Invalid or expired token");
            }

            System.out.println("Token validated");

            // Extract user information from token
            String userId = jwtUtil.extractUserId(token);
            System.out.println("Auth filter: Extracted userId from token: " + userId);
            logger.debug("Extracted userId from token: {}", userId);
            if (userId == null || userId.isEmpty()) {
                throw new AuthenticationException("Invalid user ID in token");
            }
            String email = jwtUtil.extractEmail(token);

            // Set user information in request attributes
            try {
                request.setAttribute("userId", UUID.fromString(userId));
            } catch (IllegalArgumentException e) {
                throw new AuthenticationException("Invalid UUID format for user ID: " + userId);
            }
            request.setAttribute("email", email);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
        }
    }

    private boolean isPublicPath(String currentPath) {
        return publicPaths.stream().anyMatch(path -> pathMatcher.match(path, currentPath));
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}