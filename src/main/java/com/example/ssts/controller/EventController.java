package com.example.ssts.controller;

import com.example.ssts.model.Event;
import com.example.ssts.model.Subscription;
import com.example.ssts.model.User;
import com.example.ssts.service.EventService;
import com.example.ssts.service.SubscriptionService;
import com.example.ssts.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public EventController(EventService eventService,
                        SubscriptionService subscriptionService,
                        UserService userService) {
        this.eventService = eventService;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }


    private boolean isAdmin(Principal principal) {
        if (principal == null) return false;
        User user = userService.getUserByUsername(principal.getName());
        return user.getRole().equals(User.ROLE_ADMIN);
    }

    // Constructor injection (no @Autowired needed for single constructor)
    public EventController(EventService eventService, 
                         UserService userService,
                         SubscriptionService subscriptionService) {
        this.eventService = eventService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/all")
    public String getAllEvents(Model model, Principal principal) {
    List<Event> events = eventService.getAllEvents();
    model.addAttribute("events", events);
    
    // Check if admin to show subscriber info
    if (isAdmin(principal)) {
        Map<Long, List<User>> eventSubscribers = new HashMap<>();
        for (Event event : events) {
            List<User> subscribers = subscriptionService.findUsersSubscribedToEvent(event.getId());
            eventSubscribers.put(event.getId(), subscribers);
        }
        model.addAttribute("eventSubscribers", eventSubscribers);
    }
    
    return "events";
}

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("minDate", LocalDateTime.now().plusDays(1));
        return "event-form";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save")
    public String saveEvent(@ModelAttribute Event event) {
        eventService.createEvent(event);
        return "redirect:/events/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        return "event-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events/all";
    }

    @GetMapping("/{id}")
    public String getEventDetails(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        return "event-details";
    }

@PostMapping("/subscribe/event/{eventId}")
public String subscribeToEvent(@PathVariable Long eventId, 
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
    try {
        // Get current user
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        
        // Create and save subscription
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setEvent(eventService.getEventById(eventId));
        subscription.setCourse(null); // Explicitly set course to null
        
        subscriptionService.subscribeToEvent(user.getId(), eventId);
        
        // Add success message
        redirectAttributes.addFlashAttribute("success", 
            "Successfully subscribed to event!");
        
        return "redirect:/events/all";
    } catch (Exception e) {
        // Add error message
        redirectAttributes.addFlashAttribute("error", 
            "Failed to subscribe: " + e.getMessage());
        return "redirect:/events/all";
    }
}
}