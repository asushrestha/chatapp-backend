package com.leapfrog.chattest.controller;

import com.leapfrog.chattest.constants.Route;
import com.leapfrog.chattest.dto.UserProfile;
import com.leapfrog.chattest.service.UsersListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class UsersListingController {
    private final UsersListingService usersListingService;

    @Autowired
    public UsersListingController(UsersListingService usersListingService) {
        this.usersListingService = usersListingService;
    }

    @GetMapping(Route.LIST_USERS)
    public List<UserProfile> userProfileList(){
        log.info("list all user profile:");
        return  usersListingService.listAllUserProfile();
    }
}
