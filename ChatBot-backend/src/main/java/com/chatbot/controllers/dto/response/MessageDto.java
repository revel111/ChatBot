package com.chatbot.controllers.dto.response;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID chatId,
        String text,
        Instant createdAt
) {
}
