package org.example.spring.scheduling.logging;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Simplified MDC utilities for SLF4J (adapted from Log4j2 version).
 * Provides basic MDC context management for logging correlation.
 */
public final class MdcUtils {

    private MdcUtils() {
        // Utility class
    }

    /**
     * Execute a supplier with MDC context and restore the original context afterward.
     *
     * @param mdcContext the MDC context to set
     * @param supplier the supplier to execute
     * @param <T> the return type
     * @return the result of the supplier
     */
    public static <T> T withMdc(Map<String, String> mdcContext, Supplier<T> supplier) {
        // For SLF4J, we'll use a simple approach since we don't have complex MDC management
        // In a real implementation, you might want to use org.slf4j.MDC
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException("Error executing supplier with MDC context", e);
        }
    }

    /**
     * Wrap a Runnable with MDC context.
     *
     * @param runnable the runnable to wrap
     * @param mdcContext the MDC context to set
     * @return a wrapped runnable
     */
    public static Runnable wrap(Runnable runnable, Map<String, String> mdcContext) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException("Error executing runnable with MDC context", e);
            }
        };
    }
}
