package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.CardDesign;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardDesignRepository extends MongoRepository<CardDesign, String> {
    List<CardDesign> findByIsActiveTrue();
    
    List<CardDesign> findByCategoryAndIsActiveTrue(String category);
}
