package org.foo.activemq.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String id;
    private String content;
    private String sender;
    private LocalDateTime timestamp = LocalDateTime.now();
    private MessageType type;

    @JsonCreator
    public Message(@JsonProperty("id") String id,
                   @JsonProperty("content") String content,
                   @JsonProperty("sender") String sender,
                   @JsonProperty("type") MessageType type) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public enum MessageType {
        INFO, WARNING, ERROR, SUCCESS
    }
}
