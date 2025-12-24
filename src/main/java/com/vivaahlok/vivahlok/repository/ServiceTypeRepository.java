package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.ServiceType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeRepository extends MongoRepository<ServiceType, String> {
    List<ServiceType> findByIsActiveTrue();
}
