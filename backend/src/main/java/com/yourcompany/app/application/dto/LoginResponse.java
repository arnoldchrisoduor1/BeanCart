package com.yourcompany.app.application.dto;

import java.util.UUID;

public class LoginResponse {
    
    private UUID userId;
    private String email;
    private String token;
    private String role;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private boolean isVerified;

    public LoginResponse(UUID userId, String email, String token, String role, String firstName, String lastName, String profileImageUrl, boolean isVerified) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.isVerified = isVerified;
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

    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public boolean getIsVerified() {
        return isVerified;
    }

}
