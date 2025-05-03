package com.yourcompany.app.interfaces.rest;

import com.yourcompany.app.application.dto.*;
import com.yourcompany.app.application.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistResponseDto> createWishlist(
            @RequestAttribute("userId") UUID userId,
            @Valid @RequestBody WishlistCreateDto request) {
        WishlistResponseDto response = wishlistService.createWishlist(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> getUserWishlists(
            @RequestAttribute("userId") UUID userId) {
        List<WishlistResponseDto> wishlists = wishlistService.getUserWishlists(userId);
        return ResponseEntity.ok(wishlists);
    }

    @GetMapping("/{wishlistId}")
    public ResponseEntity<WishlistDetailResponseDto> getWishlistDetails(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID wishlistId) {
        WishlistDetailResponseDto response = wishlistService.getWishlistDetails(userId, wishlistId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{wishlistId}")
    public ResponseEntity<WishlistResponseDto> updateWishlist(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID wishlistId,
            @Valid @RequestBody WishlistUpdateDto request) {
        WishlistResponseDto response = wishlistService.updateWishlist(userId, wishlistId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> deleteWishlist(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID wishlistId) {
        wishlistService.deleteWishlist(userId, wishlistId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{wishlistId}/items")
    public ResponseEntity<WishlistItemResponseDto> addItemToWishlist(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID wishlistId,
            @Valid @RequestBody AddWishlistItemDto request) {
        WishlistItemResponseDto response = wishlistService.addItemToWishlist(userId, wishlistId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{wishlistId}/items/{itemId}")
    public ResponseEntity<WishlistItemResponseDto> updateWishlistItem(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID wishlistId,
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateWishlistItemDto request) {
        WishlistItemResponseDto response = wishlistService.updateWishlistItem(userId, wishlistId, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wishlistId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromWishlist(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID wishlistId,
            @PathVariable UUID productId) {
        wishlistService.removeItemFromWishlist(userId, wishlistId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/{productId}/status")
    public ResponseEntity<ProductWishlistStatusDto> getProductWishlistStatus(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID productId) {
        ProductWishlistStatusDto response = wishlistService.getProductWishlistStatus(userId, productId);
        return ResponseEntity.ok(response);
    }

    // Additional endpoint to get just the items in a wishlist
    @GetMapping("/{wishlistId}/items")
    public ResponseEntity<List<WishlistItemResponseDto>> getWishlistItems(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID wishlistId) {
        WishlistDetailResponseDto wishlist = wishlistService.getWishlistDetails(userId, wishlistId);
        return ResponseEntity.ok(wishlist.getItems());
    }
}