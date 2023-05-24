package com.leapfrog.chattest.service.impl;

import com.leapfrog.chattest.commons.chatConfig.WebSocketEventListener;
import com.leapfrog.chattest.commons.context.ContextHolderService;
import com.leapfrog.chattest.constants.Route;
import com.leapfrog.chattest.dto.UserProfile;
import com.leapfrog.chattest.dto.chat.ChatMessageRequest;
import com.leapfrog.chattest.dto.chat.ChatMessageResponse;
import com.leapfrog.chattest.entity.ChatMessage;
import com.leapfrog.chattest.entity.ChatRoom;
import com.leapfrog.chattest.entity.Users;
import com.leapfrog.chattest.exception.RestException;
import com.leapfrog.chattest.repository.ChatMessageRepository;
import com.leapfrog.chattest.service.ChatMessageService;
import com.leapfrog.chattest.service.ChatRoomService;
import com.leapfrog.chattest.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final String CHAT_MESSAGE_PATH = "chat-file";

    private final ChatMessageRepository chatMessageRepository;
    private final JwtUtil jwtTokenUtil;
    private final ChatRoomService chatRoomService;
    private final WebSocketEventListener webSocketEventListener;
    private final ContextHolderService contextHolderService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository,
                                  JwtUtil jwtTokenUtil,
                                  ChatRoomService chatRoomService,
                                  WebSocketEventListener webSocketEventListener,
                                  ContextHolderService contextHolderService, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.chatRoomService = chatRoomService;
        this.webSocketEventListener = webSocketEventListener;
        this.contextHolderService = contextHolderService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<ChatMessageResponse> sendMessage(ChatMessageRequest request, String authorization) {
        Long id = getUserNameFromToken(authorization);
        Users users = chatRoomService.validateUser(id);
        ChatRoom chatRoom = chatRoomService.validateChatRoomByChatRoomId(request.getChatRoomId());
//        chatRoomService.validateIfUserBelongsToChatRoom();
        ChatMessage chatMessage = chatMessageRepository.save(prepareToAddChatMessage(request, chatRoom,users));
        System.out.println("asfasf"+chatMessage);
        chatRoomService.updateLastMessage(chatMessage);
        publishInSocket(chatMessage,users);
        return getChatMessageResponse(chatMessage.getChatRoom().getId());
    }

    private Long getUserNameFromToken(String authorization) {
        if (authorization != null) {
            return jwtTokenUtil.getId(authorization);
        }
        throw new RestException("A001", "Invalid access token ");
    }

    @Override
    public List<ChatMessageResponse> getChatMessageResponse(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.validateChatRoomByChatRoomId(chatRoomId);
        List<ChatMessage> chatMessages = chatMessageRepository.getChatMessages(chatRoomId);
        List<ChatMessageResponse> chatMessageResponses = new ArrayList<>();
        chatMessages.forEach(chatMessage -> chatMessageResponses.add(prepareChatRoomResponse(chatMessage)));
        return chatMessageResponses;
    }


    private void publishInSocket(ChatMessage chatMessage,Users users) {
        publishMessageInSocket(chatMessage,users);
    }

    private void publishMessageInSocket(ChatMessage chatMessage,Users users) {
        ChatMessageResponse response = prepareChatRoomResponse(chatMessage);
        String chatRoomRoute = "/topic/queue/message/room/" + chatMessage.getChatRoom().getId();
        System.out.println("chatRoomRoute" + chatRoomRoute);
        messagingTemplate.convertAndSend(chatRoomRoute, getChatMessageResponse(response.getChatRoomId()));
        Users otherUser = chatRoomService.findOtherUserOfThisChatRoom(chatMessage.getChatRoom(),users);
        String otherUserRoute = "/topic/queue/message/list/"+ otherUser.getId();
        messagingTemplate.convertAndSend(otherUserRoute, chatRoomService.getChatRoom(otherUser.getId()));
        String selfUserRoute = "/topic/queue/message/list/"+ users.getId();
        messagingTemplate.convertAndSend(selfUserRoute, chatRoomService.getChatRoom(users.getId()));
        System.out.println("selfUserRoute:"+selfUserRoute);
        System.out.println("otherUserRoute:"+otherUserRoute);
    }

    private ChatMessageResponse prepareChatRoomResponse(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .messageId(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoom().getId())
                .plainText(chatMessage.getPlainText())
                .sentBy(prepareUserProfile(chatMessage.getSentBy()))
                .sentDateTime(chatMessage.getSentDateTime()).build();
    }

    private UserProfile prepareUserProfile(Users user) {
        return UserProfile.builder()
                .displayName(user.getDisplayName())
                .userName(user.getUserName())
                .createdAt(user.getCreatedAt())
                .isOnline(user.getIsOnline())
                .id(user.getId())
                .build();
    }

    private ChatMessage prepareToAddChatMessage(ChatMessageRequest request, ChatRoom chatRoom,Users users) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSentDateTime(LocalDateTime.now());
        chatMessage.setSentBy(users);
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setPlainText(request.getPlainText());
        return chatMessage;
    }


}