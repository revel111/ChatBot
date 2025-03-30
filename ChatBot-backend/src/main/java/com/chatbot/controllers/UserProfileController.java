package com.chatbot.controllers;

import com.chatbot.controllers.dto.request.ChangePasswordDto;
import com.chatbot.controllers.dto.request.UpdateUserProfileDto;
import com.chatbot.controllers.dto.response.UserProfileDto;
import com.chatbot.security.contexts.AuthContextHolder;
import com.chatbot.services.UserProfileService;
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
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        var userId = AuthContextHolder.get().id();
        return ResponseEntity.ok(userProfileService.getInfo(userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById() {
        var userId = AuthContextHolder.get().id();
        userProfileService.deleteById(userId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UpdateUserProfileDto updateUserProfileDto) {
        var userId = AuthContextHolder.get().id();
        return ResponseEntity.ok(userProfileService.updateProfile(updateUserProfileDto, userId));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        var userId = AuthContextHolder.get().id();
        userProfileService.updatePassword(changePasswordDto, userId);
        return ResponseEntity.ok(null);
    }
}
