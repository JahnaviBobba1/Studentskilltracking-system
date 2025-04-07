package com.example.ssts.controller;

import com.example.ssts.model.User;
import com.example.ssts.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user) {
        // Authentication is handled by Spring Security
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
public String registerUser(@ModelAttribute User user,
                         HttpServletRequest request,
                         Model model,
                         RedirectAttributes redirectAttributes) {
    try {
        // Register user
        userService.registerUser(user);
        
        // Authenticate the user automatically
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
        
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        // Create session
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        
        redirectAttributes.addFlashAttribute("success", "Welcome!");
        return "redirect:/courses";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "register";
    }
}
}