package com.leapfrog.chattest.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String userName;
    private String displayName;
    private String password;
}
