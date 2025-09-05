package org.example.spring.sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot application demonstrating Server-Sent Events (SSE).
 * SSE provides one-way communication from server to client over HTTP.
 */
@SpringBootApplication
@EnableScheduling
public class SseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SseApplication.class, args);
    }
}
