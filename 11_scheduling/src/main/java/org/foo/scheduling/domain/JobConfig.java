package org.foo.scheduling.domain;

import lombok.Data;

/**
 * Configuration for a scheduled job
 */
@Data
public class JobConfig {
    private JobType type;
    private String value;
    private String endpoint;
    private String description;
    private boolean disabled;
}
