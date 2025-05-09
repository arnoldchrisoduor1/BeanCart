package com.yourcompany.app.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem {
    
    @Id
    private UUID id;
    
    @Column(name = "cart_id", nullable = false)
    private UUID cartId;
    
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "shop_id", nullable=false)
    private UUID shopId;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    // Default constructor
    public CartItem() {
    }
    
    // Parameterized constructor
    public CartItem(UUID id, UUID cartId, UUID productId, UUID shopId, Integer quantity, LocalDateTime createdAt, 
                   LocalDateTime updatedAt, Cart cart, Product product) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.shopId = shopId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cart = cart;
        this.product = product;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getCartId() {
        return cartId;
    }
    
    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getShopId() {
        return shopId;
    }

    public void setShopId(UUID shopId) {
        this.shopId = shopId;
    }
    
    public UUID getProductId() {
        return productId;
    }
    
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
    
    public Cart getCart() {
        return cart;
    }
    
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    // Builder pattern implementation
    public static CartItemBuilder builder() {
        return new CartItemBuilder();
    }
    
    // Builder class
    public static class CartItemBuilder {
        private UUID id;
        private UUID cartId;
        private UUID productId;
        private UUID shopId;
        private Integer quantity;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Cart cart;
        private Product product;
        
        public CartItemBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public CartItemBuilder cartId(UUID cartId) {
            this.cartId = cartId;
            return this;
        }
        
        public CartItemBuilder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public CartItemBuilder shopId(UUID shopId) {
            this.shopId = shopId;
            return this;
        }
        
        public CartItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }
        
        public CartItemBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public CartItemBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public CartItemBuilder cart(Cart cart) {
            this.cart = cart;
            return this;
        }
        
        public CartItemBuilder product(Product product) {
            this.product = product;
            return this;
        }
        
        public CartItem build() {
            return new CartItem(id, cartId, productId, shopId, quantity, createdAt, updatedAt, cart, product);
        }

    }
    
    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return id != null && id.equals(cartItem.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", productId=" + productId +
                ", shopId=" + shopId +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}