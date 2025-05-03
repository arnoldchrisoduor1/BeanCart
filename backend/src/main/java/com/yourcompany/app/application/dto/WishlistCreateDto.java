package com.yourcompany.app.application.dto;

import jakarta.validation.constraints.NotBlank;

public class WishlistCreateDto {
    @NotBlank(message = "Wishlist name is required")
    private String name;
    private String description;
    private boolean isPublic = false;

    // Default constructor
    public WishlistCreateDto() {}

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    // Validation method
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Wishlist name cannot be empty");
        }
    }
}