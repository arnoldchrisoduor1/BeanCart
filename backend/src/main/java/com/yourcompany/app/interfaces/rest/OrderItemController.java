package com.yourcompany.app.interfaces.rest;

import com.yourcompany.app.application.dto.OrderItemCreateDto;
import com.yourcompany.app.application.dto.OrderItemResponseDto;
import com.yourcompany.app.application.dto.OrderItemUpdateDto;
import com.yourcompany.app.application.service.OrderItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * Create a new order item
     */
    @PostMapping
    public ResponseEntity<OrderItemResponseDto> createOrderItem(@Valid @RequestBody OrderItemCreateDto createDto) {
        OrderItemResponseDto responseDto = orderItemService.createOrderItem(createDto);
        System.out.println("=========Successfully created new item========");
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Get an order item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponseDto> getOrderItemById(@PathVariable UUID id) {
        OrderItemResponseDto responseDto = orderItemService.getOrderItemById(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Get all order items for a specific order
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItemsByOrderId(@PathVariable UUID orderId) {
        System.out.println("------Getting all orderd for user---------");
        List<OrderItemResponseDto> items = orderItemService.getOrderItemsByOrderId(orderId);
        System.out.println("=======Successfully retrieved orderd for user=========");
        return ResponseEntity.ok(items);
    }

    /**
     * Get all order items for a specific shop
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItemsByShopId(@PathVariable UUID shopId) {
        List<OrderItemResponseDto> items = orderItemService.getOrderItemsByShopId(shopId);
        return ResponseEntity.ok(items);
    }

    /**
     * Get order items for a specific shop with a specific status
     */
    @GetMapping("/shop/{shopId}/status/{status}")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItemsByShopAndStatus(
            @PathVariable UUID shopId, 
            @PathVariable String status) {
        List<OrderItemResponseDto> items = orderItemService.getOrderItemsByShopAndStatus(shopId, status);
        return ResponseEntity.ok(items);
    }

    /**
     * Update an order item
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponseDto> updateOrderItem(
            @PathVariable UUID id, 
            @Valid @RequestBody OrderItemUpdateDto updateDto) {
        OrderItemResponseDto responseDto = orderItemService.updateOrderItem(id, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Update order item status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderItemResponseDto> updateOrderItemStatus(
            @PathVariable UUID id, 
            @RequestParam String status) {
        OrderItemResponseDto responseDto = orderItemService.updateOrderItemStatus(id, status);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Delete an order item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable UUID id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calculate total value of order items for a shop with specific status
     */
    @GetMapping("/shop/{shopId}/status/{status}/total")
    public ResponseEntity<BigDecimal> calculateTotalValueByShopAndStatus(
            @PathVariable UUID shopId, 
            @PathVariable String status) {
        BigDecimal total = orderItemService.calculateTotalValueByShopAndStatus(shopId, status);
        return ResponseEntity.ok(total);
    }
}