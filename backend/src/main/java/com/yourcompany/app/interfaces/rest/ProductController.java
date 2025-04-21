package com.yourcompany.app.interfaces.rest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yourcompany.app.application.dto.ProductCreateDto;
import com.yourcompany.app.application.dto.ProductResponseDto;
import com.yourcompany.app.application.dto.ProductUpdateDto;
import com.yourcompany.app.application.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Create a new product
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            HttpServletRequest request,
            @Valid @RequestBody ProductCreateDto productDto) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        // System.out.println("User uuid" + userId);
        logger.debug("Creating product for shop ID: {} by user ID: {}", productDto.getShopId(), userId);
        
        // TODO: Add authorization check to ensure user owns the shop
        
        ProductResponseDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    
    /**
     * Update an existing product
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            HttpServletRequest request,
            @PathVariable UUID productId,
            @Valid @RequestBody ProductUpdateDto productDto) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("Updating product {} by user {}", productId, userId);
        
        // Ensure the ID in the path matches the ID in the DTO
        productDto.setId(productId);
        
        // TODO: Add authorization check to ensure user owns the product's shop
        
        ProductResponseDto updatedProduct = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Get a product by ID
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID productId) {
        logger.debug("Fetching product with ID: {}", productId);
        
        ProductResponseDto product = productService.getProductById(productId);
        System.out.println("Returning product gotten by id ===============");
        return ResponseEntity.ok(product);
    }
    
    /**
     * Get all products with pagination
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        logger.debug("Fetching all products with pagination: page={}, size={}, sortBy={}, direction={}", 
                page, size, sortBy, direction);
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get all active products
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDto>> getActiveProducts() {
        logger.debug("Fetching all active products");
        
        List<ProductResponseDto> products = productService.getActiveProducts();
        System.out.println("Successfully gotten all active products");
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by shop ID
     */
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByShop(@PathVariable UUID shopId) {
        logger.debug("Fetching products for shop: {}", shopId);
        
        List<ProductResponseDto> products = productService.getProductsByShopId(shopId);
        System.out.println("==== got product by shop id===");
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get active products by shop ID
     */
    @GetMapping("/shop/{shopId}/active")
    public ResponseEntity<List<ProductResponseDto>> getActiveProductsByShop(@PathVariable UUID shopId) {
        logger.debug("Fetching active products for shop: {}", shopId);
        
        List<ProductResponseDto> products = productService.getActiveProductsByShopId(shopId);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String category) {
        logger.debug("Fetching products with category: {}", category);
        
        List<ProductResponseDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by shop and category
     */
    @GetMapping("/shop/{shopId}/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByShopAndCategory(
            @PathVariable UUID shopId, 
            @PathVariable String category) {
        
        logger.debug("Fetching products for shop: {} with category: {}", shopId, category);
        
        List<ProductResponseDto> products = productService.getProductsByShopAndCategory(shopId, category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get active products by shop and category
     */
    @GetMapping("/shop/{shopId}/category/{category}/active")
    public ResponseEntity<List<ProductResponseDto>> getActiveProductsByShopAndCategory(
            @PathVariable UUID shopId, 
            @PathVariable String category) {
        
        logger.debug("Fetching active products for shop: {} with category: {}", shopId, category);
        
        List<ProductResponseDto> products = productService.getActiveProductsByShopAndCategory(shopId, category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get featured products
     */
    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponseDto>> getFeaturedProducts() {
        logger.debug("Fetching featured products");
        
        List<ProductResponseDto> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get featured products by shop
     */
    @GetMapping("/shop/{shopId}/featured")
    public ResponseEntity<List<ProductResponseDto>> getFeaturedProductsByShop(@PathVariable UUID shopId) {
        logger.debug("Fetching featured products for shop: {}", shopId);
        
        List<ProductResponseDto> products = productService.getFeaturedProductsByShopId(shopId);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get active featured products by shop
     */
    @GetMapping("/shop/{shopId}/featured/active")
    public ResponseEntity<List<ProductResponseDto>> getActiveFeaturedProductsByShop(@PathVariable UUID shopId) {
        logger.debug("Fetching active featured products for shop: {}", shopId);
        
        List<ProductResponseDto> products = productService.getActiveFeaturedProductsByShopId(shopId);
        System.out.println("Successfully gotten featured products =======");
        return ResponseEntity.ok(products);
    }
    
    /**
     * Search products by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDto>> searchProductsByName(@RequestParam String keyword) {
        logger.debug("Searching products with name containing: {}", keyword);
        
        List<ProductResponseDto> products = productService.searchProductsByName(keyword);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Search products (name, description, category)
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(@RequestParam String keyword) {
        logger.debug("Searching products with keyword: {}", keyword);
        
        List<ProductResponseDto> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Search products within a shop
     */
    @GetMapping("/shop/{shopId}/search")
    public ResponseEntity<List<ProductResponseDto>> searchProductsByShop(
            @PathVariable UUID shopId,
            @RequestParam String keyword) {
        
        logger.debug("Searching products in shop: {} with keyword: {}", shopId, keyword);
        
        List<ProductResponseDto> products = productService.searchProductsByShop(shopId, keyword);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products with available stock
     */
    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithStock(
            @RequestParam(defaultValue = "0") Integer minQuantity) {
        
        logger.debug("Fetching products with stock > {}", minQuantity);
        
        List<ProductResponseDto> products = productService.getProductsWithStock(minQuantity);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        
        logger.debug("Fetching products with price between {} and {}", minPrice, maxPrice);
        
        List<ProductResponseDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by price range and category
     */
    @GetMapping("/price-range/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRangeAndCategory(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @PathVariable String category) {
        
        logger.debug("Fetching products with price between {} and {} in category {}", 
                minPrice, maxPrice, category);
        
        List<ProductResponseDto> products = productService.getProductsByPriceRangeAndCategory(
                minPrice, maxPrice, category);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products with discounts
     */
    @GetMapping("/discounted")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithDiscount() {
        logger.debug("Fetching products with discounts");
        
        List<ProductResponseDto> products = productService.getProductsWithDiscount();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products with discounts by shop
     */
    @GetMapping("/shop/{shopId}/discounted")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithDiscountByShop(@PathVariable UUID shopId) {
        logger.debug("Fetching discounted products for shop: {}", shopId);
        
        List<ProductResponseDto> products = productService.getProductsWithDiscountByShop(shopId);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products with minimum discount percentage
     */
    @GetMapping("/discount-percentage")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithMinimumDiscountPercentage(
            @RequestParam Double percentage) {
        
        logger.debug("Fetching products with discount percentage >= {}", percentage);
        
        List<ProductResponseDto> products = productService.getProductsWithMinimumDiscountPercentage(percentage);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get low stock products
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts(
            @RequestParam(defaultValue = "5") Integer threshold) {
        
        logger.debug("Fetching low stock products with threshold: {}", threshold);
        
        List<ProductResponseDto> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get low stock products by shop
     */
    @GetMapping("/shop/{shopId}/low-stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProductsByShop(
            @PathVariable UUID shopId,
            @RequestParam(defaultValue = "5") Integer threshold) {
        
        logger.debug("Fetching low stock products for shop: {} with threshold: {}", shopId, threshold);
        
        List<ProductResponseDto> products = productService.getLowStockProductsByShop(shopId, threshold);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products with upcoming stock depletion
     */
    @GetMapping("/upcoming-stock-depletion")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithUpcomingStockDepletion(
            @RequestParam(defaultValue = "5") Integer threshold) {
        
        logger.debug("Fetching products with upcoming stock depletion, threshold: {}", threshold);
        
        List<ProductResponseDto> products = productService.getProductsWithUpcomingStockDepletion(threshold);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get recently added products
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ProductResponseDto>> getRecentProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        logger.debug("Fetching recent products: page={}, size={}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        List<ProductResponseDto> products = productService.getRecentProducts(pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get best selling products
     */
    @GetMapping("/best-selling")
    public ResponseEntity<List<ProductResponseDto>> getBestSellingProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        logger.debug("Fetching best selling products: page={}, size={}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        List<ProductResponseDto> products = productService.getBestSellingProducts(pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products updated since a specific date
     */
    @GetMapping("/updated-since")
    public ResponseEntity<List<ProductResponseDto>> getProductsUpdatedSince(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        
        logger.debug("Fetching products updated since: {}", date);
        
        List<ProductResponseDto> products = productService.getProductsUpdatedSince(date);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Update product quantity
     */
    @PatchMapping("/{productId}/quantity")
    public ResponseEntity<ProductResponseDto> updateProductQuantity(
            HttpServletRequest request,
            @PathVariable UUID productId,
            @RequestParam Integer quantity) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} updating quantity of product {} to {}", userId, productId, quantity);
        
        // TODO: Add authorization check to ensure user owns the product's shop
        
        ProductResponseDto updatedProduct = productService.updateProductQuantity(productId, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Toggle product featured status
     */
    @PatchMapping("/{productId}/toggle-featured")
    public ResponseEntity<ProductResponseDto> toggleFeaturedStatus(
            HttpServletRequest request,
            @PathVariable UUID productId) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} toggling featured status of product {}", userId, productId);
        
        // TODO: Add authorization check to ensure user owns the product's shop
        
        ProductResponseDto updatedProduct = productService.toggleFeaturedStatus(productId);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Toggle product active status
     */
    @PatchMapping("/{productId}/toggle-active")
    public ResponseEntity<ProductResponseDto> toggleActiveStatus(
            HttpServletRequest request,
            @PathVariable UUID productId) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} toggling active status of product {}", userId, productId);
        
        // TODO: Add authorization check to ensure user owns the product's shop
        
        ProductResponseDto updatedProduct = productService.toggleActiveStatus(productId);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Delete a product
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(
            HttpServletRequest request,
            @PathVariable UUID productId) {
        
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("User {} deleting product {}", userId, productId);
        
        // TODO: Add authorization check to ensure user owns the product's shop
        
        productService.deleteProduct(productId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}