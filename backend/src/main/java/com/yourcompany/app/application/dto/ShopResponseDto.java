package com.yourcompany.app.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.yourcompany.app.domain.model.Shop;

// This Dto creates the response for the shop data.
public class ShopResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String logoUrl;
    private String address;
    private String phone;
    private String email;
    private boolean isVerified;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // seller info.
    private UUID sellerId;
    private String sellerName;
    private String sellerEmail;

    // Default constructor.
    public ShopResponseDto() {}

    // Constructor from entity.
    public ShopResponseDto(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.description = shop.getDescription();
        this.logoUrl = shop.getLogoUrl();
        this.address = shop.getAddress();
        this.phone = shop.getPhone();
        this.email = shop.getEmail();
        this.isVerified = shop.isVerified();
        this.active = shop.isActive();
        this.createdAt = shop.getCreatedAt();
        this.updatedAt = shop.getUpdatedAt();

        // Extracting basic seller info
        if(shop.getSeller() != null) {
            this.sellerId = shop.getSeller().getId();
            this.sellerName = shop.getSeller().getFirstName();
            this.sellerEmail = shop.getSeller().getEmail();
        }
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public UUID getSellerId() {
        return sellerId;
    }

    public void setSellerId(UUID sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    // Static convenience method to convert a shop to DTO
    public static ShopResponseDto fromEntity(Shop shop) {
        return new ShopResponseDto(shop);
    }
}
