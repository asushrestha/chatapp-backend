package com.leapfrog.chattest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String token;
    private UserProfile userProfile;
}
