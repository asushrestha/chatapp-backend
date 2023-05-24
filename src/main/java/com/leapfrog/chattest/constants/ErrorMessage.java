package com.leapfrog.chattest.constants;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    INVALID_USER("AUTH001","No such user found!"),
    DUPLICATE_USERNAME("AUTH002","Duplicate userName!"),
    INVALID_ACCESS_TOKEN("FILTER001","Invalid access token!"),
    INVALID_CHAT_ROOM("CHAT001","This chat room is not for you!"),
    INVALID_CHAT_ROOM_ID("CHAT002","Invalid chat room id!");

    String code;
    String message;
    ErrorMessage(String code, String message){
        this.code = code;
        this.message = message;
    }
}
