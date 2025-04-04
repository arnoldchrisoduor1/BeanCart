package com.yourcompany.app.application.dto;

import java.util.UUID;

public class LoginResponse {
    
    private UUID userId;
    private String email;
    private String token;

    public LoginResponse(UUID userId, String email, String token) {
        this.userId = userId;
        this.email = email;
        this.token = token;
    }

    // Getters
    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

}
