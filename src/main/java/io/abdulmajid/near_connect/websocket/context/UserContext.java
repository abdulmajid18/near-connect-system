package io.abdulmajid.near_connect.websocket.context;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private static final ThreadLocal<String> userIdThreadLocal = new ThreadLocal<>();

    public void setUserId(String userId) {
        userIdThreadLocal.set(userId);
    }

    public String getUserId() {
        return userIdThreadLocal.get();
    }

    public void clear() {
        userIdThreadLocal.remove();
    }
}
