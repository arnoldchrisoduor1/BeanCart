package com.yourcompany.app.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourcompany.app.common.exception.BadRequestException;
import com.yourcompany.app.common.exception.ResourceNotFoundException;
import com.yourcompany.app.application.dto.CartItemDto;
import com.yourcompany.app.application.dto.CartItemResponseDto;
import com.yourcompany.app.application.dto.CartItemUpdateDto;
import com.yourcompany.app.application.dto.CartResponseDto;
import com.yourcompany.app.domain.model.Cart;
import com.yourcompany.app.domain.model.CartItem;
import com.yourcompany.app.domain.model.Product;
import com.yourcompany.app.domain.repository.CartItemRepository;
import com.yourcompany.app.domain.repository.CartRepository;
import com.yourcompany.app.domain.repository.ProductRepository;
import com.yourcompany.app.domain.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get cart by buyer ID
     */
    @Transactional(readOnly = true)
    public CartResponseDto getCartByBuyerId(UUID buyerId) {
        // Get or create cart
        Cart cart = getOrCreateCart(buyerId);
        
        // Get cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        
        return buildCartResponseDto(cart, cartItems);
    }
    
    /**
     * Add item to cart
     */
    @Transactional
    public CartResponseDto addItemToCart(UUID buyerId, CartItemDto cartItemDto) {
        // Check if product exists and has enough stock
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartItemDto.getProductId()));
        
        if (!product.getIsActive()) {
            throw new BadRequestException("Product is not available");
        }
        
        if (product.getQuantity() < cartItemDto.getQuantity()) {
            throw new BadRequestException("Not enough stock available");
        }
        
        // Get or create cart
        Cart cart = getOrCreateCart(buyerId);
        
        // Check if cart item already exists
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), cartItemDto.getProductId())
                .orElse(null);
        
        LocalDateTime now = LocalDateTime.now();
        
        if (cartItem == null) {
            // Create new cart item
            cartItem = CartItem.builder()
                    .id(UUID.randomUUID())
                    .cartId(cart.getId())
                    .productId(cartItemDto.getProductId())
                    .quantity(cartItemDto.getQuantity())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
        } else {
            // Update existing cart item quantity
            int newQuantity = cartItem.getQuantity() + cartItemDto.getQuantity();
            
            if (product.getQuantity() < newQuantity) {
                throw new BadRequestException("Not enough stock available");
            }
            
            cartItem.setQuantity(newQuantity);
            cartItem.setUpdatedAt(now);
        }
        
        // Update cart updated_at
        cart.setUpdatedAt(now);
        cartRepository.save(cart);
        
        // Save cart item
        cartItemRepository.save(cartItem);
        
        // Get updated cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        
        return buildCartResponseDto(cart, cartItems);
    }
    
    /**
     * Update cart item quantity
     */
    @Transactional
    public CartResponseDto updateCartItemQuantity(UUID buyerId, UUID cartItemId, CartItemUpdateDto updateDto) {
        // Get cart
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for buyer: " + buyerId));
        
        // Get cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
        
        // Check if cart item belongs to the cart
        if (!cartItem.getCartId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to this cart");
        }
        
        // Check if product has enough stock
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartItem.getProductId()));
        
        if (product.getQuantity() < updateDto.getQuantity()) {
            throw new BadRequestException("Not enough stock available");
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // Update cart item quantity
        cartItem.setQuantity(updateDto.getQuantity());
        cartItem.setUpdatedAt(now);
        cartItemRepository.save(cartItem);
        
        // Update cart updated_at
        cart.setUpdatedAt(now);
        cartRepository.save(cart);
        
        // Get updated cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        
        return buildCartResponseDto(cart, cartItems);
    }
    
    /**
     * Remove item from cart
     */
    @Transactional
    public CartResponseDto removeItemFromCart(UUID buyerId, UUID cartItemId) {
        // Get cart
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for buyer: " + buyerId));
        
        // Get cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
        
        // Check if cart item belongs to the cart
        if (!cartItem.getCartId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to this cart");
        }
        
        // Remove cart item
        cartItemRepository.delete(cartItem);
        
        // Update cart updated_at
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        // Get updated cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        
        return buildCartResponseDto(cart, cartItems);
    }
    
    /**
     * Clear cart
     */
    @Transactional
    public CartResponseDto clearCart(UUID buyerId) {
        // Get cart
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for buyer: " + buyerId));
        
        // Remove all cart items
        cartItemRepository.deleteByCartId(cart.getId());
        
        // Update cart updated_at
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return buildCartResponseDto(cart, List.of());
    }
    
    /**
     * Get or create cart for buyer
     */
    private Cart getOrCreateCart(UUID buyerId) {
        // Check if user exists
        if (!userRepository.existsById(buyerId)) {
            throw new ResourceNotFoundException("User not found with id: " + buyerId);
        }
        
        // Try to find existing cart
        return cartRepository.findByBuyerId(buyerId)
                .orElseGet(() -> {
                    // Create new cart if not exists
                    LocalDateTime now = LocalDateTime.now();
                    Cart newCart = Cart.builder()
                            .id(UUID.randomUUID())
                            .buyerId(buyerId)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                    
                    return cartRepository.save(newCart);
                });
    }
    
    /**
     * Build cart response DTO
     */
    private CartResponseDto buildCartResponseDto(Cart cart, List<CartItem> cartItems) {
        List<CartItemResponseDto> itemDtos = cartItems.stream()
                .map(this::mapToCartItemResponseDto)
                .collect(Collectors.toList());
        
        // Calculate totals
        int totalItems = itemDtos.stream()
                .mapToInt(CartItemResponseDto::getQuantity)
                .sum();
        
        BigDecimal subtotal = itemDtos.stream()
                .map(CartItemResponseDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal discount = itemDtos.stream()
                .map(item -> item.getProductDiscount().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal total = subtotal.subtract(discount);
        
        return CartResponseDto.builder()
                .id(cart.getId())
                .buyerId(cart.getBuyerId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .items(itemDtos)
                .totalItems(totalItems)
                .subtotal(subtotal)
                .discount(discount)
                .total(total)
                .build();
    }
    
    /**
     * Map CartItem to CartItemResponseDto
     */
    private CartItemResponseDto mapToCartItemResponseDto(CartItem cartItem) {
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartItem.getProductId()));
        
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        
        return CartItemResponseDto.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .productDiscount(product.getDiscount())
                .productImageUrl(product.getImageUrl())
                .quantity(cartItem.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }
}