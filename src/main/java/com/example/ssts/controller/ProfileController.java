package com.example.ssts.controller;
import com.example.ssts.model.Subscription;
import com.example.ssts.model.User;  // Add this import
import com.example.ssts.service.SubscriptionService;
import com.example.ssts.service.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public ProfileController(UserService userService, 
                           SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        try {
            User user = userService.getUserByUsername(principal.getName());
            model.addAttribute("user", user);
            
            List<Subscription> courseSubscriptions = subscriptionService.findCourseSubscriptionsByUserId(user.getId());
            List<Subscription> eventSubscriptions = subscriptionService.findEventSubscriptionsByUserId(user.getId());
            
            model.addAttribute("courseSubscriptions", 
                courseSubscriptions != null ? courseSubscriptions : Collections.emptyList());
            model.addAttribute("eventSubscriptions", 
                eventSubscriptions != null ? eventSubscriptions : Collections.emptyList());
            
            return "profile";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading profile: " + e.getMessage());
            return "profile";
        }
    }
    
}