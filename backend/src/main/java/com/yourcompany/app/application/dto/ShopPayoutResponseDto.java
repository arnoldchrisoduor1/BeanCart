package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for returning ShopPayout information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopPayoutResponseDto {
    private UUID id;
    private UUID shopId;
    private BigDecimal amount;
    private String stripeTransferId;
    private String status;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields that might be useful in responses
    private String shopName;
    private String sellerEmail;
}