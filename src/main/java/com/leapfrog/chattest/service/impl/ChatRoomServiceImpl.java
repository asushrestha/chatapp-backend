package com.leapfrog.chattest.service.impl;

import com.leapfrog.chattest.commons.chatConfig.WebSocketEventListener;
import com.leapfrog.chattest.commons.context.ContextHolderService;
import com.leapfrog.chattest.constants.ErrorMessage;
import com.leapfrog.chattest.dto.UserProfile;
import com.leapfrog.chattest.dto.chat.ChatRoomResponse;
import com.leapfrog.chattest.dto.chat.CreateChatRoomRequest;
import com.leapfrog.chattest.entity.ChatMessage;
import com.leapfrog.chattest.entity.ChatRoom;
import com.leapfrog.chattest.entity.Users;
import com.leapfrog.chattest.exception.RestException;
import com.leapfrog.chattest.repository.ChatRoomRepository;
import com.leapfrog.chattest.repository.UserRepository;
import com.leapfrog.chattest.service.ChatRoomService;
import com.leapfrog.chattest.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ContextHolderService contextHolderService;
    private final JwtUtil jwtTokenUtil;
    private final WebSocketEventListener webSocketEventListener;
    private final UserRepository userRepository;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, ContextHolderService contextHolderService, JwtUtil jwtTokenUtil, WebSocketEventListener webSocketEventListener, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.contextHolderService = contextHolderService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.webSocketEventListener = webSocketEventListener;
        this.userRepository = userRepository;
    }

    @Override
    public Users validateUser(String userName) {
        return userRepository.findUserByUserName(userName.toUpperCase()).orElseThrow(() -> new RestException(ErrorMessage.INVALID_USER));
    }

    @Override
    public Users validateUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RestException(ErrorMessage.INVALID_USER));
    }

    @Override
    public ChatRoomResponse createChatRoom(CreateChatRoomRequest request) {
        Users participantOne = validateUser(contextHolderService.getContext().getUserName());
        ChatRoom chatRoom = chatRoomRepository.save(prepareToCreateChatRoom(new ChatRoom(), request, participantOne));
        return prepareToAddChatRoomResponse(chatRoom,participantOne);
    }

    @Override
    public List<ChatRoomResponse> getChatRoom() {
        Users user = validateUser(contextHolderService.getContext().getUserName());
        List<ChatRoom> chatRooms = chatRoomRepository.findMyChatRoomList(user);
        List<ChatRoomResponse> responses = new ArrayList<>();
        chatRooms.forEach(chatRoom -> responses.add(prepareToAddChatRoomResponse(chatRoom,user)));
        return responses;
    }
    @Override
    public List<ChatRoomResponse> getChatRoom(Long id) {
        Users user = validateUser(id);
        List<ChatRoom> chatRooms = chatRoomRepository.findMyChatRoomList(user);
        List<ChatRoomResponse> responses = new ArrayList<>();
        chatRooms.forEach(chatRoom -> responses.add(prepareToAddChatRoomResponse(chatRoom,user)));
        return responses;
    }

    @Override
    public ChatRoom validateChatRoomByChatRoomId(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RestException(ErrorMessage.INVALID_CHAT_ROOM_ID));
    }

    @Override
    public void updateLastMessage(ChatMessage chatMessage) {
        ChatRoom chatRoom = chatMessage.getChatRoom();
        chatRoom.setLastMessageDateTime(chatMessage.getSentDateTime());
        chatRoom.setLastTextMessage(chatMessage.getPlainText());
        chatRoomRepository.save(chatRoom);
    }

    private ChatRoomResponse prepareToAddChatRoomResponse(ChatRoom chatRoom,Users users) {
        Users otherUser = findOtherUserOfThisChatRoom(chatRoom,users);
        return ChatRoomResponse.builder()
                .lastMessage(null)
                .otherUserProfile(prepareUserProfile(otherUser))
                .lastMessageDateTime(chatRoom.getLastMessageDateTime())
                .chatRoomId(chatRoom.getId())
                .lastMessage(chatRoom.getLastTextMessage())
                .build();
    }

    private UserProfile prepareUserProfile(Users user) {
        return UserProfile.builder()
                .displayName(user.getDisplayName())
                .userName(user.getUserName())
                .createdAt(user.getCreatedAt())
                .isOnline(user.getIsOnline())
                .build();
    }

    @Override
    public Users findOtherUserOfThisChatRoom(ChatRoom chatRoom, Users users) {
        if (users.getId() == chatRoom.getParticipantOne().getId()) {
            return chatRoom.getParticipantTwo();
        } else if (users.getId() == chatRoom.getParticipantTwo().getId()) {
            return chatRoom.getParticipantOne();
        } else {
            throw new RestException(ErrorMessage.INVALID_CHAT_ROOM);
        }
    }
    private ChatRoom prepareToCreateChatRoom(ChatRoom chatRoom, CreateChatRoomRequest request, Users participantOne) {

        chatRoom.setParticipantTwo(validateUser(request.getParticipantTwoId()));
        chatRoom.setParticipantOne(participantOne);
        chatRoom.setLastMessageDateTime(LocalDateTime.now());
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findUniqueChatRoomForParticipants(chatRoom.getParticipantOne(),chatRoom.getParticipantTwo());
        return optionalChatRoom.orElse(chatRoom);
    }
}
