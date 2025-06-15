package com.bbk.chat_app.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    private String id;
    private String sender;
    private String content;
    private String roomId;
    private MessageType type;

    private long timestamp;


    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

}
