package com.chatbot.controllers.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendMessageDto(
        @NotNull
        @NotBlank
        String message
) {
}
