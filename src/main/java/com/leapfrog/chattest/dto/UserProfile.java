package com.leapfrog.chattest.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserProfile {
    private String userName;

    private String displayName;
    private Boolean isOnline;

    private LocalDateTime createdAt;
    private Long id;
}
