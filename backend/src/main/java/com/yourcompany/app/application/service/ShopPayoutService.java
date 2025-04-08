package com.yourcompany.app.application.service;

import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.model.ShopPayout;
import com.yourcompany.app.domain.repository.ShopPayoutRepository;
import com.yourcompany.app.domain.repository.ShopRepository;
import com.yourcompany.app.application.dto.ShopPayoutCreateDto;
import com.yourcompany.app.application.dto.ShopPayoutResponseDto;
import com.yourcompany.app.application.dto.ShopPayoutUpdateDto;
import com.yourcompany.app.common.exception.ResourceNotFoundException;
import com.yourcompany.app.common.exception.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShopPayoutService {

    private final ShopPayoutRepository shopPayoutRepository;
    private final ShopRepository shopRepository;
    
    @Autowired
    public ShopPayoutService(ShopPayoutRepository shopPayoutRepository, ShopRepository shopRepository) {
        this.shopPayoutRepository = shopPayoutRepository;
        this.shopRepository = shopRepository;
    }
    
    /**
     * Create a new shop payout
     */
    @Transactional
    public ShopPayoutResponseDto createShopPayout(ShopPayoutCreateDto createDto) {
        // Validate dates
        if (createDto.getPeriodEnd().isBefore(createDto.getPeriodStart())) {
            throw new BadRequestException("Period end date must be after period start date");
        }
        
        // Fetch the shop
        Shop shop = shopRepository.findById(createDto.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + createDto.getShopId()));
        
        // Check for overlapping periods
        boolean overlappingExists = shopPayoutRepository.existsOverlappingPeriodForShop(
                createDto.getShopId(), createDto.getPeriodStart(), createDto.getPeriodEnd());
        
        if (overlappingExists) {
            throw new BadRequestException("An overlapping payout period already exists for this shop");
        }
        
        // Create new shop payout
        ShopPayout shopPayout = new ShopPayout();
        shopPayout.setShop(shop);
        shopPayout.setAmount(createDto.getAmount());
        shopPayout.setStripeTransferId(createDto.getStripeTransferId());
        shopPayout.setStatus(createDto.getStatus());
        shopPayout.setPeriodStart(createDto.getPeriodStart());
        shopPayout.setPeriodEnd(createDto.getPeriodEnd());
        
        // Save and return
        ShopPayout savedPayout = shopPayoutRepository.save(shopPayout);
        return mapToResponseDto(savedPayout);
    }
    
    /**
     * Get a shop payout by ID
     */
    public ShopPayoutResponseDto getShopPayoutById(UUID id) {
        ShopPayout shopPayout = shopPayoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop payout not found with id: " + id));
        return mapToResponseDto(shopPayout);
    }
    
    /**
     * Get a shop payout by Stripe transfer ID
     */
    public ShopPayoutResponseDto getShopPayoutByStripeTransferId(String stripeTransferId) {
        ShopPayout shopPayout = shopPayoutRepository.findByStripeTransferId(stripeTransferId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop payout not found with Stripe transfer id: " + stripeTransferId));
        return mapToResponseDto(shopPayout);
    }
    
    /**
     * Get all payouts for a specific shop
     */
    public List<ShopPayoutResponseDto> getShopPayoutsByShopId(UUID shopId) {
        // Verify shop exists
        if (!shopRepository.existsById(shopId)) {
            throw new ResourceNotFoundException("Shop not found with id: " + shopId);
        }
        
        List<ShopPayout> payouts = shopPayoutRepository.findByShopId(shopId);
        return payouts.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get payouts by status
     */
    public List<ShopPayoutResponseDto> getShopPayoutsByStatus(String status) {
        List<ShopPayout> payouts = shopPayoutRepository.findByStatus(status);
        return payouts.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get payouts by shop ID and status
     */
    public List<ShopPayoutResponseDto> getShopPayoutsByShopIdAndStatus(UUID shopId, String status) {
        // Verify shop exists
        if (!shopRepository.existsById(shopId)) {
            throw new ResourceNotFoundException("Shop not found with id: " + shopId);
        }
        
        List<ShopPayout> payouts = shopPayoutRepository.findByShopIdAndStatus(shopId, status);
        return payouts.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get payouts for a shop within a specific period
     */
    public List<ShopPayoutResponseDto> getShopPayoutsByShopIdAndPeriod(
            UUID shopId, LocalDateTime periodStart, LocalDateTime periodEnd) {
        // Verify shop exists
        if (!shopRepository.existsById(shopId)) {
            throw new ResourceNotFoundException("Shop not found with id: " + shopId);
        }
        
        List<ShopPayout> payouts = shopPayoutRepository.findByShopIdAndPeriodStartAfterAndPeriodEndBefore(
                shopId, periodStart, periodEnd);
        return payouts.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update a shop payout
     */
    @Transactional
    public ShopPayoutResponseDto updateShopPayout(UUID id, ShopPayoutUpdateDto updateDto) {
        ShopPayout shopPayout = shopPayoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop payout not found with id: " + id));
        
        // Update fields if provided
        if (updateDto.getStatus() != null) {
            shopPayout.setStatus(updateDto.getStatus());
        }
        
        if (updateDto.getAmount() != null) {
            shopPayout.setAmount(updateDto.getAmount());
        }
        
        if (updateDto.getStripeTransferId() != null) {
            shopPayout.setStripeTransferId(updateDto.getStripeTransferId());
        }
        
        // Update timestamp
        shopPayout.setUpdatedAt(LocalDateTime.now());
        
        // Save and return
        ShopPayout updatedPayout = shopPayoutRepository.save(shopPayout);
        return mapToResponseDto(updatedPayout);
    }
    
    /**
     * Calculate total payout amount for a shop
     */
    public BigDecimal calculateTotalPayoutAmountByShopId(UUID shopId) {
        // Verify shop exists
        if (!shopRepository.existsById(shopId)) {
            throw new ResourceNotFoundException("Shop not found with id: " + shopId);
        }
        
        BigDecimal total = shopPayoutRepository.calculateTotalPayoutAmountByShopId(shopId);
        return (total != null) ? total : BigDecimal.ZERO;
    }
    
    /**
     * Calculate total payout amount for a shop within a period
     */
    public BigDecimal calculateTotalPayoutAmountByShopIdAndPeriod(
            UUID shopId, LocalDateTime startDate, LocalDateTime endDate) {
        // Verify shop exists
        if (!shopRepository.existsById(shopId)) {
            throw new ResourceNotFoundException("Shop not found with id: " + shopId);
        }
        
        BigDecimal total = shopPayoutRepository.calculateTotalPayoutAmountByShopIdAndPeriod(
                shopId, startDate, endDate);
        return (total != null) ? total : BigDecimal.ZERO;
    }
    
    /**
     * Delete a shop payout - typically should be avoided in production for financial records
     */
    @Transactional
    public void deleteShopPayout(UUID id) {
        ShopPayout shopPayout = shopPayoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop payout not found with id: " + id));
        shopPayoutRepository.delete(shopPayout);
    }
    
    /**
     * Helper method to map ShopPayout entity to ShopPayoutResponseDto
     */
    private ShopPayoutResponseDto mapToResponseDto(ShopPayout shopPayout) {
        return ShopPayoutResponseDto.builder()
                .id(shopPayout.getId())
                .shopId(shopPayout.getShop().getId())
                .amount(shopPayout.getAmount())
                .stripeTransferId(shopPayout.getStripeTransferId())
                .status(shopPayout.getStatus())
                .periodStart(shopPayout.getPeriodStart())
                .periodEnd(shopPayout.getPeriodEnd())
                .createdAt(shopPayout.getCreatedAt())
                .updatedAt(shopPayout.getUpdatedAt())
                .shopName(shopPayout.getShop().getName())
                .sellerEmail(shopPayout.getShop().getSeller().getEmail()) // Assuming shop has seller with email
                .build();
    }
}