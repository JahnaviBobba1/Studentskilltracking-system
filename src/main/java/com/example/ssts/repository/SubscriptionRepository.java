package com.example.ssts.repository;

import com.example.ssts.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Custom query to find subscriptions by userId
    List<Subscription> findByUserId(Long userId);
}
