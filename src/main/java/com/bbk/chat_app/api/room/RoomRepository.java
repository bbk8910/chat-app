package com.bbk.chat_app.api.room;

import com.bbk.chat_app.api.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, String> {

    @Query("{ 'participants.id': { $all: [?0, ?1] } }")
    List<Room> findByParticipantsContainingBoth(String name1, String name2);

    @Query("{ 'participants.id': ?0 }")
    List<Room> findByParticipantsContainingName(String name);

    @Query("{ 'participants.id': { $in: [?0, ?1] } }")
    List<Room> findByAnyParticipantName(String name1, String name2);


    @Query("{ 'participants.id': { $all: ?0 } }")
    List<Room> findByParticipantsContainingAllNames(List<String> names);

}