package com.leapfrog.chattest.constants;

public class Route {
    public static final String LOCAL_URL = "/chatapp";
    public static final String BASE_URL = LOCAL_URL +"/v1/users";
    public static final String AUTHENTICATE_USER = BASE_URL +"/authenticate";
    public static final String REGISTER_USER = BASE_URL +"/register-new";
    public static final String MARK_AS_ONLINE = BASE_URL +"/mark-as-online";
    public static final String MARK_AS_OFFLINE = BASE_URL +"/mark-as-offline";
    public static final String GET_PROFILE = BASE_URL +"/get-profile";
    public static final String LOG_OUT = BASE_URL +"/log-out";
    public static final String LIST_USERS = BASE_URL +"/list-all";
    public static final String CHAT_ROOM = BASE_URL +"/chat-room";
//    public static final String CHAT_ROUTE = BASE_URL +"/chat-room/route";
//    public static final String CHAT_SEND_MESSAGE = BASE_URL +"/chat-room/send";
    public static final String CHAT_GET_MESSAGE = BASE_URL +"/chat-room/get";

    private Route(){}

}