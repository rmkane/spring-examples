package org.example.spring.websocket.model;

public class FizzBuzzMessage {
    private String topic;
    private String message;
    private String timestamp;

    public FizzBuzzMessage(String topic, String message, String timestamp) {
        this.topic = topic;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
