package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new OrderItem
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateDto {
    @NotNull(message = "Order ID is required")
    private UUID orderId;
    
    @NotNull(message = "Product ID is required")
    private UUID productId;
    
    @NotNull(message = "Shop ID is required")
    private UUID shopId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
    
    private String status = "pending";
}