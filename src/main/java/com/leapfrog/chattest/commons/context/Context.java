package com.leapfrog.chattest.commons.context;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Context {
    private Long userId;
    private String userName;

    public Context(){}
}
