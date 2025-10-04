package org.foo.security.service;

import org.foo.security.dto.DefaultUserDto;
import org.foo.security.entity.Role;
import org.foo.security.entity.User;
import org.foo.security.repository.UserRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    @Cacheable(value = "users", key = "#username")
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
        try {
            // Load default users from JSON file
            ClassPathResource resource = new ClassPathResource("default-users.json");
            List<DefaultUserDto> defaultUsers = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<DefaultUserDto>>() {}
            );

            // Convert DTOs to User entities and save them
            for (DefaultUserDto dto : defaultUsers) {
                User user = User.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .fullName(dto.getFullName())
                    .role(Role.valueOf(dto.getRole()))
                    .build();
                userRepository.save(user);
            }
        } catch (Exception e) {
            // Log error but don't fail application startup
            System.err.println("Failed to load default users from JSON: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @CacheEvict(value = "users", key = "#user.username")
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

    @Cacheable(value = "users", key = "#username")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
