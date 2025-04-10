package com.yourcompany.app.interfaces.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yourcompany.app.application.dto.CartItemDto;
import com.yourcompany.app.application.dto.CartItemUpdateDto;
import com.yourcompany.app.application.dto.CartResponseDto;
import com.yourcompany.app.application.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    /**
     * Get current user's cart
     */
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@RequestAttribute("userId") UUID userId) {
        return ResponseEntity.ok(cartService.getCartByBuyerId(userId));
    }
    
    /**
     * Add item to cart
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addItemToCart(
            @RequestAttribute("userId") UUID userId,
            @Valid @RequestBody CartItemDto cartItemDto) {
        
        return ResponseEntity.ok(cartService.addItemToCart(userId, cartItemDto));
    }
    
    /**
     * Update cart item quantity
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDto> updateCartItemQuantity(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID itemId,
            @Valid @RequestBody CartItemUpdateDto updateDto) {
        
        return ResponseEntity.ok(cartService.updateCartItemQuantity(userId, itemId, updateDto));
    }
    
    /**
     * Remove item from cart
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDto> removeItemFromCart(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID itemId) {
        
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, itemId));
    }
    
    /**
     * Clear cart
     */
    @DeleteMapping
    public ResponseEntity<CartResponseDto> clearCart(@RequestAttribute("userId") UUID userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}