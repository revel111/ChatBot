package com.chatbot.controllers;

import com.chatbot.controllers.dto.request.SignInRequestDto;
import com.chatbot.controllers.dto.request.SignUpRequestDto;
import com.chatbot.controllers.dto.response.JwtTokenResponse;
import com.chatbot.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserProfileService userProfileService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtTokenResponse> signIn(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        return ResponseEntity.ok(userProfileService.signIn(signInRequestDto));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        userProfileService.signUp(signUpRequestDto);
        return ResponseEntity.ok(null);
    }
}
