package com.yourcompany.app.application.service;

import com.yourcompany.app.domain.model.Order;
import com.yourcompany.app.domain.model.OrderItem;
import com.yourcompany.app.domain.model.Product;
import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.repository.OrderItemRepository;
import com.yourcompany.app.domain.repository.OrderRepository;
import com.yourcompany.app.domain.repository.ProductRepository;
import com.yourcompany.app.domain.repository.ShopRepository;
import com.yourcompany.app.application.dto.OrderItemCreateDto;
import com.yourcompany.app.application.dto.OrderItemResponseDto;
import com.yourcompany.app.application.dto.OrderItemUpdateDto;
import com.yourcompany.app.common.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public OrderItemService(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository,
            ShopRepository shopRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }

    /**
     * Create a new order item
     */
    @Transactional
    public OrderItemResponseDto createOrderItem(OrderItemCreateDto createDto) {
        // Fetch related entities
        Order order = orderRepository.findById(createDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + createDto.getOrderId()));
        
        Product product = productRepository.findById(createDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + createDto.getProductId()));
        
        Shop shop = shopRepository.findById(createDto.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + createDto.getShopId()));

        // Create new order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setShop(shop);
        orderItem.setQuantity(createDto.getQuantity());
        orderItem.setUnitPrice(createDto.getUnitPrice());
        orderItem.setStatus(createDto.getStatus());
        
        // Calculate subtotal
        BigDecimal subtotal = createDto.getUnitPrice().multiply(new BigDecimal(createDto.getQuantity()));
        orderItem.setSubtotal(subtotal);
        
        // Save and return
        OrderItem savedItem = orderItemRepository.save(orderItem);
        return mapToResponseDto(savedItem);
    }

    /**
     * Get an order item by ID
     */
    public OrderItemResponseDto getOrderItemById(UUID id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));
        return mapToResponseDto(orderItem);
    }

    /**
     * Get all order items for a specific order
     */
    public List<OrderItemResponseDto> getOrderItemsByOrderId(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        return orderItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all order items for a specific shop
     */
    public List<OrderItemResponseDto> getOrderItemsByShopId(UUID shopId) {
        List<OrderItem> orderItems = orderItemRepository.findByShopId(shopId);
        return orderItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Update an order item
     */
    @Transactional
    public OrderItemResponseDto updateOrderItem(UUID id, OrderItemUpdateDto updateDto) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));
        
        // Update fields if provided
        if (updateDto.getQuantity() != null) {
            orderItem.setQuantity(updateDto.getQuantity());
        }
        
        if (updateDto.getUnitPrice() != null) {
            orderItem.setUnitPrice(updateDto.getUnitPrice());
        }
        
        if (updateDto.getStatus() != null) {
            orderItem.setStatus(updateDto.getStatus());
        }
        
        // Recalculate subtotal
        orderItem.setSubtotal(orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        
        // Update timestamp
        orderItem.setUpdatedAt(LocalDateTime.now());
        
        // Save and return
        OrderItem updatedItem = orderItemRepository.save(orderItem);
        return mapToResponseDto(updatedItem);
    }

    /**
     * Delete an order item
     */
    @Transactional
    public void deleteOrderItem(UUID id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));
        orderItemRepository.delete(orderItem);
    }

    /**
     * Update status of an order item
     */
    @Transactional
    public OrderItemResponseDto updateOrderItemStatus(UUID id, String status) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + id));
        
        orderItem.setStatus(status);
        orderItem.setUpdatedAt(LocalDateTime.now());
        
        OrderItem updatedItem = orderItemRepository.save(orderItem);
        return mapToResponseDto(updatedItem);
    }

    /**
     * Get all order items with a specific status for a shop
     */
    public List<OrderItemResponseDto> getOrderItemsByShopAndStatus(UUID shopId, String status) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        List<OrderItem> orderItems = orderItemRepository.findByShopAndStatus(shop, status);
        return orderItems.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Calculate total value of order items for a shop with specific status
     */
    public BigDecimal calculateTotalValueByShopAndStatus(UUID shopId, String status) {
        Double total = orderItemRepository.calculateTotalValueByShopAndStatus(shopId, status);
        return (total != null) ? new BigDecimal(total) : BigDecimal.ZERO;
    }

    /**
     * Helper method to map OrderItem entity to OrderItemResponseDto
     */
    private OrderItemResponseDto mapToResponseDto(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .productId(orderItem.getProduct().getId())
                .shopId(orderItem.getShop().getId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subtotal(orderItem.getSubtotal())
                .status(orderItem.getStatus())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .productName(orderItem.getProduct().getName()) // Assuming Product has getName()
                .shopName(orderItem.getShop().getName())
                .build();
    }
}