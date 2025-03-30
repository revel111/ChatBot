package com.chatbot.security.contexts;

public final class AuthContextHolder {

    private static final ThreadLocal<AuthContext> contextHolder = new ThreadLocal<>();

    public static void set(AuthContext authContext) {
        contextHolder.set(authContext);
    }

    public static AuthContext get() {
        return contextHolder.get();
    }

    public static void remove() {
        contextHolder.remove();
    }
}
