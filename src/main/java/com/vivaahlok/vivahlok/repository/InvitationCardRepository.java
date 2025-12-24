package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.InvitationCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationCardRepository extends MongoRepository<InvitationCard, String> {
    List<InvitationCard> findByUserId(String userId);
}
