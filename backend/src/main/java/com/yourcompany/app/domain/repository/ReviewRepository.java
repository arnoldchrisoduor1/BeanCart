package com.yourcompany.app.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourcompany.app.domain.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    
    List<Review> findByProductId(UUID productId);
    
    List<Review> findByShopId(UUID shopId);
    
    List<Review> findByBuyerId(UUID buyerId);
    
    Optional<Review> findByOrderItemId(UUID orderItemId);
    
    List<Review> findByProductIdAndRatingGreaterThanEqual(UUID productId, Integer rating);
    
    List<Review> findByShopIdAndRatingGreaterThanEqual(UUID shopId, Integer rating);
    
    Long countByProductId(UUID productId);
    
    Long countByShopId(UUID shopId);
}