package org.foo.activemq.controller;

import org.foo.activemq.model.Message;
import org.foo.activemq.service.MessageConsumer;
import org.foo.activemq.service.MessageProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageProducer messageProducer;
    private final MessageConsumer messageConsumer;

    @PostMapping("/send/queue")
    public ResponseEntity<Map<String, String>> sendMessageToQueue(@RequestBody Message message) {
        try {
            messageProducer.sendMessage("message.queue", message);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to queue successfully");
            response.put("messageId", message.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send message: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/send/topic")
    public ResponseEntity<Map<String, String>> sendMessageToTopic(@RequestBody Message message) {
        try {
            messageProducer.sendMessage("message.topic", message);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Message sent to topic successfully");
            response.put("messageId", message.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send message: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/send/error")
    public ResponseEntity<Map<String, String>> sendErrorMessage(@RequestBody Message message) {
        try {
            messageProducer.sendMessage("error.queue", message);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Error message sent successfully");
            response.put("messageId", message.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send error message: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/send/text")
    public ResponseEntity<Map<String, String>> sendTextMessage(
            @RequestParam String content,
            @RequestParam String sender,
            @RequestParam(defaultValue = "message.queue") String destination) {
        try {
            messageProducer.sendTextMessage(destination, content, sender);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Text message sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to send text message: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/received")
    public ResponseEntity<List<Message>> getReceivedMessages() {
        List<Message> messages = messageConsumer.getReceivedMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getMessageCount() {
        Map<String, Integer> response = new HashMap<>();
        response.put("count", messageConsumer.getMessageCount());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearReceivedMessages() {
        messageConsumer.clearReceivedMessages();
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Received messages cleared successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "ActiveMQ Message Service");
        return ResponseEntity.ok(response);
    }
}
