package com.chatbot.controllers;

import com.chatbot.controllers.dto.request.SignInRequestDto;
import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.controllers.dto.response.AuthDataResponse;
import com.chatbot.services.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final UserProfileService userProfileService;

    @PostMapping("/sign-in")
    @Operation(
            description = "Sign in an existing user. The email and password fields are required.",
            summary = "Sign In User",
            responses = {
                    @ApiResponse(
                            description = "Successful sign in",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Invalid email or password",
                            responseCode = "401"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Sign in request containing email and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignInRequestDto.class)
                    )
            )
    )
    public ResponseEntity<AuthDataResponse> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        return ResponseEntity.ok(userProfileService.signIn(signInRequestDto));
    }

    @PostMapping("/sign-up")
    @Operation(
            description = "Sign up a new user. The password and confirmPassword fields must match.",
            summary = "Sign Up User",
            responses = {
                    @ApiResponse(
                            description = "Successful sign up",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "User with this email already exists",
                            responseCode = "409"
                    ),
                    @ApiResponse(
                            description = "Passwords do not match",
                            responseCode = "400"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Sign up request containing email, password, and confirmPassword",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignUpRequestDto.class)
                    )
            )
    )
    public ResponseEntity<AuthDataResponse> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.ok(userProfileService.signUp(signUpRequestDto));
    }

    @GetMapping("/refresh")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            description = "Refresh the authentication tokens using the refresh token.",
            summary = "Refresh Tokens",
            responses = {
                    @ApiResponse(
                            description = "Successful token refresh",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Invalid or expired refresh token",
                            responseCode = "401"
                    )
            },
            parameters = @Parameter(
                    name = "Authorization",
                    description = "Refresh token in the format 'Bearer <token>.",
                    required = true,
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", format = "Bearer token")
            )
    )
    public ResponseEntity<AuthDataResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(userProfileService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            description = "Logout the user by invalidating the refresh token.",
            summary = "Logout User",
            responses = {
                    @ApiResponse(
                            description = "Successful logout",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Invalid or missing refresh token",
                            responseCode = "400"
                    )
            },
            parameters = @Parameter(
                    name = "Authorization",
                    description = "Refresh token in the format 'Bearer <token>.",
                    required = true,
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", format = "Bearer token")
            )
    )
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {
        userProfileService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }
}
