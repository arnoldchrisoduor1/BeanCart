package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new ShopPayout
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopPayoutCreateDto {
    @NotNull(message = "Shop ID is required")
    private UUID shopId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotBlank(message = "Stripe transfer ID is required")
    private String stripeTransferId;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotNull(message = "Period start date is required")
    private LocalDateTime periodStart;
    
    @NotNull(message = "Period end date is required")
    private LocalDateTime periodEnd;
}