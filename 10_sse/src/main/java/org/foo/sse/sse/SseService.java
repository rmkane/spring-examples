package org.foo.sse.sse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

import org.foo.sse.model.EventType;
import org.foo.sse.model.SseEvent;
import org.foo.sse.utils.TimeUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service for managing Server-Sent Events (SSE).
 * SSE provides one-way communication from server to client over HTTP.
 */
@Slf4j
@Service
public class SseService {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private static final AtomicLong eventId = new AtomicLong(1);

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * Register a new SSE emitter for a client.
     */
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> {
            log.info("🔌 SSE client disconnected (completion)");
            emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            log.info("⏰ SSE client disconnected (timeout)");
            emitters.remove(emitter);
        });

        emitter.onError((ex) -> {
            log.info("❌ SSE client disconnected (error): {}", ex.getMessage());
            emitters.remove(emitter);
        });

        emitters.add(emitter);
        log.info("🔗 SSE client connected. Total clients: {}", emitters.size());

        return emitter;
    }

    /**
     * Send an event to all connected SSE clients.
     */
    public void sendEvent(SseEvent event) {
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .id(String.valueOf(eventId.getAndIncrement()))
                    .name(event.getType())
                    .data(event, MediaType.APPLICATION_JSON));

                log.debug("📤 SSE event sent: {} - {}", event.getType(), event.getData());
            } catch (IOException e) {
                log.debug("❌ Failed to send SSE event to client: {}", e.getMessage());
                deadEmitters.add(emitter);
            }
        });

        // Remove dead emitters
        emitters.removeAll(deadEmitters);
        if (!deadEmitters.isEmpty()) {
            log.info("🧹 Cleaned up {} dead SSE emitters. Active clients: {}",
                deadEmitters.size(), emitters.size());
        }
    }

    /**
     * Generate and send a random SSE event every 3 seconds.
     */
    @Scheduled(fixedRateString = "PT3S")
    public void sendRandomEvent() {
        int currentNumber = counter.getAndIncrement();
        EventType eventType = determineEventType(currentNumber);
        String eventData = generateEventData(currentNumber, eventType);

        SseEvent event = new SseEvent(
            eventType.getType(),
            eventData,
            TimeUtils.getTimestamp(),
            String.valueOf(eventId.get())
        );

        sendEvent(event);
    }

    private EventType determineEventType(int number) {
        if (number % 5 == 0) {
            return EventType.ALERT;
        } else if (number % 4 == 0) {
            return EventType.NEWS;
        } else if (number % 3 == 0) {
            return EventType.STOCK;
        } else if (number % 2 == 0) {
            return EventType.WEATHER;
        } else {
            return EventType.SYSTEM;
        }
    }

    private String generateEventData(int number, EventType eventType) {
        switch (eventType) {
            case ALERT:
                return "🚨 Alert #" + number + ": System maintenance scheduled";
            case NEWS:
                return "📰 News #" + number + ": Breaking news update";
            case STOCK:
                return "📈 Stock #" + number + ": Market data update";
            case WEATHER:
                return "🌤️ Weather #" + number + ": Temperature update";
            default:
                return "⚙️ System #" + number + ": Status update";
        }
    }

    /**
     * Get the number of active SSE clients.
     */
    public int getActiveClientCount() {
        return emitters.size();
    }
}
