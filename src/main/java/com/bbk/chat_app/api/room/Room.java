package com.bbk.chat_app.api.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    @Id
    private String id;
    private String name;
    private Set<RoomParticipant> participants = new HashSet<>();

    public String getName() {
        if (!participants.isEmpty()) {
            return participants.stream()
                    .map(RoomParticipant::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" - "));
        }
        return "";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomParticipant {
        private String id;
        private String name;

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            RoomParticipant that = (RoomParticipant) object;
            return Objects.equals(id, that.id) && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

}
