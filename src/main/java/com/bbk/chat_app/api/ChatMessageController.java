package com.bbk.chat_app.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ChatMessageController {
    private final ChatMessageRepository repo;

    public ChatMessageController(ChatMessageRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ChatMessage> getAllMessages() {
        return repo.findAll();
    }
    @GetMapping("/{roomId}")
    public List<ChatMessage> getMessagesByRoom(@PathVariable String roomId) {
        return repo.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
