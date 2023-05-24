package com.leapfrog.chattest.service;

import com.leapfrog.chattest.dto.chat.ChatRoomResponse;
import com.leapfrog.chattest.dto.chat.CreateChatRoomRequest;
import com.leapfrog.chattest.entity.ChatMessage;
import com.leapfrog.chattest.entity.ChatRoom;
import com.leapfrog.chattest.entity.Users;

import java.util.List;

public interface ChatRoomService {
    Users validateUser(String userName);

    Users validateUser(Long userId);

    ChatRoomResponse createChatRoom(CreateChatRoomRequest request);

    List<ChatRoomResponse> getChatRoom();
    List<ChatRoomResponse> getChatRoom(Long id);

    ChatRoom validateChatRoomByChatRoomId(Long chatRoomId);

    void updateLastMessage(ChatMessage chatMessage);

    Users findOtherUserOfThisChatRoom(ChatRoom chatRoom,Users users);
}
