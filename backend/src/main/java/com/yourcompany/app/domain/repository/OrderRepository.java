package com.yourcompany.app.domain.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yourcompany.app.domain.model.Order;
import com.yourcompany.app.domain.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Find order by ID
    Optional<Order> findById(UUID id);
    
    // Find all orders by buyer
    List<Order> findByBuyer(User buyer);
    
    // Find orders by status
    List<Order> findByStatus(String status);
    
    // Find orders by buyer and status
    List<Order> findByBuyerAndStatus(User buyer, String status);
    
    // Find orders created between two dates
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Find orders with total amount greater than a specific value
    List<Order> findByTotalAmountGreaterThan(BigDecimal amount);
    
    // Find orders with total amount less than a specific value
    List<Order> findByTotalAmountLessThan(BigDecimal amount);
    
    // Find orders with tracking number
    List<Order> findByTrackingNumberIsNotNull();
    
    // Find orders without tracking number
    List<Order> findByTrackingNumberIsNull();
    
    // Custom query to find orders containing specific notes (case insensitive)
    @Query("SELECT o FROM Order o WHERE LOWER(o.notes) LIKE LOWER(concat('%', :keyword, '%'))")
    List<Order> searchByNotes(@Param("keyword") String keyword);
    
    // Custom query to find orders with shipping address containing a specific string
    @Query("SELECT o FROM Order o WHERE LOWER(o.shippingAddress) LIKE LOWER(concat('%', :address, '%'))")
    List<Order> searchByShippingAddress(@Param("address") String address);
    
    // Count orders by status
    long countByStatus(String status);
    
    // Count orders by buyer
    long countByBuyer(User buyer);
    
    // Find latest orders for a buyer
    @Query("SELECT o FROM Order o WHERE o.buyer = :buyer ORDER BY o.createdAt DESC")
    List<Order> findLatestOrdersByBuyer(@Param("buyer") User buyer);
    
    // Find orders by buyer and order by created date (newest first)
    List<Order> findByBuyerOrderByCreatedAtDesc(User buyer);
}