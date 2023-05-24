package com.leapfrog.chattest.controller;



import com.leapfrog.chattest.constants.Route;
import com.leapfrog.chattest.dto.chat.ChatRoomResponse;
import com.leapfrog.chattest.dto.chat.CreateChatRoomRequest;
import com.leapfrog.chattest.service.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(Route.CHAT_ROOM)
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/create")
    public ChatRoomResponse createChatRoom( @RequestBody CreateChatRoomRequest request){
        log.info("Create chat room :: {}", request);
        return chatRoomService.createChatRoom(request);
    }

    @GetMapping()
    public List<ChatRoomResponse> getChatRoomList( ){
        log.info("Get all chat room ");
        return chatRoomService.getChatRoom();
    }

}