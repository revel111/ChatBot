package com.chatbot.controllers.dto.response;

import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String username,
        String email
) {
}
