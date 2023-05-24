package com.leapfrog.chattest.dto.chat;

import com.leapfrog.chattest.dto.UserProfile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {
    private Long messageId;

    private String plainText;

    private LocalDateTime sentDateTime;

    private Long chatRoomId;

    private UserProfile sentBy;

}
