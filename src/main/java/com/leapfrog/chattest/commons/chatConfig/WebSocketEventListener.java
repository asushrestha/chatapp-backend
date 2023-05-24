package com.leapfrog.chattest.commons.chatConfig;

import com.leapfrog.chattest.commons.context.ContextHolderService;
import com.leapfrog.chattest.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ContextHolderService contextHolderService;
    private final JwtUtil jwtTokenUtil;
    private Map<Long, String> onlineMap = new HashMap<>();

    @Autowired
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate, ContextHolderService contextHolderService, JwtUtil jwtTokenUtil) {
        this.messagingTemplate = messagingTemplate;
        this.contextHolderService = contextHolderService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection {}", event);
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        GenericMessage connectHeader = (GenericMessage) headerAccessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
//        System.out.println(nativeHeaders);
//        System.out.println("conncethed " +nativeHeaders.get("Authorization"));
        String simpSessionId = (String) connectHeader.getHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER);

        String token = nativeHeaders != null ? nativeHeaders.get("Authorization").get(0) : null;
        log.info("token of user :: {}", token);
        onlineMap.put(jwtTokenUtil.getId(token.replace("Bearer ","")), simpSessionId);

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Session disconnected :: event {}, session Id {} ", event, headerAccessor.getSessionId());
        try{
            removeUser(headerAccessor.getSessionId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void removeUser(String sessionId) {
        Set<Long> userId = onlineMap.keySet();
        for (Long user : userId) {
            if(sessionId.equalsIgnoreCase(onlineMap.get(user))){
                onlineMap.remove(user);

            }
        }
    }
    @EventListener
    public void handelWebSocketSubscribeListener(SessionSubscribeEvent sessionSubscribeEvent) {
        log.info("Web socket subscription event :: {}", sessionSubscribeEvent);

    }

    @EventListener
    public void handleWebSocketSubscription(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        log.info("Session unsubscription event :: {} ", sessionUnsubscribeEvent);


    }
}
