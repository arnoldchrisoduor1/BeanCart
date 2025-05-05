package com.yourcompany.app.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemUpdateDto {
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    // Default constructor
    public CartItemUpdateDto() {
    }
    
    // Parameterized constructor
    public CartItemUpdateDto(Integer quantity) {
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    // Builder pattern
    public static CartItemUpdateDtoBuilder builder() {
        return new CartItemUpdateDtoBuilder();
    }
    
    // Builder class
    public static class CartItemUpdateDtoBuilder {
        private Integer quantity;
        
        public CartItemUpdateDtoBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }
        
        public CartItemUpdateDto build() {
            return new CartItemUpdateDto(quantity);
        }
    }
    
    @Override
    public String toString() {
        return "CartItemUpdateDto{" +
                "quantity=" + quantity +
                '}';
    }
}