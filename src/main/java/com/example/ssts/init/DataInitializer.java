package com.example.ssts.init;

import com.example.ssts.model.User;
import com.example.ssts.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not exists
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123"); // In production, use a more secure password
            admin.setRole(User.ROLE_ADMIN);
            userService.registerUser(admin);
            System.out.println("Admin user created successfully");
        }
    }
}