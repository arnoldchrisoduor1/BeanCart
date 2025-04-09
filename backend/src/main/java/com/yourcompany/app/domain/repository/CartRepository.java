package com.yourcompany.app.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourcompany.app.domain.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    
    Optional<Cart> findByBuyerId(UUID buyerId);
    
    boolean existsByBuyerId(UUID buyerId);
}