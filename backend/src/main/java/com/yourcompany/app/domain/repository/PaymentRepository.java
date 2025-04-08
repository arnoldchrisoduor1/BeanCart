package com.yourcompany.app.domain.repository;

import com.yourcompany.app.domain.model.Order;
import com.yourcompany.app.domain.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    // Find payment by ID
    Optional<Payment> findById(UUID id);
    
    // Find payments by order
    List<Payment> findByOrder(Order order);
    
    // Find payment by order ID
    List<Payment> findByOrderId(UUID orderId);
    
    // Find payment by Stripe payment ID
    Optional<Payment> findByStripePaymentId(String stripePaymentId);
    
    // Find payment by Stripe customer ID
    List<Payment> findByStripeCustomerId(String stripeCustomerId);
    
    // Find payments by status
    List<Payment> findByStatus(String status);
    
    // Find payments by payment method
    List<Payment> findByPaymentMethod(String paymentMethod);
    
    // Find refunded payments
    List<Payment> findByRefundedTrue();
    
    // Find non-refunded payments
    List<Payment> findByRefundedFalse();
    
    // Find payments by order ID and status
    List<Payment> findByOrderIdAndStatus(UUID orderId, String status);
    
    // Custom query to calculate total payment amount by order ID
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.order.id = :orderId AND p.status = 'succeeded'")
    BigDecimal calculateTotalPaidAmountByOrderId(@Param("orderId") UUID orderId);
    
    // Custom query to find payments by amount greater than
    @Query("SELECT p FROM Payment p WHERE p.amount > :amount")
    List<Payment> findByAmountGreaterThan(@Param("amount") BigDecimal amount);
    
    // Custom query to find latest payment for an order
    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId ORDER BY p.createdAt DESC LIMIT 1")
    Optional<Payment> findLatestPaymentByOrderId(@Param("orderId") UUID orderId);
    
    // Custom query to count payments by status
    @Query("SELECT p.status, COUNT(p) FROM Payment p GROUP BY p.status")
    List<Object[]> countPaymentsByStatus();
    
    // Check if any successful payment exists for an order
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.order.id = :orderId AND p.status = 'succeeded'")
    boolean existsSuccessfulPaymentByOrderId(@Param("orderId") UUID orderId);
}