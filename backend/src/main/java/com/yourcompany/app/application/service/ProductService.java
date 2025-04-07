package com.yourcompany.app.application.service;

import com.yourcompany.app.domain.model.Product;
import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.repository.ProductRepository;
import com.yourcompany.app.domain.repository.ShopRepository;
import com.yourcompany.app.application.dto.ProductCreateDto;
import com.yourcompany.app.application.dto.ProductResponseDto;
import com.yourcompany.app.application.dto.ProductUpdateDto;
import com.yourcompany.app.common.exception.BadRequestException;
import com.yourcompany.app.common.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ShopRepository shopRepository) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }

    /**
     * Create a new product
     * @param productDto Data transfer containing product info.
     * @return ResponseDto created with shop details.
     */
    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto productDto) {
        // Shop validation
        Shop shop = shopRepository.findById(productDto.getShopId())
            .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + productDto.getShopId()));
        
        // Convert DTO to entity
        Product product = productDto.toEntity(shop);
        
        // Set default values if needed
        if (product.getQuantity() == null) {
            product.setQuantity(0);
        }
        
        if (product.getDiscount() == null) {
            product.setDiscount(BigDecimal.ZERO);
        }
        
        // Save the product
        Product savedProduct = productRepository.save(product);
        
        // Return the response DTO
        return new ProductResponseDto(savedProduct);
    }
    
    /**
     * Get all products with optional pagination
     * @param pageable Pagination information
     * @return Page of product response DTOs
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponseDto::new);
    }
    
    /**
     * Get all products for a specific shop
     * @param shopId Shop identifier
     * @return List of product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByShopId(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findByShop(shop).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active products
     * @return List of active product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getActiveProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active products for a specific shop
     * @param shopId Shop identifier
     * @return List of active product response DTOs for the shop
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getActiveProductsByShopId(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findByShopAndActiveTrue(shop).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products by category
     * @param category Product category
     * @return List of product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products by category for a specific shop
     * @param shopId Shop identifier
     * @param category Product category
     * @return List of product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByShopAndCategory(UUID shopId, String category) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findByShopAndCategory(shop, category).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get active products by category for a specific shop
     * @param shopId Shop identifier
     * @param category Product category
     * @return List of active product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getActiveProductsByShopAndCategory(UUID shopId, String category) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findByShopAndCategoryAndActiveTrue(shop, category).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get featured products
     * @return List of featured product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get featured products for a specific shop
     * @param shopId Shop identifier
     * @return List of featured product response DTOs for the shop
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getFeaturedProductsByShopId(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findByShopAndIsFeaturedTrue(shop).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get active featured products for a specific shop
     * @param shopId Shop identifier
     * @return List of active featured product response DTOs for the shop
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getActiveFeaturedProductsByShopId(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findByShopAndIsFeaturedTrueAndActiveTrue(shop).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a product by ID
     * @param id Product identifier
     * @return Product response DTO
     */
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        return new ProductResponseDto(product);
    }
    
    /**
     * Update a product
     * @param id Product identifier
     * @param updateDto Data for updating the product
     * @return Updated product response DTO
     */
    @Transactional
    public ProductResponseDto updateProduct(UUID id, ProductUpdateDto updateDto) {
        // Find the product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        // Ensure ID in DTO matches path parameter if provided
        if (updateDto.getId() != null && !updateDto.getId().equals(id)) {
            throw new BadRequestException("ID in request body must match path parameter");
        }
        
        // Update entity from DTO
        updateDto.updateEntity(product);
        
        // Save updated product
        Product updatedProduct = productRepository.save(product);
        
        // Return updated response DTO
        return new ProductResponseDto(updatedProduct);
    }
    
    /**
     * Update product stock quantity
     * @param id Product identifier
     * @param quantity New quantity
     * @return Updated product response DTO
     */
    @Transactional
    public ProductResponseDto updateProductQuantity(UUID id, Integer quantity) {
        if (quantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        product.setQuantity(quantity);
        Product updatedProduct = productRepository.save(product);
        
        return new ProductResponseDto(updatedProduct);
    }
    
    /**
     * Toggle product featured status
     * @param id Product identifier
     * @return Updated product response DTO
     */
    @Transactional
    public ProductResponseDto toggleFeaturedStatus(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        product.setIsFeatured(!product.getIsFeatured());
        Product updatedProduct = productRepository.save(product);
        
        return new ProductResponseDto(updatedProduct);
    }
    
    /**
     * Toggle product active status
     * @param id Product identifier
     * @return Updated product response DTO
     */
    @Transactional
    public ProductResponseDto toggleActiveStatus(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        product.setIsActive(!product.getIsActive());
        Product updatedProduct = productRepository.save(product);
        
        return new ProductResponseDto(updatedProduct);
    }
    
    /**
     * Delete a product
     * @param id Product identifier
     */
    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        
        productRepository.deleteById(id);
    }
    
    /**
     * Search products by name
     * @param keyword Search keyword
     * @return List of matching product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Advanced search for products by name, description or category
     * @param keyword Search keyword
     * @return List of matching product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Search products within a specific shop
     * @param shopId Shop identifier
     * @param keyword Search keyword
     * @return List of matching product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByShop(UUID shopId, String keyword) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.searchProductsByShop(shop, keyword).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products with available stock (quantity > 0)
     * @param minQuantity Minimum quantity threshold
     * @return List of product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsWithStock(Integer minQuantity) {
        return productRepository.findByQuantityGreaterThan(minQuantity).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products within a price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products within a price range and specific category
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param category Product category
     * @return List of product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByPriceRangeAndCategory(BigDecimal minPrice, BigDecimal maxPrice, String category) {
        return productRepository.findByPriceBetweenAndCategory(minPrice, maxPrice, category).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products with discounts
     * @return List of product response DTOs with discounts
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsWithDiscount() {
        return productRepository.findProductsWithDiscount().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products with discounts for a specific shop
     * @param shopId Shop identifier
     * @return List of product response DTOs with discounts for the shop
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsWithDiscountByShop(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findProductsWithDiscountByShop(shop).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products with minimum discount percentage
     * @param discountPercentage Minimum discount percentage
     * @return List of product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsWithMinimumDiscountPercentage(Double discountPercentage) {
        return productRepository.findProductsWithMinimumDiscountPercentage(discountPercentage).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get low stock products
     * @param threshold Low stock threshold
     * @return List of product response DTOs with low stock
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get low stock products for a specific shop
     * @param shopId Shop identifier
     * @param threshold Low stock threshold
     * @return List of product response DTOs with low stock for the shop
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getLowStockProductsByShop(UUID shopId, Integer threshold) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        return productRepository.findLowStockProductsByShop(shop, threshold).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products with upcoming stock depletion
     * @param threshold Low stock threshold
     * @return List of product response DTOs with upcoming stock depletion
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsWithUpcomingStockDepletion(Integer threshold) {
        return productRepository.findProductsWithUpcomingStockDepletion(threshold).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get recently added products
     * @param pageable Pagination information
     * @return List of recently added product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getRecentProducts(Pageable pageable) {
        return productRepository.findRecentProducts(pageable).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get best selling products
     * @param pageable Pagination information
     * @return List of best selling product response DTOs
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getBestSellingProducts(Pageable pageable) {
        return productRepository.findBestSellingProducts(pageable).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products updated since a specific date
     * @param date Reference date
     * @return List of product response DTOs updated since the specified date
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsUpdatedSince(LocalDateTime date) {
        return productRepository.findByUpdatedAtAfter(date).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
}