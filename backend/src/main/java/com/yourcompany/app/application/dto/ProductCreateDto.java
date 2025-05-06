package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.yourcompany.app.domain.model.Product;
import com.yourcompany.app.domain.model.Shop;


// Dto for creating a pproduct object.
public class ProductCreateDto {

    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private String imageUrl;
    private Integer quantity;
    private String category;
    private boolean isFeatured;
    private boolean active;
    private UUID shopId;

    // The default constructor.
    public ProductCreateDto() {}

    // Constructor with required fields.
    public ProductCreateDto(String name, BigDecimal price, String category, UUID shopId, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.shopId = shopId;
    }

    // Full constructor.
    public ProductCreateDto(String name, BigDecimal price, String category, UUID shopId, String description, BigDecimal discount, String imageUrl, boolean isFeatured, boolean active, Integer quantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.shopId = shopId;
        this.description = description;
        this.quantity = quantity;
        this.discount = discount;
        this.imageUrl = imageUrl;
        this. isFeatured = isFeatured;
        this.active = active;
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
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getDiscount() {
        return discount;
    }
    
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean isFeatured() {
        return isFeatured;
    }
    
    public void setIsFeatured(boolean featured) {
        isFeatured = featured;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public UUID getShopId() {
        return shopId;
    }
    
    public void setShopId(UUID shopId) {
        this.shopId = shopId;
    }

    // Converting DTO to Entity we will need Shop object from service.
    public Product toEntity(Shop shop) {
        // creating the product with mandatoy fields.
        Product product = new Product(shop, this.name, this.price, this.category, this.quantity);

        // creating fields with optional fields if present.
        if (this.description != null) {
            product.setDescription(this.description);
        }

        if (this.discount != null) {
            product.setDiscount(this.discount);
        }

        if (this.imageUrl != null) {
            product.setImageUrl(this.imageUrl);
        }

        if (this.quantity != null) {
            product.setQuantity(this.quantity);
        }

        // returning the fully constructed Product entity.
        return product;
    }

}

