package com.yourcompany.app.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserProfileDto {
    

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;

    // Creaing the default constructor.
    public UserProfileDto() {}


    // getters and setters

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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
