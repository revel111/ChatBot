package com.chatbot.controllers.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String username,
        String email,
        Instant createdAt,
        Instant updatedAt
) {
}
