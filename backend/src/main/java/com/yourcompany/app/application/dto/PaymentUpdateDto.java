package com.yourcompany.app.application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for updating an existing Payment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDto {
    private String status;
    
    private Boolean refunded;
    
    private String refundReason;
}