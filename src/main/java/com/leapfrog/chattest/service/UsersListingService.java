package com.leapfrog.chattest.service;

import com.leapfrog.chattest.dto.UserProfile;

import java.util.List;

public interface UsersListingService {
    List<UserProfile> listAllUserProfile();
}
