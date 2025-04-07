package com.example.ssts.controller;

import com.example.ssts.model.Event;  // Import Event class
import com.example.ssts.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;          // Import List interface
import java.util.Collections;   // Import Collections utility class

@Controller
public class HomeController {

    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextMonth = now.plusDays(30);
            List<Event> events = eventService.getEventsBetween(now, nextMonth);
            model.addAttribute("events", events != null ? events : Collections.emptyList());
            return "index";
        } catch (Exception e) {
            model.addAttribute("events", Collections.emptyList());
            return "index";
        }
    }
}
