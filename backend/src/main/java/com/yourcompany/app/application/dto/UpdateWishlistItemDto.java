package com.yourcompany.app.application.dto;

import jakarta.validation.constraints.PositiveOrZero;

public class UpdateWishlistItemDto {
    @PositiveOrZero
    private Integer quantity;
    private String notes;

    // Default constructor
    public UpdateWishlistItemDto() {}

    // Getters and setters
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}