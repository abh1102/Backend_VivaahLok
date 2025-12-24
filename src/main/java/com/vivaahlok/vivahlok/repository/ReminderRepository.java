package com.vivaahlok.vivahlok.repository;

import com.vivaahlok.vivahlok.entity.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends MongoRepository<Reminder, String> {
    List<Reminder> findByUserId(String userId);
    
    List<Reminder> findByUserIdAndDate(String userId, LocalDate date);
    
    List<Reminder> findByDateAndPushEnabledTrue(LocalDate date);
    
    void deleteByUserId(String userId);
}
