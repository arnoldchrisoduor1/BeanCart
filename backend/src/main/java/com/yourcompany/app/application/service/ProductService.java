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
        // seller validation.
     }
    
}
