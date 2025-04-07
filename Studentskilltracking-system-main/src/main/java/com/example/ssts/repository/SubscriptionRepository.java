package com.example.ssts.repository;

import com.example.ssts.model.Subscription;
import com.example.ssts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByUserAndEvent(User user, Event event);
    long countByUser(User user);
}