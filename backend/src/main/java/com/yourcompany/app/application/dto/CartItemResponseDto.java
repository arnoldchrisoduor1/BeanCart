package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDto {
    
    private UUID id;
    private UUID productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private BigDecimal productDiscount;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal totalPrice;
}