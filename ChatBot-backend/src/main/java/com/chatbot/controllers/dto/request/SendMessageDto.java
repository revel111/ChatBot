package com.chatbot.controllers.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record SendMessageDto(
        @NotNull
        @NotBlank
        String message,

        UUID chatId
) {
}
