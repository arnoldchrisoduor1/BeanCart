package com.yourcompany.app.interfaces.rest;

import com.yourcompany.app.application.dto.PaymentCreateDto;
import com.yourcompany.app.application.dto.PaymentResponseDto;
import com.yourcompany.app.application.dto.PaymentUpdateDto;
import com.yourcompany.app.application.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Create a new payment
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@Valid @RequestBody PaymentCreateDto createDto) {
        PaymentResponseDto responseDto = paymentService.createPayment(createDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Get a payment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable UUID id) {
        PaymentResponseDto responseDto = paymentService.getPaymentById(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Get a payment by Stripe payment ID
     */
    @GetMapping("/stripe/{stripePaymentId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByStripePaymentId(@PathVariable String stripePaymentId) {
        PaymentResponseDto responseDto = paymentService.getPaymentByStripePaymentId(stripePaymentId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Get all payments for a specific order
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrderId(@PathVariable UUID orderId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payments by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByStatus(@PathVariable String status) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payments by payment method
     */
    @GetMapping("/method/{paymentMethod}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByPaymentMethod(@PathVariable String paymentMethod) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get refunded payments
     */
    @GetMapping("/refunded")
    public ResponseEntity<List<PaymentResponseDto>> getRefundedPayments() {
        List<PaymentResponseDto> payments = paymentService.getRefundedPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Update a payment
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> updatePayment(
            @PathVariable UUID id, 
            @Valid @RequestBody PaymentUpdateDto updateDto) {
        PaymentResponseDto responseDto = paymentService.updatePayment(id, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Process a refund
     */
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDto> processRefund(
            @PathVariable UUID id, 
            @RequestParam String refundReason) {
        PaymentResponseDto responseDto = paymentService.processRefund(id, refundReason);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Calculate total paid amount for an order
     */
    @GetMapping("/order/{orderId}/total")
    public ResponseEntity<BigDecimal> calculateTotalPaidAmountByOrderId(@PathVariable UUID orderId) {
        BigDecimal total = paymentService.calculateTotalPaidAmountByOrderId(orderId);
        return ResponseEntity.ok(total);
    }

    /**
     * Check if an order has any successful payment
     */
    @GetMapping("/order/{orderId}/has-payment")
    public ResponseEntity<Boolean> hasSuccessfulPayment(@PathVariable UUID orderId) {
        boolean hasPaid = paymentService.hasSuccessfulPayment(orderId);
        return ResponseEntity.ok(hasPaid);
    }

    /**
     * Delete a payment - typically should be avoided in production for financial records
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}