package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    List<Wishlist> findByUserId(String userId);
    
    Optional<Wishlist> findByUserIdAndVendorId(String userId, String vendorId);
    
    void deleteByUserIdAndVendorId(String userId, String vendorId);
    
    boolean existsByUserIdAndVendorId(String userId, String vendorId);
}
