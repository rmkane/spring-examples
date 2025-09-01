package org.example.spring.activemq.service;

import org.example.spring.activemq.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    // Thread-safe list to store received messages
    private final List<Message> receivedMessages = new CopyOnWriteArrayList<>();

    @JmsListener(destination = "message.queue")
    public void receiveMessage(Message message) {
        logger.info("Received message from queue: {}", message);
        receivedMessages.add(message);

        // Process the message based on its type
        processMessage(message);
    }

    @JmsListener(destination = "message.topic", containerFactory = "jmsListenerContainerFactory")
    public void receiveTopicMessage(Message message) {
        logger.info("Received message from topic: {}", message);
        receivedMessages.add(message);

        // Process the message based on its type
        processMessage(message);
    }

    @JmsListener(destination = "error.queue")
    public void receiveErrorMessage(Message message) {
        logger.error("Received error message: {}", message);
        receivedMessages.add(message);

        // Special handling for error messages
        handleErrorMessage(message);
    }

    private void processMessage(Message message) {
        switch (message.getType()) {
            case INFO:
                logger.info("Processing INFO message from {}: {}", message.getSender(), message.getContent());
                break;
            case WARNING:
                logger.warn("Processing WARNING message from {}: {}", message.getSender(), message.getContent());
                break;
            case ERROR:
                logger.error("Processing ERROR message from {}: {}", message.getSender(), message.getContent());
                break;
            case SUCCESS:
                logger.info("Processing SUCCESS message from {}: {}", message.getSender(), message.getContent());
                break;
            default:
                logger.info("Processing message from {}: {}", message.getSender(), message.getContent());
        }
    }

    private void handleErrorMessage(Message message) {
        logger.error("Error message received - ID: {}, Content: {}, Sender: {}",
                    message.getId(), message.getContent(), message.getSender());

        // Here you could implement error handling logic like:
        // - Sending notifications
        // - Logging to external systems
        // - Triggering alerts
        // - Storing in error database
    }

    public List<Message> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);
    }

    public void clearReceivedMessages() {
        receivedMessages.clear();
        logger.info("Cleared all received messages");
    }

    public int getMessageCount() {
        return receivedMessages.size();
    }
}
