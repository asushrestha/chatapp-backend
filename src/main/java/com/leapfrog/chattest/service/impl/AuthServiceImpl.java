package com.leapfrog.chattest.service.impl;

import com.leapfrog.chattest.commons.context.ContextHolderService;
import com.leapfrog.chattest.constants.ErrorMessage;
import com.leapfrog.chattest.dto.*;
import com.leapfrog.chattest.entity.Users;
import com.leapfrog.chattest.exception.RestException;
import com.leapfrog.chattest.repository.UserRepository;
import com.leapfrog.chattest.service.AuthService;
import com.leapfrog.chattest.util.JwtUtil;
import com.leapfrog.chattest.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ContextHolderService contextHolderService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, ContextHolderService contextHolderService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.contextHolderService = contextHolderService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Users user = userRepository.findUserForAuthentication(loginRequest.getUserName().toUpperCase(), SecurityUtil.encode(loginRequest.getPassword())).orElseThrow(()-> new RestException(ErrorMessage.INVALID_USER));
        return prepareLoginResponse(user);
    }

    private LoginResponse prepareLoginResponse(Users user) {
        return LoginResponse.builder()
                .token(jwtUtil.generateToken(prepareClaims(user)))
                .userProfile(prepareUserProfile(user))
                .build();
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
    private Map<String, Object> prepareClaims(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("userName", user.getUserName());
        return claims;
    }
    @Override
    public RegisterResponse registerNewUser(RegisterUserRequest registerUserRequest) {
        Optional<Users> optionalUsers = userRepository.findUserForRegistration(registerUserRequest.getUserName().toUpperCase());
        if(optionalUsers.isPresent()){
            throw new RestException(ErrorMessage.DUPLICATE_USERNAME);
        }
        userRepository.saveAndFlush(prepareToAddNewUser(registerUserRequest));
        return RegisterResponse.builder()
                .message("Account with username [".concat(registerUserRequest.getUserName()).concat("] is  successfully created!"))
                .build();
    }

    @Override
    public UserProfile markUserAsOnline(ConfirmationRequest confirmationRequest) {
        Users users = userRepository.findUserByUserName(contextHolderService.getContext().getUserName().toUpperCase()).orElseThrow(()-> new RestException(ErrorMessage.INVALID_USER));
        users.setIsOnline(true);
        users  = userRepository.save(users);
        return prepareUserProfile(users);
    }

    @Override
    public UserProfile markUserAsOffline(ConfirmationRequest confirmationRequest) {
        Users users = userRepository.findUserByUserName(contextHolderService.getContext().getUserName().toUpperCase()).orElseThrow(()-> new RestException(ErrorMessage.INVALID_USER));
        users.setIsOnline(false);
        userRepository.save(users);
        return  prepareUserProfile(users);
    }

    @Override
    public Message logOutUser(ConfirmationRequest confirmationRequest) {
        Users users = userRepository.findUserByUserName(contextHolderService.getContext().getUserName().toUpperCase()).orElseThrow(()-> new RestException(ErrorMessage.INVALID_USER));
        users.setIsOnline(false);
        userRepository.save(users);
        return  Message.builder()
                .message("User logged out successfully")
                .build();
    }

    @Override
    public UserProfile getUserProfile() {
        Users users = userRepository.findUserByUserName(contextHolderService.getContext().getUserName().toUpperCase()).orElseThrow(()-> new RestException(ErrorMessage.INVALID_USER));

        return prepareUserProfile(users);
    }

    private Users prepareToAddNewUser(RegisterUserRequest registerUserRequest) {
        Users users = new Users();
        users.setUserName(registerUserRequest.getUserName());
        users.setPassword(SecurityUtil.encode(registerUserRequest.getPassword()));
        users.setDisplayName(registerUserRequest.getDisplayName());
        return  users;
    }
}
