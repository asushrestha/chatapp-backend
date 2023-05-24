package com.leapfrog.chattest.service;

import com.leapfrog.chattest.dto.chat.ChatMessageRequest;
import com.leapfrog.chattest.dto.chat.ChatMessageResponse;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessageResponse>  sendMessage(ChatMessageRequest request, String authorization);

    List<ChatMessageResponse> getChatMessageResponse(Long chatRoomId);
}
