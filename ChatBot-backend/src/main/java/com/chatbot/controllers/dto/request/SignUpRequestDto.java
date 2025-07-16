package com.chatbot.controllers.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
        @NotNull
        @Size(min = 3, max = 20)
        String username,

        @NotNull
        @Email
        String email,

        @NotNull
        @Size(min = 8, max = 20)
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")
        String password,

        @NotNull
        String confirmPassword
) {
}
