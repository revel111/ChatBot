package com.chatbot.controllers.dto.request;

public record SignInRequestDto(
        String email,
        String password
) {
}
