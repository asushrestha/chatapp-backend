package com.leapfrog.chattest.controller;

import com.leapfrog.chattest.constants.Route;
import com.leapfrog.chattest.dto.*;
import com.leapfrog.chattest.service.AuthService;
import com.leapfrog.chattest.dto.RegisterUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AuthController(AuthService authService, SimpMessagingTemplate messagingTemplate) {
        this.authService = authService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping(Route.AUTHENTICATE_USER)
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.info("authenticate user:{}", loginRequest);
         return  authService.authenticateUser(loginRequest);
    }
    @PostMapping(value = Route.REGISTER_USER)
    public RegisterResponse registerResponse(@RequestBody RegisterUserRequest registerUserRequest){
        log.info("register new user:{}",registerUserRequest);
        return authService.registerNewUser(registerUserRequest);
    }

    @GetMapping(value = Route.AUTHENTICATE_USER+ "/text")
    public void testApi (){
        messagingTemplate.convertAndSend("/topic/queue/message/list/1", "datatest");

    }

    @PutMapping( value = Route.MARK_AS_ONLINE)
    public UserProfile markUserAsOnline(@RequestBody ConfirmationRequest confirmationRequest){
        log.info("marking user as online:{}",confirmationRequest);
        return authService.markUserAsOnline(confirmationRequest);
    }
    @PutMapping(value = Route.MARK_AS_OFFLINE)
    public UserProfile markUserAsOffline(@RequestBody ConfirmationRequest confirmationRequest){
        log.info("marking user as offline:{}",confirmationRequest);
        return authService.markUserAsOffline(confirmationRequest);
    }

    @PutMapping(Route.LOG_OUT)
    public Message logOutUser(@RequestBody ConfirmationRequest confirmationRequest){
        log.info("log out  user :{}",confirmationRequest);
        return authService.logOutUser(confirmationRequest);
    }

    @GetMapping(Route.GET_PROFILE)
    public UserProfile getUserProfile(){
        log.info("getprofile of user :");
        return authService.getUserProfile();
    }
}
