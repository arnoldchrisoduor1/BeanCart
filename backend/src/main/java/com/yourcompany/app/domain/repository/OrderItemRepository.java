package com.yourcompany.app.domain.repository;

import com.yourcompany.app.domain.model.Order;
import com.yourcompany.app.domain.model.OrderItem;
import com.yourcompany.app.domain.model.Product;
import com.yourcompany.app.domain.model.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    // Find order item by ID
    Optional<OrderItem> findById(UUID id);
    
    // Find all order items by order
    List<OrderItem> findByOrder(Order order);
    
    // Find all order items by product
    List<OrderItem> findByProduct(Product product);
    
    // Find all order items by shop
    List<OrderItem> findByShop(Shop shop);
    
    // Find all order items by order and status
    List<OrderItem> findByOrderAndStatus(Order order, String status);
    
    // Find all order items by shop and status
    List<OrderItem> findByShopAndStatus(Shop shop, String status);
    
    // Find order items by order ID
    List<OrderItem> findByOrderId(UUID orderId);
    
    // Find order items by shop ID
    List<OrderItem> findByShopId(UUID shopId);
    
    // Find order items by product ID
    List<OrderItem> findByProductId(UUID productId);
    
    // Count order items by order
    long countByOrder(Order order);
    
    // Count order items by shop
    long countByShop(Shop shop);
    
    // Custom query to find order items with quantity greater than a specific value
    @Query("SELECT oi FROM OrderItem oi WHERE oi.quantity > :quantity")
    List<OrderItem> findByQuantityGreaterThan(@Param("quantity") Integer quantity);
    
    // Custom query to calculate total value of order items for a specific shop
    @Query("SELECT SUM(oi.subtotal) FROM OrderItem oi WHERE oi.shop.id = :shopId AND oi.status = :status")
    Double calculateTotalValueByShopAndStatus(@Param("shopId") UUID shopId, @Param("status") String status);
    
    // Custom query to find order items by order and shop
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.shop.id = :shopId")
    List<OrderItem> findByOrderIdAndShopId(@Param("orderId") UUID orderId, @Param("shopId") UUID shopId);
    
    // Custom query to find popular products based on order quantity
    @Query("SELECT oi.product.id, SUM(oi.quantity) as totalQuantity FROM OrderItem oi GROUP BY oi.product.id ORDER BY totalQuantity DESC")
    List<Object[]> findPopularProducts();
}