package com.yourcompany.app.application.dto;

public class WishlistUpdateDto {
    private String name;
    private String description;
    private Boolean isPublic;

    // Default constructor
    public WishlistUpdateDto() {}

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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    // Validation method
    public void validate() {
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("Wishlist name cannot be empty");
        }
    }
}