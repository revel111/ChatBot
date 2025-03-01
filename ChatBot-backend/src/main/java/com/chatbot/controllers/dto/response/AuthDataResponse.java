package com.chatbot.controllers.dto.response;

import java.util.UUID;

public record AuthDataResponse(
        JwtTokens jwtTokens,
        UUID id,
        String username,
        String email
) {
}
