package com.yourcompany.app.interfaces.rest;

import com.yourcompany.app.application.dto.CreateOrderDTO;
import com.yourcompany.app.application.dto.OrderResponseDto;
import com.yourcompany.app.application.dto.OrderUpdateDto;
import com.yourcompany.app.application.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        System.out.println("====Creating Orders.............========");
        OrderResponseDto createdOrder = orderService.createOrder(createOrderDTO);
        System.out.println("====Order created successfully========");
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") UUID orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByBuyer(@PathVariable UUID buyerId) {
        List<OrderResponseDto> orders = orderService.getOrdersByBuyer(buyerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByStatus(@PathVariable String status) {
        List<OrderResponseDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable("id") UUID orderId,
            @RequestBody OrderUpdateDto updateOrderDTO) {
        OrderResponseDto updatedOrder = orderService.updateOrder(orderId, updateOrderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countOrdersByStatus(@PathVariable String status) {
        long count = orderService.countOrdersByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/latest/buyer/{buyerId}")
    public ResponseEntity<List<OrderResponseDto>> getLatestOrdersByBuyer(@PathVariable UUID buyerId) {
        List<OrderResponseDto> orders = orderService.getLatestOrdersByBuyer(buyerId);
        return ResponseEntity.ok(orders);
    }
}