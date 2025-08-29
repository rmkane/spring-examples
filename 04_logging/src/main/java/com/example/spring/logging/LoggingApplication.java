package com.example.spring.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LoggingApplication {

    private static final Logger logger = LoggerFactory.getLogger(LoggingApplication.class);
    private static final Logger businessLogger = LoggerFactory.getLogger("business");
    private static final Logger securityLogger = LoggerFactory.getLogger("security");
    private static final Logger performanceLogger = LoggerFactory.getLogger("performance");

    public static void main(String[] args) {
        SpringApplication.run(LoggingApplication.class, args);
        
        // Demonstrate different log levels
        logger.trace("This is a TRACE message - very detailed debugging");
        logger.debug("This is a DEBUG message - debugging information");
        logger.info("This is an INFO message - general information");
        logger.warn("This is a WARN message - warning information");
        logger.error("This is an ERROR message - error information");
        
        // Demonstrate business logging
        businessLogger.info("User login successful: user=john.doe, timestamp=2025-08-29T19:10:00Z");
        businessLogger.warn("User account locked: user=jane.smith, reason=too_many_failed_attempts");
        
        // Demonstrate security logging
        securityLogger.info("Authentication successful: user=admin, ip=192.168.1.100");
        securityLogger.warn("Failed login attempt: user=unknown, ip=192.168.1.200");
        
        // Demonstrate performance logging
        performanceLogger.info("Database query executed: duration=45ms, query=SELECT * FROM users");
        performanceLogger.debug("Cache hit: key=user_profile_123, hit_rate=0.85");
    }

    @GetMapping("/")
    public String hello() {
        logger.info("Hello endpoint called");
        return "Hello from Spring Boot Logging Example! Check the console for logging examples.";
    }

    @GetMapping("/log-examples")
    public String logExamples() {
        logger.trace("TRACE: Very detailed debugging information");
        logger.debug("DEBUG: Debugging information");
        logger.info("INFO: General information");
        logger.warn("WARN: Warning information");
        logger.error("ERROR: Error information");
        
        return "Logged examples at different levels. Check console output.";
    }

    @GetMapping("/business-log")
    public String businessLog() {
        businessLogger.info("Business operation: order_created, order_id=12345, amount=99.99");
        businessLogger.warn("Business warning: low_inventory, product_id=ABC123, current_stock=5");
        
        return "Business logging examples generated. Check console output.";
    }

    @GetMapping("/security-log")
    public String securityLog() {
        securityLogger.info("Security event: user_logout, user=john.doe, session_id=abc123");
        securityLogger.warn("Security warning: suspicious_activity, ip=192.168.1.150, pattern=rapid_requests");
        
        return "Security logging examples generated. Check console output.";
    }

    @GetMapping("/performance-log")
    public String performanceLog() {
        performanceLogger.info("Performance metric: api_response_time, endpoint=/api/users, duration=120ms");
        performanceLogger.debug("Performance debug: memory_usage, heap=512MB, non_heap=128MB");
        
        return "Performance logging examples generated. Check console output.";
    }
}
