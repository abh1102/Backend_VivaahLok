package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends MongoRepository<Facility, String> {
    List<Facility> findByIsActiveTrue();
}
