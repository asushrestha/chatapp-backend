package com.leapfrog.chattest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Message {
    private String message;
}
