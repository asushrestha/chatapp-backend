package com.leapfrog.chattest.dto.chat;

import com.leapfrog.chattest.dto.UserProfile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponse {


    private String lastMessage;
    private LocalDateTime lastMessageDateTime;

    private UserProfile otherUserProfile;

    private Long chatRoomId;
}
