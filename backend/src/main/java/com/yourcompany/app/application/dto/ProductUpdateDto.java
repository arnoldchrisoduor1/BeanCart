package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.util.UUID;
import com.yourcompany.app.domain.model.Product;

public class ProductUpdateDto {
    
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private String imageUrl;
    private Integer quantity;
    private String category;
    private Boolean isFeatured;
    private Boolean active;

    // Default update constructor.
    public ProductUpdateDto() {}

    // Default with id only
    public ProductUpdateDto(UUID id) {
        this.id = id;
    }

    // Full constructor.
    public ProductUpdateDto(UUID id, String name, String description, BigDecimal price, BigDecimal discount, String imageUrl, Integer quantity, String category, boolean isFeatured, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.category = category;
        this.isFeatured = isFeatured;
        this.active = active;
    }

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

    public Boolean isFeatured() {
        return isFeatured;
    }

    public Boolean isActive() {
        return active;
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

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Time to update an existing product entity wtih DTO data.
    public void updateEntity(Product product) {
        if(this.name != null) {
            product.setName(this.name);
        }
        if(this.description != null) {
            product.setDescription(this.description);
        }
        if(this.price != null) {
            product.setPrice(this.price);
        }
        if(this.discount != null) {
            product.setDiscount(this.discount);
        }
        if(this.imageUrl != null) {
            product.setImageUrl(this.imageUrl);
        }
        if(this.quantity != null) {
            product.setQuantity(this.quantity);
        }
        if(this.category != null) {
            product.setCategory(this.category);
        }
        if(this.isFeatured != null) {
            product.setIsFeatured(this.isFeatured);
        }
        if(this.active != null) {
            product.setIsActive(this.active);
        }
    }

}
