package com.chatbot.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "revel111",
                        email = "arturarshava@gmail.com",
                        url = "https://github.com/revel111"
                ),
                title = "Chatbot API",
                version = "1.0.0",
                description = "API for a chatbot application that allows users to create and manage chats, send messages, and manage user profiles.",
                termsOfService = "Use however you want."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local server"
                ),
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Bearer authentication scheme for securing API endpoints.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
