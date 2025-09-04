package org.example.spring.websocket.model;

public record FizzBuzzMessage(
    String topic,
    String message,
    String timestamp
) {}
