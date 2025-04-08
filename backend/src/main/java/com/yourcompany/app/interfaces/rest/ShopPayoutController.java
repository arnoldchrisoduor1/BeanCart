package com.yourcompany.app.interfaces.rest;

import com.yourcompany.app.application.dto.ShopPayoutCreateDto;
import com.yourcompany.app.application.dto.ShopPayoutResponseDto;
import com.yourcompany.app.application.dto.ShopPayoutUpdateDto;
import com.yourcompany.app.application.service.ShopPayoutService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shop-payouts")
public class ShopPayoutController {

    private final ShopPayoutService shopPayoutService;

    @Autowired
    public ShopPayoutController(ShopPayoutService shopPayoutService) {
        this.shopPayoutService = shopPayoutService;
    }

    /**
     * Create a new shop payout
     */
    @PostMapping
    public ResponseEntity<ShopPayoutResponseDto> createShopPayout(@Valid @RequestBody ShopPayoutCreateDto createDto) {
        ShopPayoutResponseDto responseDto = shopPayoutService.createShopPayout(createDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Get a shop payout by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShopPayoutResponseDto> getShopPayoutById(@PathVariable UUID id) {
        ShopPayoutResponseDto responseDto = shopPayoutService.getShopPayoutById(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Get a shop payout by Stripe transfer ID
     */
    @GetMapping("/stripe/{stripeTransferId}")
    public ResponseEntity<ShopPayoutResponseDto> getShopPayoutByStripeTransferId(@PathVariable String stripeTransferId) {
        ShopPayoutResponseDto responseDto = shopPayoutService.getShopPayoutByStripeTransferId(stripeTransferId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Get all payouts for a specific shop
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ShopPayoutResponseDto>> getShopPayoutsByShopId(@PathVariable UUID shopId) {
        List<ShopPayoutResponseDto> payouts = shopPayoutService.getShopPayoutsByShopId(shopId);
        return ResponseEntity.ok(payouts);
    }

    /**
     * Get payouts by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShopPayoutResponseDto>> getShopPayoutsByStatus(@PathVariable String status) {
        List<ShopPayoutResponseDto> payouts = shopPayoutService.getShopPayoutsByStatus(status);
        return ResponseEntity.ok(payouts);
    }

    /**
     * Get payouts by shop ID and status
     */
    @GetMapping("/shop/{shopId}/status/{status}")
    public ResponseEntity<List<ShopPayoutResponseDto>> getShopPayoutsByShopIdAndStatus(
            @PathVariable UUID shopId, 
            @PathVariable String status) {
        List<ShopPayoutResponseDto> payouts = shopPayoutService.getShopPayoutsByShopIdAndStatus(shopId, status);
        return ResponseEntity.ok(payouts);
    }

    /**
     * Get payouts for a shop within a specific period
     */
    @GetMapping("/shop/{shopId}/period")
    public ResponseEntity<List<ShopPayoutResponseDto>> getShopPayoutsByShopIdAndPeriod(
            @PathVariable UUID shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodEnd) {
        List<ShopPayoutResponseDto> payouts = shopPayoutService.getShopPayoutsByShopIdAndPeriod(
                shopId, periodStart, periodEnd);
        return ResponseEntity.ok(payouts);
    }

    /**
     * Update a shop payout
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShopPayoutResponseDto> updateShopPayout(
            @PathVariable UUID id, 
            @Valid @RequestBody ShopPayoutUpdateDto updateDto) {
        ShopPayoutResponseDto responseDto = shopPayoutService.updateShopPayout(id, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Calculate total payout amount for a shop
     */
    @GetMapping("/shop/{shopId}/total")
    public ResponseEntity<BigDecimal> calculateTotalPayoutAmountByShopId(@PathVariable UUID shopId) {
        BigDecimal total = shopPayoutService.calculateTotalPayoutAmountByShopId(shopId);
        return ResponseEntity.ok(total);
    }

    /**
     * Calculate total payout amount for a shop within a period
     */
    @GetMapping("/shop/{shopId}/total/period")
    public ResponseEntity<BigDecimal> calculateTotalPayoutAmountByShopIdAndPeriod(
            @PathVariable UUID shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        BigDecimal total = shopPayoutService.calculateTotalPayoutAmountByShopIdAndPeriod(
                shopId, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    /**
     * Delete a shop payout - typically should be avoided in production for financial records
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShopPayout(@PathVariable UUID id) {
        shopPayoutService.deleteShopPayout(id);
        return ResponseEntity.noContent().build();
    }
}