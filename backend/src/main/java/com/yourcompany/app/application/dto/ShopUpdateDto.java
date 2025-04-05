package com.yourcompany.app.application.dto;

import java.util.UUID;

import com.yourcompany.app.domain.model.Shop;


public class ShopUpdateDto {
    
    private UUID id;
    private String name;
    private String description;
    private String logoUrl;
    private String address;
    private String phone;
    private String email;
    private Boolean isVerified;
    private Boolean active;

    // default constructor.
    public ShopUpdateDto() {}

    // Constructor with ID only
    public ShopUpdateDto(UUID id) {
        this.id = id;
    }

    // Full constructor.
    public ShopUpdateDto(UUID id, String name, String description, String logoUrl, String address, String phone, String email, Boolean isVerified, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.isVerified = isVerified;
        this.active = active;
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

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // updating an existing shop entity with DTO data.
    public void updateEntity(Shop shop) {
        if (this.name != null) {
            shop.setName(this.name);
        }
        if(this.description != null) {
            shop.setDescription(this.description);
        }
        if (this.logoUrl != null) {
            shop.setLogoUrl(this.logoUrl);
        }
        if (this.address != null) {
            shop.setAddress(this.address);
        }
        if (this.phone != null) {
            shop.setPhone(this.phone);
        }
        if (this.email != null) {
            shop.setEmail(this.email);
        }
        if (this.isVerified != null) {
            shop.setVerified(this.isVerified);
        }
        if (this.active != null) {
            shop.setActive(this.active);
        }
    }

}
