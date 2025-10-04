package org.foo.activemq.service;

import org.foo.activemq.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private final JmsTemplate jmsTemplate;

    public void sendMessage(String destination, Message message) {
        try {
            if (message.getId() == null) {
                message.setId(UUID.randomUUID().toString());
            }

            jmsTemplate.convertAndSend(destination, message);
            logger.info("Message sent to destination '{}': {}", destination, message);
        } catch (Exception e) {
            logger.error("Error sending message to destination '{}': {}", destination, e.getMessage(), e);
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public void sendTextMessage(String destination, String content, String sender) {
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setType(Message.MessageType.INFO);
        sendMessage(destination, message);
    }

    public void sendErrorMessage(String destination, String content, String sender) {
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setType(Message.MessageType.ERROR);
        sendMessage(destination, message);
    }

    public void sendWarningMessage(String destination, String content, String sender) {
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setType(Message.MessageType.WARNING);
        sendMessage(destination, message);
    }

    public void sendSuccessMessage(String destination, String content, String sender) {
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setType(Message.MessageType.SUCCESS);
        sendMessage(destination, message);
    }
}
