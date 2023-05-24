package com.leapfrog.chattest.commons.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextHolder implements Runnable {
    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<>();
    private final Long userId;
    private final String userName;

    public ContextHolder(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public static void set(Context context) {
        threadLocal.set(context);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static Context get() {
        return threadLocal.get();
    }

    @Override
    public void run() {
        set(new Context(userId, userName));
        new ContextHolderService().getContext();
    }

}
