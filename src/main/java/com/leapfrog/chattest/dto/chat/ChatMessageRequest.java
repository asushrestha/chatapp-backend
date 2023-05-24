package com.leapfrog.chattest.dto.chat;

import lombok.Data;

@Data
public class ChatMessageRequest {

    private String plainText;
    private Long chatRoomId;
}
