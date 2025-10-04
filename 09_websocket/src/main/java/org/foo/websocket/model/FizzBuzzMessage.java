package org.foo.websocket.model;

public record FizzBuzzMessage(
    String topic,
    String message,
    String timestamp
) {}
