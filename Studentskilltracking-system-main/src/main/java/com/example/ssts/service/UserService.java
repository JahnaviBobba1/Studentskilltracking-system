package com.example.ssts.service;

import com.example.ssts.model.User;
import com.example.ssts.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inject PasswordEncoder
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Log the incoming authentication request
        System.out.println("\n===== Authentication Debug Start =====");
        System.out.println("[DEBUG] Attempting to authenticate user: " + username);

        try {
            // 2. Find user in database
            System.out.println("[DEBUG] Searching database for username: " + username);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("[WARN] User not found in database: " + username);
                    return new UsernameNotFoundException("User not found");
                });

            // 3. Log user details (excluding password for security)
            System.out.println("[DEBUG] Found user record:");
            System.out.println("  • Username: " + user.getUsername());
            System.out.println("  • Roles: " + 
                user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));

            // 4. Map roles to authorities with debug output
            System.out.println("[DEBUG] Mapping roles to authorities:");
            Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .peek(role -> System.out.println("  • Processing role: " + role.getName()))
                .map(role -> {
                    String authority = "ROLE_" + role.getName();
                    System.out.println("    → Mapped to authority: " + authority);
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toList());

            // 5. Final verification before authentication
            System.out.println("[DEBUG] Final security details:");
            System.out.println("  • Username: " + user.getUsername());
            System.out.println("  • Password: [PROTECTED]");
            System.out.println("  • Granted Authorities: " + authorities);
            System.out.println("===== Authentication Debug End =====\n");

            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
            );

        } catch (Exception e) {
            System.out.println("[ERROR] Authentication failed for user: " + username);
            System.out.println("  • Reason: " + e.getMessage());
            throw e;
        }
    }

    public void registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}