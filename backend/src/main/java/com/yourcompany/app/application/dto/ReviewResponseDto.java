package com.yourcompany.app.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for response with review details
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    
    private UUID id;
    private UUID productId;
    private String productName;
    private UUID shopId;
    private String shopName;
    private UUID buyerId;
    private String buyerName;
    private UUID orderItemId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}