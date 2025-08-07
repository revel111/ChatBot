package com.chatbot.controllers;

import com.chatbot.controllers.dto.request.ChangePasswordDto;
import com.chatbot.controllers.dto.request.UpdateUserProfileDto;
import com.chatbot.controllers.dto.response.UserProfileDto;
import com.chatbot.security.contexts.AuthContextHolder;
import com.chatbot.services.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-profile")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User profile management")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    @Operation(
            description = "Get the authenticated user's profile information.",
            summary = "Get User Profile",
            responses = {
                    @ApiResponse(
                            description = "Successful retrieval of user profile",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            }
    )
    public ResponseEntity<UserProfileDto> getUserProfile() {
        var userId = AuthContextHolder.get().id();
        return ResponseEntity.ok(userProfileService.getInfo(userId));
    }

    @DeleteMapping
    @Operation(
            description = "Delete the authenticated user's profile.",
            summary = "Delete User Profile",
            responses = {
                    @ApiResponse(
                            description = "Successful deletion of user profile",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            }
    )
    public ResponseEntity<Void> deleteById() {
        var userId = AuthContextHolder.get().id();
        userProfileService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    @Operation(
            description = "Update the authenticated user's profile information.",
            summary = "Update User Profile",
            responses = {
                    @ApiResponse(
                            description = "Successful update of user profile",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Invalid input data",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "User with this email already exists",
                            responseCode = "409"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body containing updated user profile information.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserProfileDto.class)
                    )
            )
    )
    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UpdateUserProfileDto updateUserProfileDto) {
        var userId = AuthContextHolder.get().id();
        return ResponseEntity.ok(userProfileService.updateProfile(updateUserProfileDto, userId));
    }

    @PutMapping("/me/password")
    @Operation(
            description = "Update the authenticated user's password.",
            summary = "Update User Password",
            responses = {
                    @ApiResponse(
                            description = "Successful update of user password",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Invalid input data",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized access",
                            responseCode = "401"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body containing the current and new password.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangePasswordDto.class)
                    )
            )
    )
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        var userId = AuthContextHolder.get().id();
        userProfileService.updatePassword(changePasswordDto, userId);
        return ResponseEntity.ok().build();
    }
}
