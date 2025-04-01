package com.example.ssts.controller;

import com.example.ssts.model.Subscription;
import com.example.ssts.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    // Subscribe a user to a subscription
    @PostMapping("/subscribe")
    public Subscription subscribe(@RequestBody Subscription subscription) {
        return subscriptionService.subscribe(subscription);
    }

    // Get all subscriptions of a user
    @GetMapping("/user/{userId}")
    public List<Subscription> getUserSubscriptions(@PathVariable Long userId) {
        return subscriptionService.getUserSubscriptions(userId);
    }
}
