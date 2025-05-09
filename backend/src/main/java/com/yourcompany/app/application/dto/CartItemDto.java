package com.yourcompany.app.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// DTO for cart item operations
public class CartItemDto {
    
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Shop ID is required")
    private UUID shopId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    // Default constructor
    public CartItemDto() {
    }
    
    // Parameterized constructor
    public CartItemDto(UUID productId, UUID shopId, Integer quantity) {
        this.productId = productId;
        this.shopId = shopId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public UUID getProductId() {
        return productId;
    }
    
    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getShopId() {
        return shopId;
    }

    public void setShopId(UUID shopId) {
        this.shopId = shopId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    // Builder pattern implementation
    public static CartItemDtoBuilder builder() {
        return new CartItemDtoBuilder();
    }
    
    // Builder class
    public static class CartItemDtoBuilder {
        private UUID productId;
        private UUID shopId;
        private Integer quantity;
        
        public CartItemDtoBuilder productId(UUID productId) {
            this.productId = productId;
            return this;
        }
        
        public CartItemDtoBuilder shopId(UUID shopId) {
            this.shopId = shopId;
            return this;
        }
        
        public CartItemDtoBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        
        public CartItemDto build() {
            return new CartItemDto(productId, shopId, quantity);
        }
    }
    
    @Override
    public String toString() {
        return "CartItemDto{" +
                "productId=" + productId +
                ", shopId=" + shopId +
                ", quantity=" + quantity +
                '}';
    }
}