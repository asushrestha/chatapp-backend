package com.leapfrog.chattest.controller;

import com.leapfrog.chattest.constants.Route;
import com.leapfrog.chattest.dto.chat.ChatMessageRequest;
import com.leapfrog.chattest.dto.chat.ChatMessageResponse;
import com.leapfrog.chattest.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/send")
    private List<ChatMessageResponse> sendMessage(@Payload ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor){
        log.info("Send chat message request :: {}", request);
        List<String> authorization = headerAccessor.getNativeHeader("Authorization");
        if(authorization==null || authorization.isEmpty()) {
            System.out.println("something went wrong");
        }
        System.out.println(authorization.get(0));
        System.out.println(headerAccessor);
        return chatMessageService.sendMessage(request, authorization.get(0).replace("Bearer ",""));
    }

    @GetMapping(value=Route.CHAT_GET_MESSAGE)
    public List<ChatMessageResponse> getChatMessageResponse(@RequestParam("chatRoomId") Long chatRoomId) {
        log.info("Get chat room message ::{}", chatRoomId);
        return chatMessageService.getChatMessageResponse(chatRoomId);
    }

}