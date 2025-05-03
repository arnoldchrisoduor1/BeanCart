package com.yourcompany.app.application.dto;

import java.util.UUID;

public class WishlistProductStatusDto {
    private UUID wishlistId;
    private String wishlistName;
    private boolean containsProduct;

    // Default constructor
    public WishlistProductStatusDto() {}

    // Getters and setters
    public UUID getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(UUID wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getWishlistName() {
        return wishlistName;
    }

    public void setWishlistName(String wishlistName) {
        this.wishlistName = wishlistName;
    }

    public boolean isContainsProduct() {
        return containsProduct;
    }

    public void setContainsProduct(boolean containsProduct) {
        this.containsProduct = containsProduct;
    }
}