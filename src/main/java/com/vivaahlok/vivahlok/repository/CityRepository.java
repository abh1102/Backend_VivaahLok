package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends MongoRepository<City, String> {
    List<City> findByIsActiveTrue();
    
    List<City> findByStateAndIsActiveTrue(String state);
}
