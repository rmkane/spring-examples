package org.example.spring.scheduling.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.scheduling.model.TaskStatistics;
import org.example.spring.scheduling.service.DesyncSchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API controller for scheduling operations
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final DesyncSchedulingService desyncSchedulingService;

    /**
     * Get current task statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<TaskStatistics> getStats() {
        log.debug("📊 API: Getting task statistics");
        TaskStatistics stats = desyncSchedulingService.getTaskStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Reset all task counters
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetCounters() {
        log.info("🔄 API: Resetting task counters");
        desyncSchedulingService.resetCounters();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Task counters reset successfully");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    /**
     * Get application status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "RUNNING");
        status.put("application", "Scheduling Example");
        status.put("timestamp", LocalDateTime.now());
        status.put("description", "Spring Boot scheduling with desync patterns");

        return ResponseEntity.ok(status);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(health);
    }

    /**
     * Get application information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Example 10: Scheduling with Desync Logic");
        info.put("version", "0.1.0-SNAPSHOT");
        info.put("description", "Demonstrates various Spring Boot scheduling patterns");
        info.put("features", new String[]{
            "Fixed Rate Scheduling",
            "Fixed Delay Scheduling",
            "Cron Expressions",
            "Asynchronous Tasks",
            "Conditional Scheduling",
            "Desynchronization Patterns"
        });

        return ResponseEntity.ok(info);
    }
}
