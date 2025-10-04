package org.foo.scheduling.service;

import lombok.extern.slf4j.Slf4j;
import org.foo.scheduling.desync.Desync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Legacy job runner demonstrating both traditional and desync scheduling
 */
@Component
@Slf4j
public class LegacyJobRunner {

    @Value("${app.legacy.jobs.cron1.disabled:false}")
    private boolean disabledCron1;

    @Value("${app.legacy.jobs.cron5.disabled:false}")
    private boolean disabledCron5;

    @Value("${app.legacy.jobs.rate10.disabled:false}")
    private boolean disabledRate10;

    @Value("${app.legacy.jobs.rate20.disabled:false}")
    private boolean disabledRate20;

    @Value("${app.legacy.jobs.rate30.disabled:false}")
    private boolean disabledRate30;

    /**
     * Legacy cron job with desync - runs every minute
     */
    @Desync(key = "legacy-cron1", window = "PT10S", jitter = "PT2S")
    @Scheduled(cron = "${app.legacy.jobs.cron1.value:0 * * * * *}", zone = "UTC")
    void cron1() {
        if (disabledCron1) {
            log.debug("legacy cron1 is disabled");
            return;
        }

        log.info("🕐 Running legacy job (every minute) with desync...");
    }

    /**
     * Legacy cron job with desync - runs every 5th minute
     */
    @Desync(key = "legacy-cron5", window = "PT30S", jitter = "PT5S")
    @Scheduled(cron = "${app.legacy.jobs.cron5.value:0 */5 * * * *}", zone = "UTC")
    void cron5() {
        if (disabledCron5) {
            log.debug("legacy cron5 is disabled");
            return;
        }

        log.info("🕐 Running legacy job (every 5th minute) with desync...");
    }

    /**
     * Legacy fixed rate job - runs every 10 seconds (no desync)
     */
    @Scheduled(fixedRateString = "${app.legacy.jobs.rate10.value:10000}")
    void rate10() {
        if (disabledRate10) {
            log.debug("legacy rate10 is disabled");
            return;
        }

        log.info("⏱️ Running legacy job (10s) - traditional scheduling");
    }

    /**
     * Legacy fixed rate job - runs every 20 seconds (no desync)
     */
    @Scheduled(fixedRateString = "${app.legacy.jobs.rate20.value:20000}")
    void rate20() {
        if (disabledRate20) {
            log.debug("legacy rate20 is disabled");
            return;
        }

        log.info("⏱️ Running legacy job (20s) - traditional scheduling");
    }

    /**
     * Legacy fixed rate job - runs every 30 seconds (no desync)
     */
    @Scheduled(fixedRateString = "${app.legacy.jobs.rate30.value:30000}")
    void rate30() {
        if (disabledRate30) {
            log.debug("legacy rate30 is disabled");
            return;
        }

        log.info("⏱️ Running legacy job (30s) - traditional scheduling");
    }
}
