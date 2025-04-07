package com.example.ssts.controller;

import com.example.ssts.model.Event;
import com.example.ssts.service.EventService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String getAllEvents(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Event> events = eventService.getAllEvents();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        
        model.addAttribute("events", events);
        model.addAttribute("isAdmin", isAdmin);
        return "events";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping("/save")
    public String saveEvent(@ModelAttribute Event event, RedirectAttributes redirectAttributes) {
        try {
            eventService.createEvent(event);
            redirectAttributes.addFlashAttribute("success", "Event created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create event: " + e.getMessage());
        }
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        return "event-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("success", "Event deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete event: " + e.getMessage());
        }
        return "redirect:/events";
    }

    @PostMapping("/subscribe/{eventId}")
    public String subscribeToEvent(@PathVariable Long eventId, 
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            String username = userDetails.getUsername();
            eventService.subscribeToEvent(username, eventId);
            redirectAttributes.addFlashAttribute("success", "Subscribed to event successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to subscribe: " + e.getMessage());
        }
        return "redirect:/events";
    }
}