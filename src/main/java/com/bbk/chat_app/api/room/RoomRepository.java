package com.bbk.chat_app.api.room;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findByParticipantsContains(String username);
}