package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new Payment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateDto {
    @NotNull(message = "Order ID is required")
    private UUID orderId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotBlank(message = "Stripe payment ID is required")
    private String stripePaymentId;
    
    private String stripeCustomerId;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
}