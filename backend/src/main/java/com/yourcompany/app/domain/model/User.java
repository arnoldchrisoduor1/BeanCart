package com.yourcompany.app.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="users")
public class User {
    
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="first_name")
    private String firstName = "no_firstname";

    @Column(name="last_name")
    private String lastName = "no_lastname";

    @Column(name="profile_img_url")
    private String profileImageUrl = "https://robohash.org/bean";

    @Column(name="role")
    private String role = "buyer";

    @Column(name="is_verified")
    private boolean isVerified = false;

    @Column(name="verification_code")
    private String verificationCode = "no_code";

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    @Column(nullable=false)
    private boolean active;

    // Creating the default constructor.
    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    // The constructor with the required fields.
    public User(String email, String password, String role) {
        this();
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Creating the getters and setters.
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public boolean getIsverified() {
        return isVerified;
    }

    public void setVerificationCode(String verifivationCode) {
        this.verificationCode = verifivationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Pre-update callback to update the updated_at timestamp.
    @PreUpdate
    protected void onUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

}
