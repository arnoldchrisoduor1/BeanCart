package com.yourcompany.app.interfaces.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestAttribute;

import com.yourcompany.app.application.dto.ReviewCreateDto;
import com.yourcompany.app.application.dto.ReviewResponseDto;
import com.yourcompany.app.application.dto.ReviewUpdateDto;
import com.yourcompany.app.application.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    /**
     * Get all reviews
     */
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
    
    /**
     * Get review by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }
    
    /**
     * Get reviews by product ID
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }
    
    /**
     * Get reviews by shop ID
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByShopId(@PathVariable UUID shopId) {
        return ResponseEntity.ok(reviewService.getReviewsByShopId(shopId));
    }
    
    /**
     * Get reviews by current user
     */
    @GetMapping("/my-reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(@RequestAttribute("userId") UUID userId) {
        return ResponseEntity.ok(reviewService.getReviewsByBuyerId(userId));
    }
    
    /**
     * Create a new review
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestAttribute("userId") UUID userId,
            @Valid @RequestBody ReviewCreateDto createDto) {
        
        ReviewResponseDto createdReview = reviewService.createReview(userId, createDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }
    
    /**
     * Update an existing review
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable UUID id,
            @RequestAttribute("userId") UUID userId,
            @Valid @RequestBody ReviewUpdateDto updateDto) {
        
        ReviewResponseDto updatedReview = reviewService.updateReview(id, userId, updateDto);
        return ResponseEntity.ok(updatedReview);
    }
    
    /**
     * Delete a review
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID id,
            @RequestAttribute("userId") UUID userId) {
        
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }
}