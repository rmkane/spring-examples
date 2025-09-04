package org.example.spring.websocket.websocket;

import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import jakarta.annotation.PostConstruct;

import org.example.spring.websocket.model.FizzBuzzMessage;
import org.example.spring.websocket.model.MessageType;
import org.example.spring.websocket.utils.TimeUtils;

@Controller
public class FizzBuzzWebSocket {
    private static final AtomicInteger counter = new AtomicInteger(1);

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public FizzBuzzWebSocket(ObjectMapper objectMapper, SimpMessagingTemplate messagingTemplate) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void init() {
        System.out.println("🚀 FizzBuzzWebSocket initialized - scheduled messages will start in 5 seconds");
    }

    @MessageMapping("/fizzbuzz")
    @SendTo("/topic/fizzbuzz")
    public FizzBuzzMessage handleMessage(String message) {
        System.out.println("📨 Received message: " + message);
        return new FizzBuzzMessage(
            MessageType.WELCOME.getValue(),
            "Echo: " + message,
            TimeUtils.getTimestamp()
        );
    }

    @Scheduled(fixedRateString = "PT5S") // Every 5 seconds
    public void sendFizzBuzzMessage() {
        int currentNumber = counter.getAndIncrement();
        MessageType messageType = determineMessageType(currentNumber);
        String message = generateFizzBuzzMessage(currentNumber);

        FizzBuzzMessage fizzBuzzMessage = new FizzBuzzMessage(
            messageType.getValue(),
            message,
            TimeUtils.getTimestamp()
        );

        try {
            String jsonMessage = objectMapper.writeValueAsString(fizzBuzzMessage);

            System.out.println("=== FizzBuzz Message Generation ===");
            System.out.println("Number: " + currentNumber);
            System.out.println("Message Type: " + messageType.getValue());
            System.out.println("Message: " + message);
            System.out.println("JSON: " + jsonMessage);

            // Broadcast to all subscribers using Spring's messaging template
            System.out.println("📤 Attempting to send message to /topic/fizzbuzz...");
            messagingTemplate.convertAndSend("/topic/fizzbuzz", fizzBuzzMessage);
            System.out.println("📤 Message broadcasted to all subscribers");

            System.out.println("=====================================");
        } catch (Exception e) {
            // Log error but don't stop the scheduled task
            String errorMessage = e.getMessage();
            if (errorMessage != null && (
                errorMessage.contains("Session closed") ||
                errorMessage.contains("Cannot send a message when session is closed") ||
                errorMessage.contains("Failed to send WebSocket message")
            )) {
                // These are expected errors when clients disconnect - just log briefly
                System.out.println("ℹ️ Client disconnected, skipping message broadcast");
            } else {
                // For unexpected errors, log the full details
                System.err.println("❌ Error sending FizzBuzz message: " + errorMessage);
                e.printStackTrace();
            }
        }
    }

    private MessageType determineMessageType(int number) {
        if (number % 15 == 0) {
            return MessageType.FIZZBUZZ;
        } else if (number % 3 == 0) {
            return MessageType.FIZZ;
        } else if (number % 5 == 0) {
            return MessageType.BUZZ;
        } else {
            return MessageType.NUMBER;
        }
    }

    private String generateFizzBuzzMessage(int number) {
        MessageType messageType = determineMessageType(number);

        switch (messageType) {
            case FIZZBUZZ:
                return "FizzBuzz! Number " + number + " is divisible by both 3 and 5";
            case FIZZ:
                return "Fizz! Number " + number + " is divisible by 3";
            case BUZZ:
                return "Buzz! Number " + number + " is divisible by 5";
            default:
                return "Number " + number + " is not divisible by 3 or 5";
        }
    }
}
