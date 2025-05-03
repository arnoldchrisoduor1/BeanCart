package com.yourcompany.app.domain.repository;

import com.yourcompany.app.domain.model.Wishlist;
import com.yourcompany.app.domain.model.WishlistItem;
import com.yourcompany.app.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    // Wishlist queries
    List<Wishlist> findByUserId(UUID userId);
    
    Optional<Wishlist> findByIdAndUserId(UUID id, UUID userId);
    
    boolean existsByUserIdAndNameIgnoreCase(UUID userId, String name);
    
    Optional<Wishlist> findByUserIdAndNameIgnoreCase(UUID userId, String name);
    
    List<Wishlist> findByUserIdAndIsPublic(UUID userId, boolean isPublic);
    
    @Query("SELECT w FROM Wishlist w WHERE w.isPublic = true")
    List<Wishlist> findAllPublicWishlists();
    
    @Query("SELECT COUNT(w) > 0 FROM Wishlist w WHERE w.id = :wishlistId AND (w.userId = :userId OR w.isPublic = true)")
    boolean isWishlistAccessible(@Param("userId") UUID userId, @Param("wishlistId") UUID wishlistId);

    // WishlistItem queries
    @Query("SELECT wi FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId")
    List<WishlistItem> findItemsByWishlistId(@Param("wishlistId") UUID wishlistId);
    
    @Query("SELECT wi FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId AND wi.product.id = :productId")
    Optional<WishlistItem> findItemByWishlistAndProduct(@Param("wishlistId") UUID wishlistId, 
                                                      @Param("productId") UUID productId);
    
    @Query("SELECT CASE WHEN COUNT(wi) > 0 THEN true ELSE false END " +
           "FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId AND wi.product.id = :productId")
    boolean existsItemByWishlistAndProduct(@Param("wishlistId") UUID wishlistId, 
                                         @Param("productId") UUID productId);
    
    @Query("SELECT wi FROM WishlistItem wi JOIN wi.wishlist w " +
           "WHERE w.userId = :userId AND wi.product.id = :productId")
    List<WishlistItem> findItemsByUserAndProduct(@Param("userId") UUID userId, 
                                               @Param("productId") UUID productId);
    
    @Query("SELECT CASE WHEN COUNT(wi) > 0 THEN true ELSE false END " +
           "FROM WishlistItem wi JOIN wi.wishlist w " +
           "WHERE w.userId = :userId AND wi.product.id = :productId")
    boolean isProductInAnyUserWishlist(@Param("userId") UUID userId, 
                                     @Param("productId") UUID productId);
    
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId AND wi.product.id = :productId")
    void deleteItemByWishlistAndProduct(@Param("wishlistId") UUID wishlistId, 
                                       @Param("productId") UUID productId);
    
    @Query("SELECT p FROM Product p JOIN WishlistItem wi ON p.id = wi.product.id " +
           "WHERE wi.wishlist.id = :wishlistId")
    List<Product> findProductsInWishlist(@Param("wishlistId") UUID wishlistId);
    
    @Query("SELECT p FROM Product p JOIN WishlistItem wi ON p.id = wi.product.id " +
           "JOIN wi.wishlist w WHERE w.userId = :userId")
    List<Product> findProductsInUserWishlists(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(wi) FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId")
    int countItemsInWishlist(@Param("wishlistId") UUID wishlistId);
    
    @Query("SELECT w FROM Wishlist w JOIN w.items wi WHERE wi.product.id = :productId")
    List<Wishlist> findWishlistsContainingProduct(@Param("productId") UUID productId);
}