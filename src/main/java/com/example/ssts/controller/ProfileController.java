package com.example.ssts.controller;
import com.example.ssts.model.User;  // Add this import
import com.example.ssts.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model) {
        // In a real app, you'd get the current user's ID from security context
        Long userId = 1L; // Temporary hardcoded - replace with actual user ID
        model.addAttribute("user", userService.getUserById(userId));
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser) {
        userService.updateUserProfile(updatedUser.getId(), updatedUser);
        return "redirect:/profile";
    }
}