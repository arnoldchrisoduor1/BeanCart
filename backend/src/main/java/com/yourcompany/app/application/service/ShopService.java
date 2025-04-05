package com.yourcompany.app.application.service;

import com.yourcompany.app.domain.model.Shop;
import com.yourcompany.app.domain.model.User;
import com.yourcompany.app.domain.repository.ShopRepository;
import com.yourcompany.app.domain.repository.UserRepository;
import com.yourcompany.app.application.dto.ShopCreateDto;
import com.yourcompany.app.application.dto.ShopResponseDto;
import com.yourcompany.app.application.dto.ShopUpdateDto;
import com.yourcompany.app.common.exception.BadRequestException;
import com.yourcompany.app.common.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ShopService {
    
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShopService(ShopRepository shopRepository, UserRepository userRepository) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new shop
     * @param shopDto Data transfer object containing shop information.
     * @return Response DTO with created shop details.
     */

     @Transactional
     public ShopResponseDto createShop(ShopCreateDto shopDto) {
        // Seller validation.
        User seller = userRepository.findById(shopDto.getSellerId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + shopDto.getSellerId()));

        // validate the unique email and phone.
        validateUniqueFields(null, shopDto.getEmail(), shopDto.getPhone());

        // converting FTO to entity and saving.
        Shop shop = shopDto.toEntity(seller);
        Shop savedShop = shopRepository.save(shop);

        return new ShopResponseDto(savedShop);
     }

     /**
      * Update an existing shop
      * @param shopDto Data transfer object containing updated shop infrmation.
      * @return Response DTO with updated shop details.
      */
      @Transactional
      public ShopResponseDto updateShop(ShopUpdateDto shopDto) {
        // Finding an existing shop
        Shop existingShop = shopRepository.findById(shopDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Shop not found with ID: " + shopDto.getId()));

        // if they are being updated - validate unique phone and email.
        if(shopDto.getEmail() != null || shopDto.getPhone() != null) {
            validateUniqueFields(existingShop.getId(), shopDto.getEmail() != null ? shopDto.getEmail() : existingShop.getEmail(), shopDto.getPhone() != null ? shopDto.getPhone() : existingShop.getPhone());
        }

        // updating entity with DTO data.
        shopDto.updateEntity(existingShop);

        // Save and return updated shop
        Shop updatedShop = shopRepository.save(existingShop);
        return new ShopResponseDto(updatedShop);
      }

      /**
       * Get shop by ID
       * @param id Shop ID
       * @return Response DTO with shop details
       */
      @Transactional(readOnly = true)
      public ShopResponseDto getShopById(UUID id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shop not found with ID: " + id));
        return new ShopResponseDto(shop);
      }

      /**
       * Get all shops
       * @param activeOnly if true, returns only active shops
       * @return List of shop response DTOs
       */
      @Transactional(readOnly = true)
      public List<ShopResponseDto> getAllShops(boolean activeOnly) {
        List<Shop> shops;

        if(activeOnly) {
            shops = shopRepository.findByActiveTrue();
        } else {
            shops = shopRepository.findAll();
        }

        return shops.stream().map(ShopResponseDto::new).collect(Collectors.toList());
      }

      /**
       * Get all shops by seller ID
       * @param sellerId Seller user ID
       * @param activeOnly if True, returns only active shops
       * @return List of shop response DTOs
       */
      @Transactional(readOnly = true)
      public List<ShopResponseDto> getShopsBySeller(UUID sellerId, boolean activeOnly) {
        // Validate seller exists
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        List<Shop> shops;

        if (activeOnly) {
            shops = shopRepository.findBySellerAndActiveTrue(seller);
        } else {
            shops = shopRepository.findBySeller(seller);
        }
        
        return shops.stream()
                .map(ShopResponseDto::new)
                .collect(Collectors.toList());
      }

      /**
     * Search for shops by name
     * @param name Name to search for (case insensitive, partial match)
     * @return List of matching shop response DTOs
     */
    @Transactional(readOnly = true)
    public List<ShopResponseDto> searchShopsByName(String name) {
        List<Shop> shops = shopRepository.searchByName(name);
        
        return shops.stream()
                .map(ShopResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Verify a shop (set isVerified flag to true)
     * @param shopId Shop ID to verify
     * @return Response DTO with updated shop details
     */
    @Transactional
    public ShopResponseDto verifyShop(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with ID: " + shopId));
        
        shop.setVerified(true);
        Shop updatedShop = shopRepository.save(shop);
        
        return new ShopResponseDto(updatedShop);
    }

    /**
     * Deactivate a shop (set active flag to false)
     * @param shopId Shop ID to deactivate
     * @return Response DTO with updated shop details
     */
    @Transactional
    public ShopResponseDto deactivateShop(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with ID: " + shopId));
        
        shop.setActive(false);
        Shop updatedShop = shopRepository.save(shop);
        
        return new ShopResponseDto(updatedShop);
    }

    /**
     * Reactivate a shop (set active flag to true)
     * @param shopId Shop ID to reactivate
     * @return Response DTO with updated shop details
     */
    @Transactional
    public ShopResponseDto reactivateShop(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with ID: " + shopId));
        
        shop.setActive(true);
        Shop updatedShop = shopRepository.save(shop);
        
        return new ShopResponseDto(updatedShop);
    }

    /**
     * Delete a shop by ID
     * @param shopId Shop ID to delete
     */
    @Transactional
    public void deleteShop(UUID shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with ID: " + shopId));
        
        shopRepository.delete(shop);
    }


     /**
     * Validate that email and phone are unique
     * @param excludeId Shop ID to exclude from the uniqueness check (for updates)
     * @param email Email to check
     * @param phone Phone to check
     * @throws BadRequestException If email or phone already exist
     */
    private void validateUniqueFields(UUID excludeId, String email, String phone) {
        // For new shops (excludeId is null), check if email exists
        if (excludeId == null && shopRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("A shop with this email already exists: " + email);
        }
        
        // For new shops (excludeId is null), check if phone exists
        if (excludeId == null && shopRepository.findByPhone(phone).isPresent()) {
            throw new BadRequestException("A shop with this phone number already exists: " + phone);
        }
        
        // For updates, check if email exists for other shops
        if (excludeId != null && shopRepository.existsByEmailAndIdNot(email, excludeId)) {
            throw new BadRequestException("A shop with this email already exists: " + email);
        }
        
        // For updates, check if phone exists for other shops
        if (excludeId != null && shopRepository.existsByPhoneAndIdNot(phone, excludeId)) {
            throw new BadRequestException("A shop with this phone number already exists: " + phone);
        }
    }
}
