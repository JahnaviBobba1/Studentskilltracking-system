package com.example.ssts.controller;

import com.example.ssts.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Get events for the next 30 days
        LocalDate now = LocalDate.now();
        LocalDate nextMonth = now.plusDays(30);
        
        List<Event> events = eventService.getEventsBetween(now, nextMonth);
        model.addAttribute("events", events);
        return "index";
    }
}