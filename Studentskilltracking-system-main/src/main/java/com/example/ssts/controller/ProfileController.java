package com.example.ssts.controller;

import com.example.ssts.service.CourseService;
import com.example.ssts.service.EventService;
import com.example.ssts.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final EventService eventService;
    private final CourseService courseService;

    public ProfileController(UserService userService, EventService eventService, CourseService courseService) {
        this.userService = userService;
        this.eventService = eventService;
        this.courseService = courseService;
    }

    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        
        model.addAttribute("user", userService.getUserByUsername(username));
        model.addAttribute("subscribedEvents", eventService.getUserSubscribedEvents(username));
        model.addAttribute("subscribedCourses", courseService.getUserSubscribedCourses(username));
        model.addAttribute("eventCount", eventService.getUserEventCount(username));
        model.addAttribute("courseCount", courseService.getUserCourseCount(username));
        
        return "profile";
    }
}