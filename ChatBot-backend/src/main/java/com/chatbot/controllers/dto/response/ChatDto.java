package com.chatbot.controllers.dto.response;

import java.util.UUID;

public record ChatDto(
        UUID id,
        String name
) {
}
