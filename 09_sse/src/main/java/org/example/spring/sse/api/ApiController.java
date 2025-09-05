package org.example.spring.sse.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.spring.sse.sse.SseService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final SseService sseService;

    /**
     * SSE endpoint for client subscriptions.
     * Returns a Server-Sent Events stream.
     */
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToEvents() {
        return sseService.subscribe();
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
            "status", "running",
            "service", "sse-demo",
            "endpoint", "/api/events",
            "activeClients", sseService.getActiveClientCount(),
            "description", "Server-Sent Events demonstration"
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "healthy",
            "uptime", "running",
            "activeClients", sseService.getActiveClientCount()
        );
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
            "name", "SSE Demo",
            "description", "Server-Sent Events demonstration with real-time updates",
            "features", new String[]{
                "Server-to-client communication",
                "Real-time event streaming",
                "Automatic reconnection",
                "Event type filtering"
            },
            "endpoints", Map.of(
                "sse", "/api/events",
                "web", "/sse",
                "api_status", "/api/status",
                "api_health", "/api/health"
            )
        );
    }
}
