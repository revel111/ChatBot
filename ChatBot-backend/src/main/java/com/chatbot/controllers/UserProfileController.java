package com.chatbot.controllers;

import com.chatbot.controllers.dto.response.UserProfileDto;
import com.chatbot.security.contexts.AuthContextHolder;
import com.chatbot.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        var authContext = AuthContextHolder.get();
        return ResponseEntity.ok(userProfileService.getInfo(authContext));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById() {
        var userId = AuthContextHolder.get().id();
        userProfileService.deleteById(userId);
        return ResponseEntity.ok(null);
    }

//    @PutMapping
//    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody) {
//
//    }
}
