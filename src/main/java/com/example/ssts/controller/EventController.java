package com.example.ssts.controller;

import com.example.ssts.model.Event;
import com.example.ssts.model.Subscription;
import com.example.ssts.model.User;
import com.example.ssts.service.EventService;
import com.example.ssts.service.SubscriptionService;
import com.example.ssts.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    // Constructor injection (no @Autowired needed for single constructor)
    public EventController(EventService eventService, 
                         UserService userService,
                         SubscriptionService subscriptionService) {
        this.eventService = eventService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/all")
    public String getAllEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
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