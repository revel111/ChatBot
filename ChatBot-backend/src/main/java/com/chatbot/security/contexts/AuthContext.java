package com.chatbot.security.contexts;

import java.util.UUID;

public record AuthContext(
        UUID id,
        String username,
        String email,
        UUID selectedChat
) {
}
