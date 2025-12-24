package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends MongoRepository<Vendor, String> {
    Page<Vendor> findByIsActiveTrue(Pageable pageable);
    
    Page<Vendor> findByCategoryAndIsActiveTrue(String category, Pageable pageable);
    
    List<Vendor> findByCategoryAndIsActiveTrue(String category);
    
    List<Vendor> findByIsFeaturedTrueAndIsActiveTrue();
    
    @Query("{'isActive': true, 'name': {$regex: ?0, $options: 'i'}}")
    List<Vendor> searchByName(String query);
    
    @Query("{'isActive': true, '$or': [{'name': {$regex: ?0, $options: 'i'}}, {'category': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}")
    List<Vendor> searchVendors(String query);
    
    Page<Vendor> findByIsActiveTrueAndPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    
    Page<Vendor> findByCategoryAndIsActiveTrueAndPriceBetween(String category, Double minPrice, Double maxPrice, Pageable pageable);
    
    List<Vendor> findByCityAndIsActiveTrue(String city);
    
    long countByCategoryAndIsActiveTrue(String category);
}
