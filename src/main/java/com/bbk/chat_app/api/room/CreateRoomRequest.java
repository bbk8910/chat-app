package com.bbk.chat_app.api.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {
    private Set<String> participants = new HashSet<>();
}
