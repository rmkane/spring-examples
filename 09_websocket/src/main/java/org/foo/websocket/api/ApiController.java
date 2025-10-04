package org.foo.websocket.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.foo.websocket.model.MessageType;
import org.foo.websocket.utils.TimeUtils;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final String VERSION = "0.1.0-SNAPSHOT";

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
            "status", "running",
            "service", "websocket-fizzbuzz",
            "endpoint", "/websocket/fizzbuzz",
            "timestamp", TimeUtils.getTimestamp(),
            "version", VERSION
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "healthy",
            "uptime", "running",
            "timestamp", TimeUtils.getTimestamp()
        );
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
            "name", "FizzBuzz WebSocket Demo",
            "description", "Real-time WebSocket communication with FizzBuzz algorithm",
            "features", new String[]{
                "WebSocket messaging",
                "FizzBuzz algorithm",
                "Topic-based messages",
                "Real-time statistics"
            },
            "endpoints", Map.of(
                "websocket", "/websocket/fizzbuzz",
                "web", "/fizzbuzz",
                "api_status", "/api/status",
                "api_health", "/api/health"
            )
        );
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
            "currentNumber", "Next FizzBuzz number to be generated",
            "messageInterval", "5 seconds",
            "messageTypes", MessageType.getAllValues(),
            "messageTypeDescriptions", MessageType.getAllWithDescriptions(),
            "algorithm", "FizzBuzz divisibility rules",
            "timestamp", TimeUtils.getTimestamp()
        );
    }

    @GetMapping("/test")
    public Map<String, Object> test() {
        return Map.of(
            "message", "API is working correctly",
            "timestamp", TimeUtils.getTimestamp(),
            "testData", MessageType.getAllWithDescriptions()
        );
    }


}
