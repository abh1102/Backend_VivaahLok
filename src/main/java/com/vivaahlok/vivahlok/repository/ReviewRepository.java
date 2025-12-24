package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Page<Review> findByVendorId(String vendorId, Pageable pageable);
    
    List<Review> findByVendorId(String vendorId);
    
    List<Review> findByUserId(String userId);
    
    Optional<Review> findByUserIdAndVendorId(String userId, String vendorId);
    
    long countByVendorId(String vendorId);
    
    @Aggregation(pipeline = {
        "{ $match: { vendorId: ?0 } }",
        "{ $group: { _id: null, avgRating: { $avg: '$rating' } } }"
    })
    Double getAverageRatingByVendorId(String vendorId);
}
