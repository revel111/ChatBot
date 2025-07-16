package com.chatbot.controllers;

import com.chatbot.controllers.dto.request.SignInRequestDto;
import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.controllers.dto.response.AuthDataResponse;
import com.chatbot.services.UserProfileService;
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
public class AuthController {

    private final UserProfileService userProfileService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthDataResponse> signIn(@RequestBody SignInRequestDto signInRequestDto) {
        return ResponseEntity.ok(userProfileService.signIn(signInRequestDto));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthDataResponse> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.ok(userProfileService.signUp(signUpRequestDto));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthDataResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(userProfileService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {
        userProfileService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }
}
