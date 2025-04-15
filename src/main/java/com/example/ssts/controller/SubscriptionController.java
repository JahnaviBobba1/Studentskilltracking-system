package com.example.ssts.controller;

import com.example.ssts.model.Subscription;
import com.example.ssts.model.User;
import com.example.ssts.service.SubscriptionService;
import com.example.ssts.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, 
                                UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @PostMapping("/subscribe/course/{courseId}")
    public String subscribeToCourse(@PathVariable Long courseId, 
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName());
            subscriptionService.subscribeToCourse(user.getId(), courseId);
            redirectAttributes.addFlashAttribute("success", "Successfully subscribed to course!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to subscribe: " + e.getMessage());
        }
        return "redirect:/courses/all";
    }

    @PostMapping("/subscribe/event/{eventId}")
    public String subscribeToEvent(@PathVariable Long eventId, 
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(principal.getName());
            subscriptionService.subscribeToEvent(user.getId(), eventId);
            redirectAttributes.addFlashAttribute("success", "Successfully subscribed to event!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to subscribe: " + e.getMessage());
        }
        return "redirect:/events/all";
    }
}
