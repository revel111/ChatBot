package com.chatbot.handlers.dto;


import java.time.LocalDateTime;

public record ErrorResponse(
        int statusCode,
        String code,
        String description,
        LocalDateTime timestamp
) {
}
