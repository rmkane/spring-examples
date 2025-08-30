package com.example.spring.security.controller;

import com.example.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());
        return "dashboard";
    }

    @GetMapping("/user/profile")
    public String userProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());
        return "user/profile";
    }

    @GetMapping("/moderator/panel")
    public String moderatorPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());
        return "moderator/panel";
    }

    @GetMapping("/admin/panel")
    public String adminPanel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());
        model.addAttribute("users", userService.getAllUsers());
        return "admin/panel";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    // API endpoints for testing different security levels
    @GetMapping("/api/public")
    @ResponseBody
    public Map<String, Object> publicApi() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a public API endpoint");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/api/user/info")
    @ResponseBody
    public Map<String, Object> userApi() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User API endpoint");
        response.put("username", auth.getName());
        response.put("authorities", auth.getAuthorities().stream().map(Object::toString).toList());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/api/moderator/stats")
    @ResponseBody
    public Map<String, Object> moderatorApi() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Moderator API endpoint");
        response.put("username", auth.getName());
        response.put("role", "MODERATOR");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/api/admin/users")
    @ResponseBody
    public Map<String, Object> adminApi() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin API endpoint");
        response.put("username", auth.getName());
        response.put("role", "ADMIN");
        response.put("userCount", userService.getAllUsers().size());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    // Method-level security example
    @GetMapping("/api/secure")
    @ResponseBody
    public Map<String, Object> secureApi() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Secure API endpoint");
        response.put("username", auth.getName());
        response.put("authenticated", auth.isAuthenticated());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
