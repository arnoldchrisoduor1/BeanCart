package com.yourcompany.app.domain.repository;

import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.model.ShopPayout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopPayoutRepository extends JpaRepository<ShopPayout, UUID> {
    // Find payout by ID
    Optional<ShopPayout> findById(UUID id);
    
    // Find payouts by shop
    List<ShopPayout> findByShop(Shop shop);
    
    // Find payouts by shop ID
    List<ShopPayout> findByShopId(UUID shopId);
    
    // Find payout by Stripe transfer ID
    Optional<ShopPayout> findByStripeTransferId(String stripeTransferId);
    
    // Find payouts by status
    List<ShopPayout> findByStatus(String status);
    
    // Find payouts by shop ID and status
    List<ShopPayout> findByShopIdAndStatus(UUID shopId, String status);
    
    // Find payouts by shop ID and period dates
    List<ShopPayout> findByShopIdAndPeriodStartAfterAndPeriodEndBefore(
            UUID shopId, LocalDateTime periodStart, LocalDateTime periodEnd);
    
    // Find payouts created within a date range
    List<ShopPayout> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Custom query to calculate total payout amount by shop ID
    @Query("SELECT SUM(sp.amount) FROM ShopPayout sp WHERE sp.shop.id = :shopId AND sp.status = 'completed'")
    BigDecimal calculateTotalPayoutAmountByShopId(@Param("shopId") UUID shopId);
    
    // Custom query to calculate total payout amount by shop ID within a period
    @Query("SELECT SUM(sp.amount) FROM ShopPayout sp WHERE sp.shop.id = :shopId " +
           "AND sp.status = 'completed' AND sp.periodStart >= :startDate AND sp.periodEnd <= :endDate")
    BigDecimal calculateTotalPayoutAmountByShopIdAndPeriod(
            @Param("shopId") UUID shopId, 
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    // Custom query to find shops with highest payout amounts
    @Query("SELECT sp.shop.id, SUM(sp.amount) as totalAmount FROM ShopPayout sp " +
           "WHERE sp.status = 'completed' GROUP BY sp.shop.id ORDER BY totalAmount DESC")
    List<Object[]> findTopShopsByPayoutAmount();
    
    // Custom query to check if a payout period exists for a shop
    @Query("SELECT CASE WHEN COUNT(sp) > 0 THEN true ELSE false END FROM ShopPayout sp " +
           "WHERE sp.shop.id = :shopId AND " +
           "((sp.periodStart <= :periodEnd AND sp.periodEnd >= :periodStart))")
    boolean existsOverlappingPeriodForShop(
            @Param("shopId") UUID shopId,
            @Param("periodStart") LocalDateTime periodStart,
            @Param("periodEnd") LocalDateTime periodEnd);
}