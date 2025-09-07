package org.example.spring.scheduling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Statistics model for tracking task execution counts
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatistics {

    private int fixedRateCount;
    private int fixedDelayCount;
    private int cronCount;
    private long asyncCount;
    private LocalDateTime timestamp;

    /**
     * Get total task count
     */
    public long getTotalCount() {
        return fixedRateCount + fixedDelayCount + cronCount + asyncCount;
    }

    /**
     * Get formatted timestamp
     */
    public String getFormattedTimestamp() {
        return timestamp != null ? timestamp.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) : "N/A";
    }
}
