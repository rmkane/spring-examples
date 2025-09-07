package org.example.spring.scheduling.domain;

/**
 * Types of scheduled jobs supported by the system
 */
public enum JobType {
    CRON,      // Cron-based scheduling
    DURATION   // Duration-based scheduling
}
