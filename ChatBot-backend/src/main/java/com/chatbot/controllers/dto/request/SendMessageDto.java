package com.chatbot.controllers.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendMessageDto(
        @NotNull
        @NotBlank
        String message,

        UUID chatId
) {
}
