package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning Payment information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private UUID id;
    private UUID orderId;
    private BigDecimal amount;
    private String stripePaymentId;
    private String stripeCustomerId;
    private String status;
    private String paymentMethod;
    private boolean refunded;
    private String refundReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields that might be useful in responses
    private String buyerEmail;
    private BigDecimal orderTotalAmount;
}