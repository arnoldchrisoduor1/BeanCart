package com.yourcompany.app.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {
    
    @Id
    private UUID id;
    
    @Column(name = "buyer_id", nullable = false)
    private UUID buyerId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", insertable = false, updatable = false)
    private User buyer;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();
    
    // Default constructor
    public Cart() {
        this.cartItems = new HashSet<>();
    }
    
    // Parameterized constructor
    public Cart(UUID id, UUID buyerId, LocalDateTime createdAt, LocalDateTime updatedAt, User buyer, Set<CartItem> cartItems) {
        this.id = id;
        this.buyerId = buyerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.buyer = buyer;
        this.cartItems = cartItems != null ? cartItems : new HashSet<>();
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
    
    public User getBuyer() {
        return buyer;
    }
    
    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }
    
    public Set<CartItem> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    // Builder pattern implementation
    public static CartBuilder builder() {
        return new CartBuilder();
    }
    
    // Builder class
    public static class CartBuilder {
        private UUID id;
        private UUID buyerId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private User buyer;
        private Set<CartItem> cartItems = new HashSet<>();
        
        public CartBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public CartBuilder buyerId(UUID buyerId) {
            this.buyerId = buyerId;
            return this;
        }
        
        public CartBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public CartBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public CartBuilder buyer(User buyer) {
            this.buyer = buyer;
            return this;
        }
        
        public CartBuilder cartItems(Set<CartItem> cartItems) {
            this.cartItems = cartItems;
            return this;
        }
        
        public Cart build() {
            return new Cart(id, buyerId, createdAt, updatedAt, buyer, cartItems);
        }
    }
    
    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id != null && id.equals(cart.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", buyerId=" + buyerId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}