package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.VendorInquiry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorInquiryRepository extends MongoRepository<VendorInquiry, String> {
    List<VendorInquiry> findByUserId(String userId);
    
    List<VendorInquiry> findByVendorId(String vendorId);
}
