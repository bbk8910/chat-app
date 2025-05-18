package com.bbk.chat_app.api.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/user/{username}")
    public List<Room> getUserRooms(@PathVariable String username) {
        return roomRepository.findByParticipantsContains(username);
    }

    @PostMapping("/join")
    public Room joinRoom(@RequestParam String username, @RequestParam String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.getParticipants().add(username);
        return roomRepository.save(room);
    }

    @PostMapping("/create")
    public Room createRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }
}
