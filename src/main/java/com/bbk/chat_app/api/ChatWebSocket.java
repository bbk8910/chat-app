//package com.bbk.chat_app.api;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.websocket.Session;
//import jakarta.websocket.server.ServerEndpoint;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.server.standard.SpringConfigurator;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//@ServerEndpoint(value = "/ws/chat",configurator = SpringConfigurator.class)
//@Component
//public class ChatWebSocket {
//
//    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
//    private  final ChatMessageRepository repository;
//
//    public ChatWebSocket(ChatMessageRepository repository) {
//        this.repository = repository;
//    }
//
//
//    @OnOpen
//    public void onOpen(Session session) {
//
//        sessions.add(session);
//    }
//
//    @OnMessage
//    public void onMessage(String messageJson, Session session) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        ChatMessage message = mapper.readValue(messageJson, ChatMessage.class);
//        message.setTimestamp(LocalDateTime.now().toString());
//
//        repository.save(message);
//
//        String broadcast = mapper.writeValueAsString(message);
//        for (Session s : sessions) {
//            if (s.isOpen()) s.getBasicRemote().sendText(broadcast);
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        sessions.remove(session);
//    }
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        throwable.printStackTrace();
//    }
//}
