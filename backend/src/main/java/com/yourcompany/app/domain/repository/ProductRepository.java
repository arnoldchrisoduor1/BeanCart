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
    // Find product by ID (already provided by JpaRepository)
    Optional<Product> findById(UUID id);
    
    // Find all products by specific shop
    List<Product> findByShop(Shop shop);
    
    // Find all active Products
    List<Product> findByActiveTrue();
    
    // Find all active products for a specific shop
    List<Product> findByShopAndActiveTrue(Shop shop);
    
    // Find products by category
    List<Product> findByCategory(String category);
    
    // Find all featured products (corrected from IsFeaturesTrue)
    List<Product> findByIsFeaturedTrue();
    
    // Find featured products by specific shop
    List<Product> findByShopAndIsFeaturedTrue(Shop shop);
    
    // Find active featured products by specific shop
    List<Product> findByShopAndIsFeaturedTrueAndActiveTrue(Shop shop);
    
    // Find products by category in a specific shop
    List<Product> findByShopAndCategory(Shop shop, String category);
    
    // Find products by name
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    // Find products with stock available
    List<Product> findByQuantityGreaterThan(Integer minQuantity);
    
    // Find products with price between min and max
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Custom query to find products with discounts
    @Query("SELECT p FROM Product p WHERE p.discount IS NOT NULL AND p.discount > 0")
    List<Product> findProductsWithDiscount();
    
    // Find products with discounts for a specific shop
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND p.discount IS NOT NULL AND p.discount > 0")
    List<Product> findProductsWithDiscountByShop(Shop shop);
    
    // Custom query to search for products by name, description or category
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(String keyword);
    
    // Search products within a specific shop
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND (" +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchProductsByShop(Shop shop, String keyword);
    
    // Custom query to find low stock products
    @Query("SELECT p FROM Product p WHERE p.quantity <= :threshold AND p.active = true")
    List<Product> findLowStockProducts(Integer threshold);
    
    // Find low stock products for a specific shop
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND p.quantity <= :threshold AND p.active = true")
    List<Product> findLowStockProductsByShop(Shop shop, Integer threshold);
    
    // Find recently added products
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findRecentProducts(org.springframework.data.domain.Pageable pageable);
    
    // Find best selling products (you would need to implement logic to track sales)
    // This is just a placeholder - you would need a sales or orderItems relation
    @Query(value = "SELECT p.* FROM products p " +
                  "JOIN order_items oi ON p.id = oi.product_id " +
                  "GROUP BY p.id " +
                  "ORDER BY SUM(oi.quantity) DESC", 
           nativeQuery = true)
    List<Product> findBestSellingProducts(org.springframework.data.domain.Pageable pageable);
    
    // Find products with upcoming stock depletion (low stock and active)
    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.quantity <= :threshold AND p.active = true ORDER BY p.quantity ASC")
    List<Product> findProductsWithUpcomingStockDepletion(Integer threshold);
    
    // Find products by price range and category
    List<Product> findByPriceBetweenAndCategory(BigDecimal minPrice, BigDecimal maxPrice, String category);
    
    // Find active products by category in a specific shop
    List<Product> findByShopAndCategoryAndActiveTrue(Shop shop, String category);
    
    // Find products with effective discounts (calculated field)
    @Query("SELECT p FROM Product p WHERE (p.price - p.discount) / p.price >= :discountPercentage")
    List<Product> findProductsWithMinimumDiscountPercentage(Double discountPercentage);
    
    // Find products updated since a certain date
    List<Product> findByUpdatedAtAfter(java.time.LocalDateTime date);
}
