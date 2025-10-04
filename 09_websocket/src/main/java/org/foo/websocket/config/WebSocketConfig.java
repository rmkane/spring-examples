package org.foo.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        // Enable simple message broker for broadcasting
        config.enableSimpleBroker("/topic");

        // Set application destination prefix for client messages
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        // Register the WebSocket endpoint
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(@NonNull WebSocketTransportRegistration registration) {
        // Configure WebSocket transport settings
        registration.setMessageSizeLimit(64 * 1024) // 64KB message size limit
                   .setSendBufferSizeLimit(512 * 1024) // 512KB send buffer
                   .setSendTimeLimit(20000); // 20 second send time limit
    }
}
