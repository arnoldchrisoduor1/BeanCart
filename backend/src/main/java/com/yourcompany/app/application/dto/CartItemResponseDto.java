package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemResponseDto {
    
    private UUID id;
    private UUID productId;
    private UUID shopId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private BigDecimal productDiscount;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal totalPrice;
    
    // Default constructor
    public CartItemResponseDto() {
    }
    
    // Parameterized constructor
    public CartItemResponseDto(UUID id, UUID productId, UUID shopId, String productName, String productDescription,
            BigDecimal productPrice, BigDecimal productDiscount, String productImageUrl,
            Integer quantity, BigDecimal totalPrice) {
        this.id = id;
        this.productId = productId;
        this.shopId = shopId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getProductId() {
        return productId;
    }
    
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }

    public UUID getShopId() {
        return shopId;
    }

    public void setShopId(UUID shopId) {
        this.shopId = shopId;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public BigDecimal getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
    
    public BigDecimal getProductDiscount() {
        return productDiscount;
    }
    
    public void setProductDiscount(BigDecimal productDiscount) {
        this.productDiscount = productDiscount;
    }
    
    public String getProductImageUrl() {
        return productImageUrl;
    }
    
    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    // Builder pattern
    public static CartItemResponseDtoBuilder builder() {
        return new CartItemResponseDtoBuilder();
    }
    
    // Builder class
    public static class CartItemResponseDtoBuilder {
        private UUID id;
        private UUID productId;
        private UUID shopId;
        private String productName;
        private String productDescription;
        private BigDecimal productPrice;
        private BigDecimal productDiscount;
        private String productImageUrl;
        private Integer quantity;
        private BigDecimal totalPrice;
        
        public CartItemResponseDtoBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public CartItemResponseDtoBuilder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public CartItemResponseDtoBuilder shopId(UUID shopId) {
            this.shopId = shopId;
            return this;
        }
        
        public CartItemResponseDtoBuilder productName(String productName) {
            this.productName = productName;
            return this;
        }
        
        public CartItemResponseDtoBuilder productDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }
        
        public CartItemResponseDtoBuilder productPrice(BigDecimal productPrice) {
            this.productPrice = productPrice;
            return this;
        }
        
        public CartItemResponseDtoBuilder productDiscount(BigDecimal productDiscount) {
            this.productDiscount = productDiscount;
            return this;
        }
        
        public CartItemResponseDtoBuilder productImageUrl(String productImageUrl) {
            this.productImageUrl = productImageUrl;
            return this;
        }
        
        public CartItemResponseDtoBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }
        
        public CartItemResponseDtoBuilder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }
        
        public CartItemResponseDto build() {
            return new CartItemResponseDto(id, productId, shopId, productName, productDescription,
                    productPrice, productDiscount, productImageUrl, quantity, totalPrice);
        }
    }
    
    @Override
    public String toString() {
        return "CartItemResponseDto{" +
                "id=" + id +
                ", productId=" + productId +
                ", shopId=" + shopId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", productDiscount=" + productDiscount +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}