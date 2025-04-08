package com.yourcompany.app.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for updating an existing OrderItem
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemUpdateDto {
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private String status;
}