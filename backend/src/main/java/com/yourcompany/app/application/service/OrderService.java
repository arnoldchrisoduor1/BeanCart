package com.yourcompany.app.application.service;

import com.yourcompany.app.application.dto.CreateOrderDTO;
import com.yourcompany.app.application.dto.OrderResponseDto;
import com.yourcompany.app.application.dto.OrderUpdateDto;
import com.yourcompany.app.domain.model.Order;
import com.yourcompany.app.domain.model.User;
import com.yourcompany.app.domain.repository.OrderRepository;
import com.yourcompany.app.domain.repository.UserRepository;
import com.yourcompany.app.common.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(CreateOrderDTO createOrderDTO) {
        // Find the buyer
        User buyer = userRepository.findById(createOrderDTO.getBuyerId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + createOrderDTO.getBuyerId()));

        // Create a new order
        Order order = new Order(buyer, createOrderDTO.getTotalAmount());
        
        // Set optional fields if provided
        if (createOrderDTO.getStatus() != null) {
            order.setStatus(createOrderDTO.getStatus());
        }
        if (createOrderDTO.getShippingAddress() != null) {
            order.setShippingAddress(createOrderDTO.getShippingAddress());
        }
        if (createOrderDTO.getShippingMethod() != null) {
            order.setShippingMethod(createOrderDTO.getShippingMethod());
        }
        if (createOrderDTO.getTrackingNumber() != null) {
            order.setTrackingNumber(createOrderDTO.getTrackingNumber());
        }
        if (createOrderDTO.getNotes() != null) {
            order.setNotes(createOrderDTO.getNotes());
        }

        // Save the order
        Order savedOrder = orderRepository.save(order);
        System.out.println("Service: Successfully Created Order");
        
        // Convert to response DTO and return
        return convertToResponseDTO(savedOrder);
    }

    public OrderResponseDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return convertToResponseDTO(order);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    public List<OrderResponseDto> getOrdersByBuyer(UUID buyerId) {
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + buyerId));
        
        return orderRepository.findByBuyer(buyer).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    public List<OrderResponseDto> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(UUID orderId, String status) {
        System.out.println("--- update order status service called--");
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        order.setStatus(status);
        
        Order updatedOrder = orderRepository.save(order);

        System.out.println("==== Successfully updated order payment status=====");

        return convertToResponseDTO(updatedOrder);
}

    @Transactional
    public OrderResponseDto updateOrder(UUID orderId, OrderUpdateDto updateOrderDTO) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        // Update fields if provided
        if (updateOrderDTO.getTotalAmount() != null) {
            order.setTotalAmount(updateOrderDTO.getTotalAmount());
        }
        if (updateOrderDTO.getStatus() != null) {
            order.setStatus(updateOrderDTO.getStatus());
        }
        if (updateOrderDTO.getShippingAddress() != null) {
            order.setShippingAddress(updateOrderDTO.getShippingAddress());
        }
        if (updateOrderDTO.getShippingMethod() != null) {
            order.setShippingMethod(updateOrderDTO.getShippingMethod());
        }
        if (updateOrderDTO.getTrackingNumber() != null) {
            order.setTrackingNumber(updateOrderDTO.getTrackingNumber());
        }
        if (updateOrderDTO.getNotes() != null) {
            order.setNotes(updateOrderDTO.getNotes());
        }
        
        // Save and return
        Order updatedOrder = orderRepository.save(order);
        System.out.println("Service: Successfully Updated Order");
        return convertToResponseDTO(updatedOrder);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        orderRepository.delete(order);
        System.out.println("Service: Successfully Deleted Order");
    }

    public long countOrdersByStatus(String status) {
        return orderRepository.countByStatus(status);
    }

    public List<OrderResponseDto> getLatestOrdersByBuyer(UUID buyerId) {
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + buyerId));
        
        return orderRepository.findByBuyerOrderByCreatedAtDesc(buyer).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    // Helper method to convert Order entity to OrderResponseDto
    private OrderResponseDto convertToResponseDTO(Order order) {
        OrderResponseDto responseDTO = new OrderResponseDto();
        responseDTO.setId(order.getId());
        responseDTO.setBuyerId(order.getBuyer().getId());
        responseDTO.setBuyerEmail(order.getBuyer().getEmail());
        responseDTO.setTotalAmount(order.getTotalAmount());
        responseDTO.setStatus(order.getStatus());
        responseDTO.setShippingAddress(order.getShippingAddress());
        responseDTO.setShippingMethod(order.getShippingMethod());
        responseDTO.setTrackingNumber(order.getTrackingNumber());
        responseDTO.setNotes(order.getNotes());
        responseDTO.setCreatedAt(order.getCreatedAt());
        responseDTO.setUpdatedAt(order.getUpdatedAt());
        return responseDTO;
    }
}