package com.yourcompany.app.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourcompany.app.common.exception.BadRequestException;
import com.yourcompany.app.common.exception.ResourceNotFoundException;
import com.yourcompany.app.application.dto.ReviewCreateDto;
import com.yourcompany.app.application.dto.ReviewResponseDto;
import com.yourcompany.app.application.dto.ReviewUpdateDto;
import com.yourcompany.app.domain.model.Review;
import com.yourcompany.app.domain.model.Product;
import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.model.User;
import com.yourcompany.app.domain.repository.ReviewRepository;
import com.yourcompany.app.domain.repository.ProductRepository;
import com.yourcompany.app.domain.repository.ShopRepository;
import com.yourcompany.app.domain.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all reviews
     */
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get review by ID
     */
    public ReviewResponseDto getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        return mapToResponseDto(review);
    }
    
    /**
     * Get reviews by product ID
     */
    public List<ReviewResponseDto> getReviewsByProductId(UUID productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get reviews by shop ID
     */
    public List<ReviewResponseDto> getReviewsByShopId(UUID shopId) {
        return reviewRepository.findByShopId(shopId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get reviews by buyer ID
     */
    public List<ReviewResponseDto> getReviewsByBuyerId(UUID buyerId) {
        return reviewRepository.findByBuyerId(buyerId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new review
     */
    @Transactional
    public ReviewResponseDto createReview(UUID buyerId, ReviewCreateDto createDto) {
        // Check if product exists
        productRepository.findById(createDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + createDto.getProductId()));
        
        // Check if shop exists
        shopRepository.findById(createDto.getShopId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + createDto.getShopId()));
        
        // Check if user exists
        userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + buyerId));
        
        // Check if review for this order item already exists
        reviewRepository.findByOrderItemId(createDto.getOrderItemId()).ifPresent(r -> {
            throw new BadRequestException("Review already exists for this order item");
        });
        
        LocalDateTime now = LocalDateTime.now();
        
        Review review = Review.builder()
                .id(UUID.randomUUID())
                .productId(createDto.getProductId())
                .shopId(createDto.getShopId())
                .buyerId(buyerId)
                .orderItemId(createDto.getOrderItemId())
                .rating(createDto.getRating())
                .comment(createDto.getComment())
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        Review savedReview = reviewRepository.save(review);
        return mapToResponseDto(savedReview);
    }
    
    /**
     * Update an existing review
     */
    @Transactional
    public ReviewResponseDto updateReview(UUID id, UUID buyerId, ReviewUpdateDto updateDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        // Check if the buyer is the owner of the review
        if (!review.getBuyerId().equals(buyerId)) {
            throw new BadRequestException("You can only update your own reviews");
        }
        
        if (updateDto.getRating() != null) {
            review.setRating(updateDto.getRating());
        }
        
        if (updateDto.getComment() != null) {
            review.setComment(updateDto.getComment());
        }
        
        review.setUpdatedAt(LocalDateTime.now());
        
        Review updatedReview = reviewRepository.save(review);
        return mapToResponseDto(updatedReview);
    }
    
    /**
     * Delete a review
     */
    @Transactional
    public void deleteReview(UUID id, UUID buyerId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        // Check if the buyer is the owner of the review
        if (!review.getBuyerId().equals(buyerId)) {
            throw new BadRequestException("You can only delete your own reviews");
        }
        
        reviewRepository.delete(review);
    }
    
    /**
     * Map Review entity to ReviewResponseDto
     */
    private ReviewResponseDto mapToResponseDto(Review review) {
        Product product = review.getProduct();
        Shop shop = review.getShop();
        User buyer = review.getBuyer();
        
        return ReviewResponseDto.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .productName(product != null ? product.getName() : null)
                .shopId(review.getShopId())
                .shopName(shop != null ? shop.getName() : null)
                .buyerId(review.getBuyerId())
                .buyerName(buyer != null ? buyer.getFirstName() + " " + buyer.getLastName() : null)
                .orderItemId(review.getOrderItemId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}