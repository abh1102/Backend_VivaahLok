package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    Page<Booking> findByUserId(String userId, Pageable pageable);
    
    Page<Booking> findByUserIdAndStatus(String userId, String status, Pageable pageable);
    
    List<Booking> findByUserId(String userId);
    
    List<Booking> findByVendorId(String vendorId);
    
    long countByUserId(String userId);
}
