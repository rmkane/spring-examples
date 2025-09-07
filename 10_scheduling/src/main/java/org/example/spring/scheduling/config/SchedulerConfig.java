package org.example.spring.scheduling.config;

import org.example.spring.scheduling.domain.JobsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Configuration for the task scheduler
 */
@Configuration
@EnableConfigurationProperties(JobsProperties.class)
public class SchedulerConfig {
    private static final int THREAD_POOL_SIZE = 8;

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(THREAD_POOL_SIZE); // tune as needed
        scheduler.setThreadNamePrefix("job-scheduler-");
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.initialize();

        return scheduler;
    }
}
