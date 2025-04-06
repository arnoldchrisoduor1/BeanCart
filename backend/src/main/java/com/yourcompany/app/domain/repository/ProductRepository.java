package com.yourcompany.app.domain.repository;

import com.yourcompany.app.domain.model.Product;
import com.yourcompany.app.domain.model.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    // Find product by ID.
    Optional<Product> findById(UUID id);

    // Find all the products by  specific seller.
    List<Product> findByShop(Shop shop);

    // Find all active Products by shop
    List<Product> findByActiveTrue();

    // Find all active products for a specific shop.
    List<Product> findByShopAndActiveTrue(Shop shop);

    // Find products by category.
    List<Product> findByCategory(String category);

    // Find all featured products.
    List<Product> findByIsFeaturesTrue();

    // Find featured products by specific shop.
    List<Product> findByShopAndIsFeaturedTrue(Shop shop);

    // Find products by category in a specific shop.
    List<Product> findByShopAndCategory(Shop shop, String category);

    // Find products by name.
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Find products with price between min and max.
    List<Product> findByQuantityGreaterThan(Integer minQuantity);

    // Find products wit price between min and max.
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Custom query to find products with discounts.
    @Query("SELECT p FROM Product p WHERE p.discount IS NOT NULL AND p.discount > 0")
    List<Product> findProductWithDiscount();

    // Custom query to search for products by name, description or category.
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(String keyword);

    // Custom query to find low stock products.
    @Query("SELECT p FROM Product p WHERE p.quantity <= :threshold AND p.active = true")
    List<Product> findLowStockProducts(Integer threshold);

}
