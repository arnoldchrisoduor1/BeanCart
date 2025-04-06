package com.yourcompany.app.application.dto;

import com.yourcompany.app.domain.model.Product;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;

public class ProductResponseDto {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private String imageUrl;
    private Integer quantity;
    private String category;
    private boolean isFeatured;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Shp info.
    private UUID shopId;
    private String shopName;
    private String shopEmail;
    private String shopPhone;

    // Default response constructor.
    public ProductResponseDto() {}

    // Creating constructor from entity.
    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.imageUrl = product.getImageUrl();
        this.quantity = product.getQuantity();
        this.category = product.getCategory();
        this.isFeatured = product.getIsFeatured();
        this.active = product.getIsActive();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();

        // Extracting basic Shop info.
        if(product.getShop() != null) {
            this.shopId = product.getShop().getId();
            this. shopName = product.getShop().getName();
            this.shopEmail = product.getShop().getEmail();
            this.shopPhone = product.getShop().getPhone();
        }
    }

    // creating the getters and setters.
    // Getters
public UUID getId() {
    return id;
}

public String getName() {
    return name;
}

public String getDescription() {
    return description;
}

public BigDecimal getPrice() {
    return price;
}

public BigDecimal getDiscount() {
    return discount;
}

public String getImageUrl() {
    return imageUrl;
}

public Integer getQuantity() {
    return quantity;
}

public String getCategory() {
    return category;
}

public boolean isFeatured() {
    return isFeatured;
}

public boolean isActive() {
    return active;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public LocalDateTime getUpdatedAt() {
    return updatedAt;
}

public UUID getShopId() {
    return shopId;
}

public String getShopName() {
    return shopName;
}

public String getShopEmail() {
    return shopEmail;
}

public String getShopPhone() {
    return shopPhone;
}

// Setters
public void setId(UUID id) {
    this.id = id;
}

public void setName(String name) {
    this.name = name;
}

public void setDescription(String description) {
    this.description = description;
}

public void setPrice(BigDecimal price) {
    this.price = price;
}

public void setDiscount(BigDecimal discount) {
    this.discount = discount;
}

public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
}

public void setQuantity(Integer quantity) {
    this.quantity = quantity;
}

public void setCategory(String category) {
    this.category = category;
}

public void setIsFeatured(boolean featured) {  // Note: 'set' prefix (no 'is')
    isFeatured = featured;
}

public void setActive(boolean active) {
    this.active = active;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
}

public void setShopId(UUID shopId) {
    this.shopId = shopId;
}

public void setShopName(String shopName) {
    this.shopName = shopName;
}

public void setShopEmail(String shopEmail) {
    this.shopEmail = shopEmail;
}

public void setShopPhone(String shopPhone) {
    this.shopPhone = shopPhone;
}

// now for a sattic cnvenience method to convert a product to DTO.
    public static ProductResponseDto fromEntity(Product product) {
        return new ProductResponseDto(product);
    }
}