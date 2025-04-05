package com.yourcompany.app.application.dto;

import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.model.User;

import java.util.UUID;


// This Dto is used in creating a new shop.
public class ShopCreateDto {

    private String name;
    private String description;
    private String logoUrl;
    private String address;
    private String phone;
    private String email;
    private UUID sellerId;

    // Default constructor.
    public ShopCreateDto() {}

    // Constructor with required fields.
    public ShopCreateDto(String name, String phone, String email, UUID sellerId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.sellerId = sellerId;
    }

    // Full constructor.
    public ShopCreateDto(String name, String description, String logoUrl, String address, String phone, String email, UUID sellerId) {
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.sellerId = sellerId;
    }

    // Getters and setters
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

    public UUID getSellerId() {
        return sellerId;
    }

    public void setSellerId(UUID sellerId) {
        this.sellerId = sellerId;
    }

    // Convert DTO to Entity (we will need User object from service)
    public Shop toEntity(User seller) {
        Shop shop = new Shop(seller, this.name, this.phone, this.email);

        if(this.description != null) {
            shop.setDescription(this.description);
        }

        if (this.logoUrl != null) {
            shop.setLogoUrl(this.logoUrl);
        }

        if(this.address != null) {
            shop.setAddress(this.address);
        }

        return shop;
    }
}
