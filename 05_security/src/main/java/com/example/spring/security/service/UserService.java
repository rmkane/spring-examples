package com.example.spring.security.service;

import com.example.spring.security.entity.Role;
import com.example.spring.security.entity.User;
import com.example.spring.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @PostConstruct
    public void initializeUsers() {
        // Only create users if none exist
        if (userRepository.count() == 0) {
            createDefaultUsers();
        }
    }

    private void createDefaultUsers() {
        // Create admin user
        User admin = new User(
            "admin",
            passwordEncoder.encode("admin123"),
            "admin@example.com",
            "Administrator",
            Role.ADMIN
        );
        userRepository.save(admin);

        // Create moderator user
        User moderator = new User(
            "moderator",
            passwordEncoder.encode("mod123"),
            "moderator@example.com",
            "Content Moderator",
            Role.MODERATOR
        );
        userRepository.save(moderator);

        // Create regular user
        User user = new User(
            "user",
            passwordEncoder.encode("user123"),
            "user@example.com",
            "Regular User",
            Role.USER
        );
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
