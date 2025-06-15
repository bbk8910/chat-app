package com.bbk.chat_app.api.room;

import com.bbk.chat_app.api.user.User;
import com.bbk.chat_app.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{username}")
    public List<Room> getUserRooms(@PathVariable String username) {
        System.out.println("hecli");
        return roomRepository.findByParticipantsContainingName(username);
    }

    @PostMapping("/join")
    public Room joinRoom(@RequestParam String username, @RequestParam String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        userRepository.findById(username).ifPresent(u -> room.getParticipants().add(Room.RoomParticipant.builder().id(u.getUsername()).name(u.getDisplayName()).build()));
        return roomRepository.save(room);
    }

    @PostMapping("/create")
    public Room createOrGetRoom(@RequestBody CreateRoomRequest room) {
        var participants = room.getParticipants().stream().map(p -> userRepository.findById(p).map(u -> Room.RoomParticipant.builder().id(u.getUsername()).name(u.getDisplayName()).build()).orElseGet(null)).filter(Objects::nonNull).collect(Collectors.toSet());
        if (participants.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No participants found");
        }

        var rooms = roomRepository.findByParticipantsContainingAllNames(participants.stream().map(Room.RoomParticipant::getId).toList());
        if (!rooms.isEmpty()) {
            return rooms.stream().findFirst().get();

        } else {
            return roomRepository.save(Room.builder().participants(participants).build());
        }
    }

    @PostMapping("/start")
    public Room startOrGetRoom(@RequestParam String requester, @RequestParam String targetUser) {
        User requesterUser = userRepository.findById(requester).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        User target = userRepository.findById(targetUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        List<Room> sharedRooms = roomRepository.findByParticipantsContainingBoth(requester, targetUser);
        if (!sharedRooms.isEmpty()) {
            return sharedRooms.getFirst(); // return existing
        }

        return roomRepository.save(Room.builder()
                .participants(Set.of(
                        Room.RoomParticipant.builder().id(requesterUser.getUsername()).name(requesterUser.getDisplayName()).build(),
                        Room.RoomParticipant.builder().id(target.getUsername()).name(target.getDisplayName()).build())).build());
    }
}
