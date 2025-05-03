package com.yourcompany.app.application.service;

import com.yourcompany.app.application.dto.*;
import com.yourcompany.app.common.exception.ResourceNotFoundException;
import com.yourcompany.app.common.exception.AuthenticationException;
import com.yourcompany.app.domain.model.*;
import com.yourcompany.app.domain.repository.ProductRepository;
import com.yourcompany.app.domain.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, 
                         ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public WishlistResponseDto createWishlist(UUID userId, WishlistCreateDto request) {
        request.validate();
        
        // Check if wishlist with same name already exists for user
        if (wishlistRepository.existsByUserIdAndNameIgnoreCase(userId, request.getName())) {
            throw new IllegalArgumentException("You already have a wishlist with this name");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setName(request.getName());
        wishlist.setDescription(request.getDescription());
        wishlist.setPublic(request.isPublic());

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return mapToWishlistResponse(savedWishlist);
    }

    public List<WishlistResponseDto> getUserWishlists(UUID userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(this::mapToWishlistResponse)
                .collect(Collectors.toList());
    }

    public WishlistDetailResponseDto getWishlistDetails(UUID userId, UUID wishlistId) {
        Wishlist wishlist = getWishlistIfAccessible(userId, wishlistId);
        return mapToWishlistDetailResponse(wishlist);
    }

    @Transactional
    public WishlistResponseDto updateWishlist(UUID userId, UUID wishlistId, WishlistUpdateDto request) {
        request.validate();
        
        Wishlist wishlist = getWishlistIfOwned(userId, wishlistId);

        if (request.getName() != null) {
            // Check if new name is already used by another wishlist
            if (!request.getName().equalsIgnoreCase(wishlist.getName()) && 
                wishlistRepository.existsByUserIdAndNameIgnoreCase(userId, request.getName())) {
                throw new IllegalArgumentException("You already have a wishlist with this name");
            }
            wishlist.setName(request.getName());
        }

        if (request.getDescription() != null) {
            wishlist.setDescription(request.getDescription());
        }

        if (request.getIsPublic() != null) {
            wishlist.setPublic(request.getIsPublic());
        }

        Wishlist updatedWishlist = wishlistRepository.save(wishlist);
        return mapToWishlistResponse(updatedWishlist);
    }

    @Transactional
    public void deleteWishlist(UUID userId, UUID wishlistId) {
        Wishlist wishlist = getWishlistIfOwned(userId, wishlistId);
        wishlistRepository.delete(wishlist);
    }

    @Transactional
    public WishlistItemResponseDto addItemToWishlist(UUID userId, UUID wishlistId, AddWishlistItemDto request) {
        Wishlist wishlist = getWishlistIfOwned(userId, wishlistId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if product already exists in wishlist
        if (wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()))) {
            throw new IllegalArgumentException("Product already exists in wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        item.setNotes(request.getNotes());

        wishlist.addItem(item);
        wishlistRepository.save(wishlist);

        return mapToWishlistItemResponse(item);
    }

    @Transactional
    public WishlistItemResponseDto updateWishlistItem(UUID userId, UUID wishlistId, UUID itemId, UpdateWishlistItemDto request) {
        Wishlist wishlist = getWishlistIfOwned(userId, wishlistId);
        
        WishlistItem item = wishlist.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in wishlist"));

        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }

        if (request.getNotes() != null) {
            item.setNotes(request.getNotes());
        }

        wishlistRepository.save(wishlist);
        return mapToWishlistItemResponse(item);
    }

    @Transactional
    public void removeItemFromWishlist(UUID userId, UUID wishlistId, UUID productId) {
        Wishlist wishlist = getWishlistIfOwned(userId, wishlistId);
        
        boolean removed = wishlist.getItems().removeIf(
            item -> item.getProduct().getId().equals(productId)
        );
        
        if (!removed) {
            throw new ResourceNotFoundException("Product not found in wishlist");
        }
        
        wishlistRepository.save(wishlist);
    }

    public ProductWishlistStatusDto getProductWishlistStatus(UUID userId, UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<WishlistProductStatusDto> statuses = wishlistRepository.findByUserId(userId).stream()
                .map(wishlist -> {
                    WishlistProductStatusDto status = new WishlistProductStatusDto();
                    status.setWishlistId(wishlist.getId());
                    status.setWishlistName(wishlist.getName());
                    status.setContainsProduct(
                        wishlist.getItems().stream()
                            .anyMatch(item -> item.getProduct().getId().equals(productId))
                    );
                    return status;
                })
                .collect(Collectors.toList());

        boolean inAnyWishlist = statuses.stream()
                .anyMatch(WishlistProductStatusDto::isContainsProduct);

        ProductWishlistStatusDto response = new ProductWishlistStatusDto();
        response.setInWishlist(inAnyWishlist);
        response.setWishlistStatuses(statuses);
        // Note: Cart status would need to be implemented separately
        response.setInCart(false); 

        return response;
    }

    // Helper methods
    private Wishlist getWishlistIfOwned(UUID userId, UUID wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        
        if (!wishlist.getUserId().equals(userId)) {
            throw new AuthenticationException("You don't have permission to modify this wishlist");
        }
        
        return wishlist;
    }

    private Wishlist getWishlistIfAccessible(UUID userId, UUID wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        
        if (!wishlist.getUserId().equals(userId) && !wishlist.isPublic()) {
            throw new AuthenticationException("You don't have permission to view this wishlist");
        }
        
        return wishlist;
    }

    private WishlistResponseDto mapToWishlistResponse(Wishlist wishlist) {
        WishlistResponseDto response = new WishlistResponseDto();
        response.setId(wishlist.getId());
        response.setName(wishlist.getName());
        // response.setDescription(wishlist.getDescription());
        response.setPublic(wishlist.isPublic());
        response.setCreatedAt(wishlist.getCreatedAt());
        response.setUpdatedAt(wishlist.getUpdatedAt());
        response.setItemCount(wishlist.getItems().size());
        response.setOwnerId(wishlist.getUserId());
        return response;
    }

    private WishlistDetailResponseDto mapToWishlistDetailResponse(Wishlist wishlist) {
        WishlistDetailResponseDto response = new WishlistDetailResponseDto();
        response.setId(wishlist.getId());
        response.setName(wishlist.getName());
        response.setDescription(wishlist.getDescription());
        response.setPublic(wishlist.isPublic());
        response.setCreatedAt(wishlist.getCreatedAt());
        response.setUpdatedAt(wishlist.getUpdatedAt());
        response.setOwnerId(wishlist.getUserId());
        
        response.setItems(wishlist.getItems().stream()
                .map(this::mapToWishlistItemResponse)
                .collect(Collectors.toList()));
        
        return response;
    }

    private WishlistItemResponseDto mapToWishlistItemResponse(WishlistItem item) {
        Product product = item.getProduct();
        
        WishlistItemResponseDto response = new WishlistItemResponseDto();
        response.setId(item.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setProductImageUrl(product.getImageUrl());
        response.setProductPrice(product.getPrice());
        response.setAddedAt(item.getAddedAt());
        response.setQuantity(item.getQuantity());
        response.setNotes(item.getNotes());
        
        return response;
    }
}