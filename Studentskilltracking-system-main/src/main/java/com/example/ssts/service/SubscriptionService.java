package com.example.ssts.service;

import com.example.ssts.model.Subscription;
import com.example.ssts.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // Method to subscribe a user
    public Subscription subscribe(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    // Get all subscriptions of a user
    public List<Subscription> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }
}
