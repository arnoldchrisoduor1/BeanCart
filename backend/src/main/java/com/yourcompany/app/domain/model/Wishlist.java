package com.yourcompany.app.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wishlists")
@Getter
@Setter
public class Wishlist {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    @NotNull
    private UUID userId;
    
    @Column(nullable = false)
    @NotBlank
    @Size(max = 255)
    private String name;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 255)
    private String description;
    
    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> items = new ArrayList<>();
    
    // Business methods
    public void addItem(WishlistItem item2) {
        WishlistItem item = new WishlistItem();
        items.add(item);
    }
    
    public void removeItem(Product product) {
        items.removeIf(item -> item.getProduct().equals(product));
    }
    
    // Better constructor
    public static Wishlist create(UUID userId, String name) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setName(name);
        return wishlist;
    }
}