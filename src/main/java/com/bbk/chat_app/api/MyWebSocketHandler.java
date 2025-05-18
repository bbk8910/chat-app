package com.bbk.chat_app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class MyWebSocketHandler  implements WebSocketHandler {

    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatMessageRepository messageRepository;

    public MyWebSocketHandler(ChatMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
//        roomSessions.add(session);
        System.out.println("Connected: " + session.getId());
    }




    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        ChatMessage chatMsg = objectMapper.readValue(message.getPayload().toString(), ChatMessage.class);

        // Register session to room
        roomSessions.computeIfAbsent(chatMsg.getRoomId(), k -> ConcurrentHashMap.newKeySet()).add(session);

        // Save to DB
        messageRepository.save(new ChatMessage(null, chatMsg.getSender(), chatMsg.getContent(), chatMsg.getRoomId(),  ChatMessage.MessageType.CHAT,chatMsg.getTimestamp()));

        // Broadcast to room
        String broadcast = objectMapper.writeValueAsString(chatMsg);
        for (WebSocketSession s : roomSessions.get(chatMsg.getRoomId())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(broadcast));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        roomSessions.values().forEach(set -> set.remove(session));
        System.out.println("Disconnected: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("Error in session " + session.getId() + ": " + exception.getMessage());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}