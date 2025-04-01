package com.example.ssts.service;

import com.example.ssts.model.User;
import com.example.ssts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new user using email as the unique identifier
    public ResponseEntity<String> registerUser(User user) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // Login a user using email
    public ResponseEntity<String> loginUser(User user) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (existingUser.isEmpty() || !existingUser.get().getPassword().equals(user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        return ResponseEntity.ok("Login successful");
    }

    // Method to get user by ID
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Method to update user profile
    public User updateUserProfile(Long id, User userDetails) {
        User user = getUserById(id); // Get the existing user
        user.setEmail(userDetails.getEmail());  // Update email
        user.setPassword(userDetails.getPassword());  // Update password
        return userRepository.save(user);  // Save the updated user
    }
}
