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
 * DTO for updating an existing ShopPayout
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopPayoutUpdateDto {
    private String status;
    
    private BigDecimal amount;
    
    private String stripeTransferId;
}