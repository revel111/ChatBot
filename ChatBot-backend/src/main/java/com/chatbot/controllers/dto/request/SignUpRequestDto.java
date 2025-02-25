package com.chatbot.controllers.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
        String password
) {
}
