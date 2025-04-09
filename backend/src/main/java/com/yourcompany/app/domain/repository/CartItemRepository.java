package com.yourcompany.app.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yourcompany.app.domain.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    
    List<CartItem> findByCartId(UUID cartId);
    
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);
    
    void deleteByCartId(UUID cartId);
    
    boolean existsByCartIdAndProductId(UUID cartId, UUID productId);
}