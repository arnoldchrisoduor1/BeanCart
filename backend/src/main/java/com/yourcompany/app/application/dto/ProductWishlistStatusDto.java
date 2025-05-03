package com.yourcompany.app.application.dto;

import java.util.List;
import java.util.UUID;

public class ProductWishlistStatusDto {
    private boolean inWishlist;
    private List<WishlistProductStatusDto> wishlistStatuses;
    private boolean inCart;

    // Default constructor
    public ProductWishlistStatusDto() {}

    // Getters and setters
    public boolean isInWishlist() {
        return inWishlist;
    }

    public void setInWishlist(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }

    public List<WishlistProductStatusDto> getWishlistStatuses() {
        return wishlistStatuses;
    }

    public void setWishlistStatuses(List<WishlistProductStatusDto> wishlistStatuses) {
        this.wishlistStatuses = wishlistStatuses;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }
}