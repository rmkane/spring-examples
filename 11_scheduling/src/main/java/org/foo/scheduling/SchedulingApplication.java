package org.foo.scheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Example 10: Scheduling with Desync Logic
 *
 * Demonstrates various scheduling patterns including:
 * - Fixed rate scheduling
 * - Fixed delay scheduling
 * - Cron expressions
 * - Asynchronous task execution
 * - Desynchronization techniques
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulingApplication.class, args);
    }
}
