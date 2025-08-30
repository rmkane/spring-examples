package org.example.spring.security.controller;

import org.example.spring.security.entity.Role;
import org.example.spring.security.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    // Role constants
    private static final String ROLE_MODERATOR = Role.MODERATOR.name();
    private static final String ROLE_ADMIN = Role.ADMIN.name();

    // Response field constants
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_AUTHORITIES = "authorities";
    private static final String FIELD_ROLE = "role";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_AUTHENTICATED = "authenticated";
    private static final String FIELD_USER_COUNT = "userCount";

    private final UserService userService;

    // API endpoints for testing different security levels
    @GetMapping("/public")
    @ResponseBody
    public Map<String, Object> publicApi() {
        Map<String, Object> response = new HashMap<>();
        response.put(FIELD_MESSAGE, "This is a public API endpoint");
        response.put(FIELD_TIMESTAMP, System.currentTimeMillis());
        return response;
    }

    @GetMapping("/user/info")
    @ResponseBody
    public Map<String, Object> userApi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put(FIELD_MESSAGE, "User API endpoint");
        response.put(FIELD_USERNAME, authentication.getName());
        response.put(FIELD_AUTHORITIES, authentication.getAuthorities().stream().map(Object::toString).toList());
        response.put(FIELD_TIMESTAMP, System.currentTimeMillis());
        return response;
    }

    @GetMapping("/moderator/stats")
    @ResponseBody
    public Map<String, Object> moderatorApi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put(FIELD_MESSAGE, "Moderator API endpoint");
        response.put(FIELD_USERNAME, authentication.getName());
        response.put(FIELD_ROLE, ROLE_MODERATOR);
        response.put(FIELD_TIMESTAMP, System.currentTimeMillis());
        return response;
    }

    @GetMapping("/admin/users")
    @ResponseBody
    public Map<String, Object> adminApi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put(FIELD_MESSAGE, "Admin API endpoint");
        response.put(FIELD_USERNAME, authentication.getName());
        response.put(FIELD_ROLE, ROLE_ADMIN);
        response.put(FIELD_USER_COUNT, userService.getAllUsers().size());
        response.put(FIELD_TIMESTAMP, System.currentTimeMillis());
        return response;
    }

    // Method-level security example
    @GetMapping("/secure")
    @ResponseBody
    public Map<String, Object> secureApi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put(FIELD_MESSAGE, "Secure API endpoint");
        response.put(FIELD_USERNAME, authentication.getName());
        response.put(FIELD_AUTHENTICATED, authentication.isAuthenticated());
        response.put(FIELD_TIMESTAMP, System.currentTimeMillis());
        return response;
    }
}
