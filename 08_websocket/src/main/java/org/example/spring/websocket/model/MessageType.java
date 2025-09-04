package org.example.spring.websocket.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum representing the different types of FizzBuzz messages.
 * This centralizes all message type definitions to avoid magic strings.
 */
public enum MessageType {
    FIZZ("fizz", "Numbers divisible by 3"),
    BUZZ("buzz", "Numbers divisible by 5"),
    FIZZBUZZ("fizzbuzz", "Numbers divisible by both 3 and 5"),
    NUMBER("number", "All other numbers"),
    WELCOME("welcome", "Welcome messages");

    private final String value;
    private final String description;

    private MessageType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Get all message type values as an array of strings.
     * Useful for API responses and validation.
     */
    public static List<String> getAllValues() {
        return Stream.of(values())
            .map(MessageType::getValue)
            .toList();
    }

    /**
     * Get all message types with their descriptions as a map.
     * Useful for API documentation.
     */
    public static Map<String, String> getAllWithDescriptions() {
        return Stream.of(values())
            .collect(Collectors.toMap(MessageType::getValue, MessageType::getDescription));
    }
}
