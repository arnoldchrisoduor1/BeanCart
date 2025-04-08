package com.yourcompany.app.application.service;

import com.yourcompany.app.domain.model.Order;
import com.yourcompany.app.domain.model.Payment;
import com.yourcompany.app.domain.repository.OrderRepository;
import com.yourcompany.app.domain.repository.PaymentRepository;
import com.yourcompany.app.application.dto.PaymentCreateDto;
import com.yourcompany.app.application.dto.PaymentResponseDto;
import com.yourcompany.app.application.dto.PaymentUpdateDto;
import com.yourcompany.app.common.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    
    @Autowired
    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }
    
    /**
     * Create a new payment
     */
    @Transactional
    public PaymentResponseDto createPayment(PaymentCreateDto createDto) {
        // Fetch the order
        Order order = orderRepository.findById(createDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + createDto.getOrderId()));
        
        // Create new payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(createDto.getAmount());
        payment.setStripePaymentId(createDto.getStripePaymentId());
        payment.setStripeCustomerId(createDto.getStripeCustomerId());
        payment.setStatus(createDto.getStatus());
        payment.setPaymentMethod(createDto.getPaymentMethod());
        
        // Save and return
        Payment savedPayment = paymentRepository.save(payment);
        return mapToResponseDto(savedPayment);
    }
    
    /**
     * Get a payment by ID
     */
    public PaymentResponseDto getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return mapToResponseDto(payment);
    }
    
    /**
     * Get a payment by Stripe payment ID
     */
    public PaymentResponseDto getPaymentByStripePaymentId(String stripePaymentId) {
        Payment payment = paymentRepository.findByStripePaymentId(stripePaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with Stripe payment id: " + stripePaymentId));
        return mapToResponseDto(payment);
    }
    
    /**
     * Get all payments for a specific order
     */
    public List<PaymentResponseDto> getPaymentsByOrderId(UUID orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        return payments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get payments by status
     */
    public List<PaymentResponseDto> getPaymentsByStatus(String status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get payments by payment method
     */
    public List<PaymentResponseDto> getPaymentsByPaymentMethod(String paymentMethod) {
        List<Payment> payments = paymentRepository.findByPaymentMethod(paymentMethod);
        return payments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get refunded payments
     */
    public List<PaymentResponseDto> getRefundedPayments() {
        List<Payment> payments = paymentRepository.findByRefundedTrue();
        return payments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Update a payment
     */
    @Transactional
    public PaymentResponseDto updatePayment(UUID id, PaymentUpdateDto updateDto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        
        // Update fields if provided
        if (updateDto.getStatus() != null) {
            payment.setStatus(updateDto.getStatus());
        }
        
        if (updateDto.getRefunded() != null) {
            payment.setRefunded(updateDto.getRefunded());
        }
        
        if (updateDto.getRefundReason() != null) {
            payment.setRefundReason(updateDto.getRefundReason());
        }
        
        // Update timestamp
        payment.setUpdatedAt(LocalDateTime.now());
        
        // Save and return
        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponseDto(updatedPayment);
    }
    
    /**
     * Process a refund
     */
    @Transactional
    public PaymentResponseDto processRefund(UUID id, String refundReason) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        
        // Update refund status
        payment.setRefunded(true);
        payment.setRefundReason(refundReason);
        payment.setStatus("refunded");
        payment.setUpdatedAt(LocalDateTime.now());
        
        // Here you would usually call Stripe API to process the refund
        // For now, we'll just update our database
        
        // Save and return
        Payment refundedPayment = paymentRepository.save(payment);
        return mapToResponseDto(refundedPayment);
    }
    
    /**
     * Calculate total paid amount for an order
     */
    public BigDecimal calculateTotalPaidAmountByOrderId(UUID orderId) {
        BigDecimal total = paymentRepository.calculateTotalPaidAmountByOrderId(orderId);
        return (total != null) ? total : BigDecimal.ZERO;
    }
    
    /**
     * Check if an order has any successful payment
     */
    public boolean hasSuccessfulPayment(UUID orderId) {
        return paymentRepository.existsSuccessfulPaymentByOrderId(orderId);
    }
    
    /**
     * Delete a payment - typically should be avoided in production for financial records
     */
    @Transactional
    public void deletePayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        paymentRepository.delete(payment);
    }
    
    /**
     * Helper method to map Payment entity to PaymentResponseDto
     */
    private PaymentResponseDto mapToResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .stripePaymentId(payment.getStripePaymentId())
                .stripeCustomerId(payment.getStripeCustomerId())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .refunded(payment.isRefunded())
                .refundReason(payment.getRefundReason())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .buyerEmail(payment.getOrder().getBuyer().getEmail()) // Assuming order has buyer with email
                .orderTotalAmount(payment.getOrder().getTotalAmount())
                .build();
    }
}