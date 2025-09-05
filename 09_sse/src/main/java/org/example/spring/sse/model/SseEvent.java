package org.example.spring.sse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class representing an SSE event.
 * SSE events are sent from server to client over HTTP.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SseEvent {
    private String type;
    private String data;
    private String timestamp;
    private String id;
}
