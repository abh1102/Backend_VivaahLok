package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByIsActiveTrueOrderBySortOrderAsc();
}
