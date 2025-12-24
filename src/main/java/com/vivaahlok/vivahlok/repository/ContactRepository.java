package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {
    List<Contact> findByUserId(String userId);
    
    void deleteByUserId(String userId);
}
