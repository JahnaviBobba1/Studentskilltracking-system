package com.example.ssts.controller;

import com.example.ssts.model.Subscription;
import com.example.ssts.service.SubscriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscribe")
    public String subscribe(@ModelAttribute Subscription subscription) {
        subscriptionService.subscribe(subscription);
        return "redirect:/courses/all";
    }

    @GetMapping("/user/{userId}")
    public String getUserSubscriptions(@PathVariable Long userId, Model model) {
        model.addAttribute("subscriptions", 
            subscriptionService.getUserSubscriptions(userId));
        return "user-subscriptions";
    }
}
