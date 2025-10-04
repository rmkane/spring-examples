package org.foo.scheduling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foo.scheduling.domain.JobConfig;

/**
 * Default job handler for YAML-configured jobs
 */
@RequiredArgsConstructor
@Slf4j
public class JobHandler implements Runnable {

    private final String jobId;
    private final JobConfig config;

    @Override
    public void run() {
        log.info("📋 Executing YAML-configured job: {} ({})", jobId, config.getDescription());

        // In a real implementation, you might:
        // - Make HTTP calls to the endpoint
        // - Execute business logic
        // - Process data
        // - Send notifications

        if (config.getEndpoint() != null) {
            log.debug("Job {} would call endpoint: {}", jobId, config.getEndpoint());
        }
    }
}
