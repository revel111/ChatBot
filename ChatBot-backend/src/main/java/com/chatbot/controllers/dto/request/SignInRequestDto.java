package com.chatbot.controllers.dto.request;

import jakarta.validation.constraints.NotNull;

public record SignInRequestDto(
        @NotNull
        String email,

        @NotNull
        String password
) {
}
