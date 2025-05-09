package com.yourcompany.app.interfaces.rest;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.yourcompany.app.application.dto.OrderResponseDto;
import com.yourcompany.app.application.dto.PaymentCreateDto;
import com.yourcompany.app.application.dto.PaymentResponseDto;
import com.yourcompany.app.application.service.OrderService;
import com.yourcompany.app.application.service.OrderItemService;
import com.yourcompany.app.application.service.PaymentService;
import com.yourcompany.app.common.exception.PaymentProcessingException;
import com.yourcompany.app.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class StripePaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    
    @Autowired
    public StripePaymentController(PaymentService paymentService, OrderService orderService, OrderItemService orderItemService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }
    
    @PostMapping("/create-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> payload) {
        try {
            // Extract parameters from payload
            System.out.println("-----creating payment intent-------");
            Double amount = Double.parseDouble(payload.get("amount").toString());
            String currency = (String) payload.get("currency");
            String orderId = (String) payload.get("orderId");
            
            // Validate order exists
            OrderResponseDto order = orderService.getOrderById(UUID.fromString(orderId));
            System.out.println("-----Orders to be payed for: " + order);

            
            // Create a PaymentIntent with the order amount and currency
            Long amountInCents = Math.round(amount * 100);
            
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency)
                .setDescription("Payment for Order #" + orderId)
                .putMetadata("orderId", orderId)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods
                        .builder()
                        .setEnabled(true)
                        .build()
                )
                .build();
                
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            System.out.println("=====Payment intent successfully created====");
            
            // Return the client secret and payment intent ID to the client
            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());
            responseData.put("paymentIntentId", paymentIntent.getId());

            System.out.println("====Returning secret and payment intent ID=====");
            
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            throw new PaymentProcessingException("Error creating payment intent: " + e.getMessage());
        }
    }
    
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> payload) {
        try {

            System.out.println("-----Attempting to confirm payment----");
            String paymentIntentId = payload.get("paymentIntentId");
            String orderId = payload.get("orderId");
            
            // Retrieve the payment intent from Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            System.out.println("---Successfully retrieved payment intent from stripe----");
            
            // Check if payment was successful
            if ("succeeded".equals(paymentIntent.getStatus())) {
                // Create payment record in our system
                System.out.println("====Payment was successfull====");
                PaymentCreateDto paymentDto = new PaymentCreateDto();
                paymentDto.setOrderId(UUID.fromString(orderId));
                paymentDto.setAmount(BigDecimal.valueOf(paymentIntent.getAmount() / 100.0));
                paymentDto.setStripePaymentId(paymentIntentId);
                paymentDto.setStripeCustomerId(paymentIntent.getCustomer());
                paymentDto.setStatus(paymentIntent.getStatus());
                paymentDto.setPaymentMethod("card"); // This could be dynamic based on what Stripe returns
                
                PaymentResponseDto payment = paymentService.createPayment(paymentDto);
                System.out.println("===Successfully created payment record locally===");
                
                // Update order status to "paid"
                System.out.println("---attempting to update ORDER status----");
                orderService.updateOrderStatus(UUID.fromString(orderId), "paid");
                System.out.println("===successfully updated ORDER status===");

                
                return ResponseEntity.ok(payment);
            } else {
                // Payment was not successful
                System.out.println("xxxxxxxPayment failed  XXX");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Payment not successful", "status", paymentIntent.getStatus()));
            }
        } catch (StripeException e) {
            throw new PaymentProcessingException("Error confirming payment: " + e.getMessage());
        }
    }
    
    @GetMapping("/status/{paymentIntentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", paymentIntent.getStatus());
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            throw new PaymentProcessingException("Error retrieving payment status: " + e.getMessage());
        }
    }
    
    // Webhook handler for Stripe events
    @PostMapping("/webhook")
    public ResponseEntity<?> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        // This is a simplified version. In production, you should verify the signature
        // and handle various event types (payment_intent.succeeded, payment_intent.failed, etc.)
        
        return ResponseEntity.ok().build();
    }
}