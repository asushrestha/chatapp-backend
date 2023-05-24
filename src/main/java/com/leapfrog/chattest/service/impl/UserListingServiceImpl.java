package com.leapfrog.chattest.service.impl;

import com.leapfrog.chattest.commons.context.ContextHolderService;
import com.leapfrog.chattest.constants.ErrorMessage;
import com.leapfrog.chattest.dto.UserProfile;
import com.leapfrog.chattest.entity.Users;
import com.leapfrog.chattest.exception.RestException;
import com.leapfrog.chattest.repository.UserRepository;
import com.leapfrog.chattest.service.UsersListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserListingServiceImpl implements UsersListingService {
    private final UserRepository userRepository;

    private final ContextHolderService contextHolderService;

    @Autowired
    public UserListingServiceImpl(UserRepository userRepository, ContextHolderService contextHolderService) {
        this.userRepository = userRepository;
        this.contextHolderService = contextHolderService;
    }

    @Override
    public List<UserProfile> listAllUserProfile() {
        Users validUsers = userRepository.findUserByUserName(contextHolderService.getContext().getUserName().toUpperCase()).orElseThrow(()-> new RestException(ErrorMessage.INVALID_USER));
        List<Users> usersList = userRepository.findAllButNotCurrentUser(validUsers);
        List<UserProfile> userProfileList = new ArrayList<>();
        usersList.forEach(users->{
            userProfileList.add(prepareUserProfile(users));
        });
        return userProfileList;
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
}
