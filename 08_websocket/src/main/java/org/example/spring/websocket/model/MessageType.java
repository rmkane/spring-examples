package org.example.spring.websocket.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the different types of FizzBuzz messages.
 * This centralizes all message type definitions to avoid magic strings.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MessageType {
    FIZZ("fizz", "Numbers divisible by 3"),
    BUZZ("buzz", "Numbers divisible by 5"),
    FIZZBUZZ("fizzbuzz", "Numbers divisible by both 3 and 5"),
    NUMBER("number", "All other numbers"),
    WELCOME("welcome", "Welcome messages");

    private final String type;
    private final String description;

    @Override
    public String toString() {
        return type;
    }

    /**
     * Get all message type values as an array of strings.
     * Useful for API responses and validation.
     */
    public static List<String> getAllValues() {
        return Stream.of(values())
            .map(MessageType::getType)
            .toList();
    }

    /**
     * Get all message types with their descriptions as a map.
     * Useful for API documentation.
     */
    public static Map<String, String> getAllWithDescriptions() {
        return Stream.of(values())
            .collect(Collectors.toMap(MessageType::getType, MessageType::getDescription));
    }
}
