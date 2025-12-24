package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Occasion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OccasionRepository extends MongoRepository<Occasion, String> {
    List<Occasion> findByIsActiveTrueOrderBySortOrderAsc();
}
