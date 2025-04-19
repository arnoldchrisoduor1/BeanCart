package com.yourcompany.app.interfaces.rest;

import com.yourcompany.app.application.dto.ShopCreateDto;
import com.yourcompany.app.application.dto.ShopResponseDto;
import com.yourcompany.app.application.dto.ShopUpdateDto;
import com.yourcompany.app.application.service.ShopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);
    
    private final ShopService shopService;
    
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }
    
    @PostMapping
    public ResponseEntity<ShopResponseDto> createShop(
            HttpServletRequest request,
            @Valid @RequestBody ShopCreateDto shopDto) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("Creating shop for seller ID: {}", userId);
        
        // Set the seller ID from the authenticated user
        shopDto.setSellerId(userId);
        
        ShopResponseDto createdShop = shopService.createShop(shopDto);
        System.out.println("==============Shop created successfully============");
        return new ResponseEntity<>(createdShop, HttpStatus.CREATED);
    }
    
    @PutMapping("/{shopId}")
    public ResponseEntity<ShopResponseDto> updateShop(
            HttpServletRequest request,
            @PathVariable UUID shopId,
            @Valid @RequestBody ShopUpdateDto shopDto) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("Updating shop {} by user {}", shopId, userId);
        
        // Ensure the ID in the path matches the ID in the DTO
        shopDto.setId(shopId);
        
        // TODO: Add authorization check to ensure user owns the shop or is admin
        
        ShopResponseDto updatedShop = shopService.updateShop(shopDto);
        return ResponseEntity.ok(updatedShop);
    }
    
    @GetMapping("/{shopId}")
    public ResponseEntity<ShopResponseDto> getShopById(@PathVariable UUID shopId) {
        logger.debug("Fetching shop with ID: {}", shopId);
        
        ShopResponseDto shop = shopService.getShopById(shopId);
        return ResponseEntity.ok(shop);
    }
    
    @GetMapping
    public ResponseEntity<List<ShopResponseDto>> getAllShops(
            @RequestParam(required = false, defaultValue = "true") boolean activeOnly) {
        
        logger.debug("Fetching all shops with activeOnly={}", activeOnly);
        
        List<ShopResponseDto> shops = shopService.getAllShops(activeOnly);
        return ResponseEntity.ok(shops);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ShopResponseDto>> searchShops(
            @RequestParam String name) {
        
        logger.debug("Searching shops with name containing: {}", name);
        
        List<ShopResponseDto> shops = shopService.searchShopsByName(name);
        return ResponseEntity.ok(shops);
    }
    
    @GetMapping("/seller")
    public ResponseEntity<List<ShopResponseDto>> getMyShops(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "true") boolean activeOnly) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("Fetching shops for seller: {} with activeOnly={}", userId, activeOnly);
        
        List<ShopResponseDto> shops = shopService.getShopsBySeller(userId, activeOnly);
        return ResponseEntity.ok(shops);
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ShopResponseDto>> getShopsBySeller(
            @PathVariable UUID sellerId,
            @RequestParam(required = false, defaultValue = "true") boolean activeOnly) {
        
        logger.debug("Fetching shops for seller: {} with activeOnly={}", sellerId, activeOnly);
        
        List<ShopResponseDto> shops = shopService.getShopsBySeller(sellerId, activeOnly);
        return ResponseEntity.ok(shops);
    }
    
    @PatchMapping("/{shopId}/verify")
    public ResponseEntity<ShopResponseDto> verifyShop(
            HttpServletRequest request,
            @PathVariable UUID shopId) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} verifying shop {}", userId, shopId);
        
        // TODO: Add role-based authorization check (only admins should verify shops)
        
        ShopResponseDto verifiedShop = shopService.verifyShop(shopId);
        return ResponseEntity.ok(verifiedShop);
    }
    
    @PatchMapping("/{shopId}/deactivate")
    public ResponseEntity<ShopResponseDto> deactivateShop(
            HttpServletRequest request,
            @PathVariable UUID shopId) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} deactivating shop {}", userId, shopId);
        
        // TODO: Add authorization check to ensure user owns the shop or is admin
        
        ShopResponseDto deactivatedShop = shopService.deactivateShop(shopId);
        return ResponseEntity.ok(deactivatedShop);
    }
    
    @PatchMapping("/{shopId}/reactivate")
    public ResponseEntity<ShopResponseDto> reactivateShop(
            HttpServletRequest request,
            @PathVariable UUID shopId) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} reactivating shop {}", userId, shopId);
        
        // TODO: Add authorization check to ensure user owns the shop or is admin
        
        ShopResponseDto reactivatedShop = shopService.reactivateShop(shopId);
        return ResponseEntity.ok(reactivatedShop);
    }
    
    @DeleteMapping("/{shopId}")
    public ResponseEntity<Map<String, String>> deleteShop(
            HttpServletRequest request,
            @PathVariable UUID shopId) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} deleting shop {}", userId, shopId);
        
        // TODO: Add authorization check to ensure user owns the shop or is admin
        
        shopService.deleteShop(shopId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Shop deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
