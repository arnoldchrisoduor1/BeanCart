package com.yourcompany.app.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

// DTO for creating a new order
public class CreateOrderDTO {
    private UUID buyerId;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String shippingMethod;
    private String trackingNumber;
    private String notes;

    // Constructors
    public CreateOrderDTO() {
    }

    public CreateOrderDTO(UUID buyerId, BigDecimal totalAmount) {
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public UUID getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(UUID buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}