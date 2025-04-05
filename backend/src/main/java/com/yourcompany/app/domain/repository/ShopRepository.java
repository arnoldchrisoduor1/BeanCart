package com.yourcompany.app.domain.repository;

import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.model.User;

import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    // Find shop by ID
    Optional<Shop> findById(UUID id);

    // Find all shops by seller
    List<Shop> findBySeller(User seller);

    // Finding all active shops.
    List<Shop> findByActiveTrue();

    // Finding all active shops by seller.
    List<Shop> findBySellerAndActiveTrue(User seller);

    // Find shop by email.
    Optional<Shop> findByEmail(String email);

    // Find shop by phone number.
    Optional<Shop> findByPhone(String phone);

    // Custom query to find shops by name containing a string( case insensitive)
    @Query("SELECT s FROM Shop s WHERE LOWER(s.name) LIKE LOWER(concat('%', :name, '%')) AND s.active = true")
    List<Shop> searchByName(@Param("name") String name);

    // Check if a shop with given email exists (excluding a specific shop)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shop s WHERE s.email = :email AND s.id <> :excludeId")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("excludeId") UUID excludeId);

    // Check if a shop with given phone exists (excluding a specific shop)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shop s WHERE s.phone = :phone AND s.id <> :excludeId")
    boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("excludeId") UUID excludeId);
}
