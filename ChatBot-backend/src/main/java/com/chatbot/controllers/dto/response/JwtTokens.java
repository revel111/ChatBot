package com.chatbot.controllers.dto.response;

import java.time.Instant;

public record JwtTokens(
        String accessToken,
        String refreshToken,
        Instant accessExpirationTime,
        Instant refreshExpirationTime
) {
}
