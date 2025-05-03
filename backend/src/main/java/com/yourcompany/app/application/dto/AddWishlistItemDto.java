package com.yourcompany.app.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public class AddWishlistItemDto {
    @NotNull(message = "Product ID is required")
    private UUID productId;
    
    @PositiveOrZero
    private int quantity = 1;
    private String notes;

    // Default constructor
    public AddWishlistItemDto() {}

    // Getters and setters
    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}