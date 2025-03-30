package com.chatbot.controllers.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileDto(
        @NotNull
        @Size(min = 3, max = 20)
        String username,

        @NotNull
        @Email
        String email
) {
}
