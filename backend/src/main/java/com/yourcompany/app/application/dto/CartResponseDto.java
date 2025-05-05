package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartResponseDto {
    
    private UUID id;
    private UUID buyerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemResponseDto> items;
    private Integer totalItems;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;
    
    // Default constructor
    public CartResponseDto() {
        this.items = new ArrayList<>();
    }
    
    // Parameterized constructor
    public CartResponseDto(UUID id, UUID buyerId, LocalDateTime createdAt, LocalDateTime updatedAt,
            List<CartItemResponseDto> items, Integer totalItems, BigDecimal subtotal,
            BigDecimal discount, BigDecimal total) {
        this.id = id;
        this.buyerId = buyerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items != null ? items : new ArrayList<>();
        this.totalItems = totalItems;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getBuyerId() {
        return buyerId;
    }
    
    public void setBuyerId(UUID buyerId) {
        this.buyerId = buyerId;
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
    
    public List<CartItemResponseDto> getItems() {
        return items;
    }
    
    public void setItems(List<CartItemResponseDto> items) {
        this.items = items;
    }
    
    public Integer getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getDiscount() {
        return discount;
    }
    
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    // Builder pattern
    public static CartResponseDtoBuilder builder() {
        return new CartResponseDtoBuilder();
    }
    
    // Builder class
    public static class CartResponseDtoBuilder {
        private UUID id;
        private UUID buyerId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<CartItemResponseDto> items = new ArrayList<>();
        private Integer totalItems;
        private BigDecimal subtotal;
        private BigDecimal discount;
        private BigDecimal total;
        
        public CartResponseDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public CartResponseDtoBuilder buyerId(UUID buyerId) {
            this.buyerId = buyerId;
            return this;
        }
        
        public CartResponseDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public CartResponseDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public CartResponseDtoBuilder items(List<CartItemResponseDto> items) {
            this.items = items;
            return this;
        }
        
        public CartResponseDtoBuilder totalItems(Integer totalItems) {
            this.totalItems = totalItems;
            return this;
        }
        
        public CartResponseDtoBuilder subtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
            return this;
        }
        
        public CartResponseDtoBuilder discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }
        
        public CartResponseDtoBuilder total(BigDecimal total) {
            this.total = total;
            return this;
        }
        
        public CartResponseDto build() {
            return new CartResponseDto(id, buyerId, createdAt, updatedAt, items,
                    totalItems, subtotal, discount, total);
        }
    }
    
    @Override
    public String toString() {
        return "CartResponseDto{" +
                "id=" + id +
                ", buyerId=" + buyerId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", items=" + items +
                ", totalItems=" + totalItems +
                ", subtotal=" + subtotal +
                ", discount=" + discount +
                ", total=" + total +
                '}';
    }
}