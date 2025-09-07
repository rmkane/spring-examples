package org.example.spring.scheduling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.scheduling.desync.Desync;
import org.example.spring.scheduling.model.TaskStatistics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service demonstrating real desynchronization scheduling patterns
 * Uses the actual desync implementation to prevent "thundering herd" problems
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DesyncSchedulingService {

    private final AtomicInteger taskCounter = new AtomicInteger(0);
    private final AtomicInteger cronTaskCounter = new AtomicInteger(0);
    private final AtomicInteger delayTaskCounter = new AtomicInteger(0);
    private final AtomicInteger desyncTaskCounter = new AtomicInteger(0);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /**
     * Fixed rate scheduling with desync - prevents thundering herd
     * Each instance will have a different execution time due to desync
     */
    @Desync(key = "fixed-rate-task", window = "PT5S", jitter = "PT1S")
    @Scheduled(fixedRate = 5000)
    public void fixedRateTask() {
        int count = taskCounter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.info("🔄 Fixed Rate Task #{} executed at {} (desync applied)", count, timestamp);

        // Simulate variable execution time
        try {
            Thread.sleep(1000 + (count % 3) * 500); // 1-2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Fixed rate task interrupted");
        }
    }

    /**
     * Fixed delay scheduling with desync
     * Demonstrates desync by ensuring consistent intervals between task completions
     */
    @Desync(key = "fixed-delay-task", window = "PT3S", jitter = "PT0.5S")
    @Scheduled(fixedDelay = 3000)
    public void fixedDelayTask() {
        int count = delayTaskCounter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.info("⏱️ Fixed Delay Task #{} executed at {} (desync applied)", count, timestamp);

        // Simulate variable execution time
        try {
            Thread.sleep(800 + (count % 4) * 200); // 0.8-1.4 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Fixed delay task interrupted");
        }
    }

    /**
     * Cron-based scheduling with desync - runs every 10 seconds
     * Demonstrates desync with complex timing patterns
     */
    @Desync(key = "cron-task", window = "PT10S", jitter = "PT2S")
    @Scheduled(cron = "*/10 * * * * *") // Every 10 seconds
    public void cronTask() {
        int count = cronTaskCounter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.info("⏰ Cron Task #{} executed at {} (desync applied)", count, timestamp);

        // Simulate processing time
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Cron task interrupted");
        }
    }

    /**
     * High-frequency task with aggressive desync
     * Demonstrates desync with shorter intervals
     */
    @Desync(key = "high-freq-task", window = "PT2S", jitter = "PT0.5S")
    @Scheduled(fixedRate = 2000)
    public void highFrequencyTask() {
        int count = desyncTaskCounter.incrementAndGet();
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.info("⚡ High Frequency Task #{} executed at {} (aggressive desync)", count, timestamp);

        // Simulate quick processing
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("High frequency task interrupted");
        }
    }

    /**
     * Long-running task with extended desync window
     * Demonstrates desync with longer execution times
     */
    @Desync(key = "long-running-task", window = "PT15S", jitter = "PT3S")
    @Scheduled(fixedRate = 15000)
    public void longRunningTask() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        log.info("🏢 Long Running Task executed at {} (extended desync window)", timestamp);

        // Simulate longer processing time
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Long running task interrupted");
        }
    }

    /**
     * Conditional scheduling with desync - only runs during business hours
     * Demonstrates desync with conditional execution
     */
    @Desync(key = "business-hours-task", window = "PT30S", jitter = "PT5S")
    @Scheduled(fixedRate = 30000)
    public void conditionalTask() {
        int hour = LocalDateTime.now().getHour();

        // Only run during business hours (9 AM - 5 PM)
        if (hour >= 9 && hour < 17) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            log.info("🏢 Business Hours Task executed at {} (conditional desync)", timestamp);
        } else {
            log.debug("💤 Skipping task - outside business hours (current hour: {})", hour);
        }
    }

    /**
     * Get current task statistics
     */
    public TaskStatistics getTaskStatistics() {
        return TaskStatistics.builder()
                .fixedRateCount(taskCounter.get())
                .fixedDelayCount(delayTaskCounter.get())
                .cronCount(cronTaskCounter.get())
                .asyncCount(desyncTaskCounter.get()) // Using desync counter instead of async
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Reset all counters
     */
    public void resetCounters() {
        taskCounter.set(0);
        delayTaskCounter.set(0);
        cronTaskCounter.set(0);
        desyncTaskCounter.set(0);
        log.info("🔄 All task counters reset");
    }
}
