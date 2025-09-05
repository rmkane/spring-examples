package org.example.spring.sse.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing different types of SSE events.
 * SSE is one-way communication from server to client.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EventType {
    SYSTEM("system", "System notifications and status updates"),
    WEATHER("weather", "Weather data updates"),
    STOCK("stock", "Stock market data"),
    NEWS("news", "News headlines"),
    ALERT("alert", "Important alerts and notifications");

    private final String type;
    private final String description;

    @Override
    public String toString() {
        return type;
    }
}
